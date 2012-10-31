/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.presentation.ClientController;
import ads.resources.communication.ServerCommunicator;
import ads.resources.communication.TimeoutException;
import ads.resources.data.ADSUser;
import ads.resources.data.BookedDelivery;
import ads.resources.data.Delivery;
import ads.resources.datacontroller.DeliveryHistory;
import ads.resources.data.DeliveryStep;
import ads.resources.data.FloorMap;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.RobotPositionAccessor;
import ads.resources.datacontroller.SectionedBox;
import ads.resources.datacontroller.UserController;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author MFA, mgamell
 */
public class DeliveryCoordinator {
    private static DeliveryCoordinator singleton = new DeliveryCoordinator();
    private Thread deliveryWaiterThread;
    
    // Eager initialization
    public static DeliveryCoordinator getInstance() {
        return singleton;
    }
    
    private DeliveryCoordinator()
    {
        deliveryWaiterThread = new Thread(new DeliveryWaiterThread());
        deliveryWaiterThread.start();
    }

    public void bookDelivery(double urgency, List<String> targetListUsername, String username)
    throws NonBookedDeliveryException
    {
        EntityManager em = Persistance.getEntityManager();
        ADSUser sender = null;
        try {
            sender = em.find(ADSUser.class, username);
        } catch (Exception e) {
            throw new NonBookedDeliveryException("receiver username "+username+"were not found");
        }

        // If the sender is incorrect, then we can't book the delivery.
        if(sender == null) {
            throw new NonBookedDeliveryException("receiver username "+username+"were not found");
        }

        if(urgency > 1.0 || urgency < 0.0) {
            throw new NonBookedDeliveryException("urgency must be a double value between 0.0 and 1.0");
        }

        em.getTransaction().begin();
        // Add the assigned deliveries to the pending deliveries
        for(String receiverUsername : targetListUsername) {
            ADSUser receiver;
            try {
                receiver = em.find(ADSUser.class, receiverUsername);
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw new NonBookedDeliveryException("receiver username "+receiverUsername+"were not found");
            }
            java.util.Date date = new java.util.Date();
            Timestamp timestampField = new Timestamp(date.getTime());
            DeliveryStep state = new BookedDelivery((Timestamp)timestampField.clone());
            em.persist(state);

            // The following instruction assigns delivery to the ADSuser automatically
            Delivery newDel = new Delivery(sender, receiver, timestampField, state, urgency);
            em.persist(newDel);
        }
        em.getTransaction().commit();
    }
    
    private class DeliveryWaiterThread implements Runnable {
        private boolean finish;
        private DeliveryWaiterThread() {
            this.finish = false;
        }
        @Override
        public void run() {
            Logger log = Logger.getLogger(DeliveryWaiterThread.class.getName());
            while(!finish) {
                log.log(Level.INFO, "begin loop");
                // Check if there are pending deliveries
                if(!DeliveryHistory.hasPendingDeliveries()) {
                log.log(Level.INFO, " no pending deliveries");
                    // There are no pending deliveries. Check the robot position
                    if(RobotPositionAccessor.getRobotPosition().getId()==FloorMap.getIdPoint0()) {
                        // The robot is in the point 0, just Wait a delay and retry.
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {}
                    } else {
                        log.log(Level.INFO, "  robot in point !=0... id={0}", RobotPositionAccessor.getRobotPosition().getId());
                        // The robot is not in the point 0, continue moving.
                        // Move the robot to the next point
                        RobotPositionAccessor.setMoving(true);
                        ServerCommunicator.moveRobotToNextPoint();
                        // Update the position
                        RobotPositionAccessor.setMoving(false);
                        RobotPositionAccessor.updateRobotPositionToNext();
                    }
                } else {
                    log.log(Level.INFO, " pending deliveries");
                    // There are pending deliveries!
                    // Move the robot to the next point
                    RobotPositionAccessor.setMoving(true);
                    ServerCommunicator.moveRobotToNextPoint();
                    // Update the position
                    RobotPositionAccessor.setMoving(false);
                    RobotPositionAccessor.updateRobotPositionToNext();
                    // Get Most prioritary delivery.
                    log.log(Level.INFO, " robot in point ... id={0}", RobotPositionAccessor.getRobotPosition().getId());
                    // Check if the robot needs to stop at that position or it
                    // needs to continue to the next one
                    
                    for(Delivery delivery = DeliveryHistory.getMostPrioritaryDelivery(); (delivery != null) && delivery.getNextUser().getOffice().equals(RobotPositionAccessor.getRobotPosition())
                            && !(SectionedBox.isFull() && delivery.isBookedNotYetPickedUp());
                            delivery = DeliveryHistory.getMostPrioritaryDelivery()) {
                        log.log(Level.INFO, "  mostPrioritaryDelivery in point ... id={0}", delivery.getNextUser().getOffice().getId());
                        log.log(Level.INFO, "  needs to stop!");
                        // The robot needs to stop!
                        ServerCommunicator.ringBuzzer();
                        boolean error = true;
                        for(int i=0 ; i<3 ; i++) {
                            String password;
                            try {
                                password = ServerCommunicator.waitForPassword(10);
                            } catch(TimeoutException e) {
                                break;
                            }
                            if(UserController.checkPassword(delivery.getNextUser(), password)) {
                                error = false;
                                break;
                            } else {
                                ServerCommunicator.showPasswordIncorrectWarning();
                            }
                        }
                        if (!error) {
                            ServerCommunicator.showPasswordCorrectMessage();
                            int trayNum = -1;

                            if(delivery.isPickedUpNotYetDelivered()) {
                                trayNum = SectionedBox.deallocateBox(delivery);
                            } else if (delivery.isBookedNotYetPickedUp()) {
                                trayNum = SectionedBox.allocateBox(delivery);
                            } else {
                                throw new RuntimeException("This delivery is picked up and delivered!");
                            }
                            
                            ServerCommunicator.openTray(trayNum);
                            
                            delivery.updateState();
                        } else {
                            ServerCommunicator.showPasswordIncorrectError();
                            //todo: IMPORTANT: add state to the delivery saying that this pickup/delivery failed!
                            
                            // todo: assumption: if the password is incorrectly inserted three times in this office, leave the office (regardless the remaining deliveries)
                            break;
                        }
                    }
                }
            }
        }
    }
}

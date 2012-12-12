
package ads.logic;

import ads.presentation.ClientController;
import ads.resources.communication.ServerCommunicator;
import ads.resources.communication.TimeoutException;
import ads.resources.data.ADSUser;
import ads.resources.data.BookedDelivery;
import ads.resources.data.Delivery;
import ads.resources.datacontroller.DeliveryHistory;
import ads.resources.data.DeliveryStep;
import ads.resources.datacontroller.FloorMap;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.RobotPositionAccessor;
import ads.resources.datacontroller.SectionedBox;
import ads.resources.datacontroller.UserController;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;

/**
 * Delivery Coordinator is the abstract base class which helps 
 * with the process of delivery.
 * <p>
 * Operations in Delivery Coordinator helping with the process of delivery
 * includes checking the validity of sender and receiver by comparing the 
 * input from user with the data stored in the database; checking the priority
 * in order to decide which delivery should go first; checking the pending 
 * delivery numbers and checking the location of robot to make the robot pick up 
 * and deliver correctly.
 */
public class DeliveryCoordinator {
    // DeliveryCoordinator object singleton
    private static DeliveryCoordinator singleton = new DeliveryCoordinator();
    // Thread Object deliveryWaiterThread
    private Thread deliveryWaiterThread; 
    private ServerCommunicator serverCommunicator;
    
    public void setServerCommunicator(ServerCommunicator serverCommunicator) {
        this.serverCommunicator = serverCommunicator;
    }
    
 /**
  * Get a object of DeliveryCordinator
  * @return singleton, which is a user defined DeliveryCoordinator object
  */
    public static DeliveryCoordinator getInstance() {
        return singleton;
    }
/**
 * The DeliveyCoordinator constructor creates a new thread of
 * control that will execute Thread object's run() method.
 */
    private DeliveryCoordinator()
    {
        deliveryWaiterThread = new Thread(new DeliveryWaiterThread());
        deliveryWaiterThread.start();
    }
    
/**
 * Allow sender books single delivery or multiple deliveries 
 * to receiver with different priorities (sender and receiver's 
 * information, including name and office should be in the database).
 * <p>
 * If sender or receiver are invalid, priority number is not among 0.0-1.0, 
 * {@link NonbooedDeliveryException} will be thrown. 
 * 
 * @param priority   the priority of deliveries
 * @param targetListUsername  the list of target usernames
 * @param username    the receiver name
 * @throws NonBookedDeliveryException  if sender, receive or priority 
 * number is not correct
 */
    public void bookDelivery(double priority, List<String> targetListUsername, String username)
    throws NonBookedDeliveryException
    {
        EntityManager em = Persistance.getEntityManager();
        ADSUser sender = null;
        // find the username in ADSUser
        try {
            sender = em.find(ADSUser.class, username);
        } catch (Exception e) {
            throw new NonBookedDeliveryException("receiver username "+username+"were not found");
        }

        // If the sender is incorrect, then we can't book the delivery.
        if(sender == null) {
            throw new NonBookedDeliveryException("receiver username "+username+"were not found");
        }
        // if the priority is not valid, then we can't book the delivery
        if(priority > 1.0 || priority < 0.0) {
            throw new NonBookedDeliveryException("urgency must be a double value between 0.0 and 1.0");
        }

        em.getTransaction().begin();
        // Add the assigned deliveries to the pending deliveries
        for(String receiverUsername : targetListUsername) {
            ADSUser receiver;
            // find the receiverUsername in ADSUser
            try {
                receiver = em.find(ADSUser.class, receiverUsername);
            } catch (Exception e) {
                em.getTransaction().rollback();
                throw new NonBookedDeliveryException("receiver username "+receiverUsername+"were not found");
            }
            java.util.Date date = new java.util.Date();
            Timestamp timestampField = new Timestamp(date.getTime());
            // The following instruction assigns delivery to the ADSuser automatically
            Delivery newDel = new Delivery(sender, receiver, timestampField, priority);
            DeliveryStep state = new BookedDelivery((Timestamp)timestampField.clone(), newDel);
            em.merge(newDel);
            em.merge(state);
            //Notify the receiver about the booked deliveries
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            NotificationCoordinator.notifyReceiver_DeliveryBooked((receiver.getFirstName()+" "+receiver.getLastName()),
                                                                  receiver.getEmail(), dateFormat.format(date));
        }
        // commit the delivery into database
        em.getTransaction().commit();
    }

/**
 * When a thread belonging to the class DeliveryWaiterThread is run,
 * it will check whether there are pending deliveries, the robot 
 * position, the priority of the delivery and the validity 
 * of the password.
 * <p>
 * If there is no pending delivery and the robot is in position 0, the system
 * will wait and retry in 5 seconds; if the robot is not in position 0, move
 * the robot to next point.If there are pending deliveries in the system, 
 * just move the robot to next point.If there are deliveries with high 
 * priority, it will be delivered first. Also, the password will be checked, if
 * password is correct,first check whether the package is picked up or not, if
 * it has been picked up but not delivered, open a tray to deliver; if
 * it is booked but bot picked up, open a tray to pick it up. 
 * If the password is not correct, show Incorrect Error. 
 */
    private class DeliveryWaiterThread implements Runnable {
        private boolean finish;
        // constructor defines bollean finish is false
        private DeliveryWaiterThread() {
            this.finish = false;
        }
        @Override
        public void run() {
            Logger log = Logger.getLogger(DeliveryWaiterThread.class.getName());
            while(!finish) {
//                log.log(Level.INFO, "begin loop");
                // Check if there are pending deliveries
                if(!DeliveryHistory.hasPendingDeliveries()) {
                    // There are no pending deliveries. Check the robot position
                    if(RobotPositionAccessor.getRobotPosition().equals(FloorMap.getStartPoint())) {
                        // The robot is in the point 0, just Wait a delay and retry.
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ex) {}
                    } else {
                        log.log(Level.INFO, " no pending deliveries");
                        log.log(Level.INFO, "  robot in point !=0... id={0}", RobotPositionAccessor.getRobotPosition().getId());
                        // The robot is not in the point 0, continue moving.
                        // Move the robot to the next point
                        RobotPositionAccessor.setMoving(true);
                        serverCommunicator.moveRobotToNextPoint();
                        // Update the position
                        RobotPositionAccessor.setMoving(false);
                        RobotPositionAccessor.updateRobotPositionToNext();
                    }
                } else {
                    log.log(Level.INFO, " pending deliveries");
                    // There are pending deliveries!
                    // Move the robot to the next point
                    RobotPositionAccessor.setMoving(true);
                    serverCommunicator.moveRobotToNextPoint();
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
                        serverCommunicator.ringBuzzer();
                        boolean error = true;
                        // check the password
                        for(int i=0 ; i<3 ; i++) {
                            String password;
                            try {
                                password = serverCommunicator.waitForPassword(10, delivery.getNextUser().getUsername());
                            } catch(TimeoutException e) {
                                break;
                            }
                            if(UserController.checkPassword(delivery.getNextUser(), password)) {
                                error = false;
                                break;
                            } else {
                                serverCommunicator.showPasswordIncorrectWarning();
                            }
                        }
                        // if the password is correct, show correct message
                        if (!error) {
                            serverCommunicator.showPasswordCorrectMessage();
                            int trayNum = -1;
                            // check whether the delivery is picked up.
                            if(delivery.isPickedUpNotYetDelivered()) {
                                trayNum = SectionedBox.deallocateBox(delivery);
                                //That means the delivery is successfully delivered !
                                String senderFullName=delivery.getSender().getFirstName()+" "+delivery.getSender().getLastName();
                                Date date=(Date)(delivery.getTimestampField());
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                System.out.println(dateFormat.format(date));
                                NotificationCoordinator.notifySender_DeliveryDelivered(senderFullName, 
                                                                                        delivery.getSender().getEmail(),dateFormat.format(date));
                            } else if (delivery.isBookedNotYetPickedUp()) {
                                trayNum = SectionedBox.allocateBox(delivery);
                            } else {
                                throw new RuntimeException("This delivery is picked up and delivered!");
                            }
                            // open a tray
                            serverCommunicator.openTray(trayNum);
                            
                            delivery.updateState();
                        } else {
                            // if the password is not correct, show error
                            serverCommunicator.showPasswordIncorrectError();
                            //todo: IMPORTANT: add state to the delivery saying 
                            //that this pickup/delivery failed.
                            // todo: assumption: if the password is incorrectly 
                            //inserted three times in this office, leave the 
                            //office (regardless the remaining deliveries)
                            break;
                        }
                    }
                }
            }
        }
    }
}

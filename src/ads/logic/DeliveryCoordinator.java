/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.BookedDelivery;
import ads.resources.data.Delivery;
import ads.resources.data.DeliveryHistory;
import ads.resources.data.DeliveryStep;
import ads.resources.data.Persistance;
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
        //deliveryWaiterThread.start();
    }

    public void bookDelivery(String urgency, List<String> targetListUsername, String username)
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

        //TODO: Assign delivery to the ADSuser
        // First add the deliveries to the user in the userlist

        em.getTransaction().begin();
        //Add the assigned deliveries to the pending deliveries
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
            while(!finish) {
                // Check if there are pending deliveries
                if(DeliveryHistory.hasPendingDeliveries()) {
                    // There are no pending deliveries. Wait a delay and retry.
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ex) {}
                    continue;
                }
                // There are pending deliveries!
                // Get Most prioritary delivery.
                Delivery delivery = DeliveryHistory.getMostPrioritaryDelivery();
                //
                
//                //todo
            }
        }
    }
}

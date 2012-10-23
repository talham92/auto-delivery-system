/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.BookedDelivery;
import ads.resources.data.Delivery;
import ads.resources.data.DeliveryStep;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.persistence.EntityManager;

/**
 *
 * @author MFA
 */
public class DeliveryCoordinator {
//    private ArrayList<Delivery> pendingDeliveries;
    private EntityManager em;
    
    public DeliveryCoordinator(EntityManager em)
    {
//        pendingDeliveries = new ArrayList<Delivery>();
        this.em = em;
    }

    public void bookDelivery(String urgency, ArrayList<String[]> targetListUsername, String username)
    throws NonBookedDeliveryException
    {
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
        for(String[] receiverUsername : targetListUsername) {
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
        }
        em.getTransaction().commit();
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.ADSUser;
import ads.resources.data.DeliveredDelivery;
import ads.resources.data.Delivery;
import adsrobotstub.RobotStub;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class DeliveryHistory implements Serializable {
    private static Logger logger = Logger.getLogger(DeliveryHistory.class.getName());

    private DeliveryHistory() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }

    public static boolean hasPendingDeliveries() {
        return DeliveryHistory.getMostPrioritaryDelivery()!=null;
    }

    public static Delivery getMostPrioritaryDelivery() {
        EntityManager em = Persistance.getEntityManager();
        
        try {
            List<Delivery> pendingDeliveries = em.createNamedQuery("Delivery.searchPending")
            .setParameter("delivereddeliveryclass", Arrays.asList(DeliveredDelivery.class))
            .getResultList();
            if(pendingDeliveries == null || pendingDeliveries.isEmpty()) {
//                throw new Exception("There are no deliveries!");
                return null;
            }

            Delivery mostPrioritaryDelivery = null;
            double maxPriority = -1;
            
            logger.finest(pendingDeliveries.toString());

            for(Delivery d : pendingDeliveries) {
                int distance = FloorMap.calculateNumOfHops(RobotPositionAccessor.getRobotPosition(), d.getNextUser().getOffice());
                double distancePriority = distance == 0 ? 1 : 1/distance;
                double priority = distancePriority + d.getPriority();
                if(priority > maxPriority) {
                    mostPrioritaryDelivery = d;
                    maxPriority = priority;
                }
            }
            
            return mostPrioritaryDelivery;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Delivery> getUserDeliveryList(ADSUser sender) {
        EntityManager em = Persistance.getEntityManager();
        
        try {
            List<Delivery> deliveries = em.createNamedQuery("Delivery.searchUserDeliveryList")
            .setParameter("sender", sender)
            .getResultList();
            
            return deliveries;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Delivery> getDeliveryList() {
        EntityManager em = Persistance.getEntityManager();
        
        try {
            List<Delivery> deliveries = em.createNamedQuery("Delivery.searchAllDeliveryList")
            .getResultList();
            
            return deliveries;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Delivery getDelivery(int deliveryId) {
        EntityManager em = Persistance.getEntityManager();
        return em.find(Delivery.class, deliveryId);
    }

}

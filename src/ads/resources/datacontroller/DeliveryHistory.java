/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.BookedDelivery;
import ads.resources.data.Delivery;
import ads.resources.data.FloorMap;
import ads.resources.data.PickedUpDelivery;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class DeliveryHistory implements Serializable {

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
            .setParameter("class", Arrays.asList(BookedDelivery.class, PickedUpDelivery.class))
            .getResultList();
            if(pendingDeliveries == null || pendingDeliveries.isEmpty()) {
                throw new Exception("There are no deliveries!");
            }

            Delivery mostPrioritaryDelivery = null;
            double maxPriority = -1;

            for(Delivery d : pendingDeliveries) {
                double priority = FloorMap.getMaxNumOfHops()/FloorMap.calculateNumOfHops(RobotPositionAccessor.getRobotPosition(), d.getNextUser().getOffice()) + d.getUrgency();
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
}

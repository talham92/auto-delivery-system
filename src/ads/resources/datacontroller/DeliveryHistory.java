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



/** 
* a class designed to record the delivery history. 
*/  


public class DeliveryHistory implements Serializable {
    private static Logger logger = Logger.getLogger(DeliveryHistory.class.getName());

    private DeliveryHistory() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }

/**  
 * hasPendingDeliveries is a function used to decide if there are pending deliveries. 
 * @return 
*  
*/  
    public static boolean hasPendingDeliveries() {
        return DeliveryHistory.getMostPrioritaryDelivery()!=null;
    }

/**  
 * return 
 * In auto delivery system, any booked delivery will be assigned a priority from 1 to 5. getMostPrioritaryDelivery is a function used to rank the delivery and find out the delivery with highest priority. 
*  
* @param em a parameter used to detect delivery list
* @param mostPrioritaryDelivery a parameter used to determine if the current delivery has the highest priority
* @return Null if no delivery record in the system
* @return excrescent return
* @exception logger.finest transform the delivery record into string
* @exception NullPointerException throw when parameter n is null
*/
    
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

    
    
/**  
 * getUserDeliveryList is a function used to list all delivery booked by user. 
 * @param sender 
* @param em a parameter used to detect delivery list
* @return deliveries or NULL if the searching is success or not
*  
*/
      
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

/**  
 * getDeliveryList is a function used to search all delivery records. 
 * @return 
*  
*/     
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


/**  
 * getDelivery is a function used to get a delivery record. 
 * @param deliveryId 
 * @return 
*  
*/
    
    public static Delivery getDelivery(int deliveryId) {
        EntityManager em = Persistance.getEntityManager();
        return em.find(Delivery.class, deliveryId);
    }

}

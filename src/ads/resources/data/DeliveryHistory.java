/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class DeliveryHistory implements Serializable {

    private DeliveryHistory() throws Exception {
        throw new Exception("Don't try to instantiate DeliveryHistory");
    }

    public static boolean hasPendingDeliveries() {
//todo
        throw new RuntimeException("nonimplemented");
    }

    public static Delivery getMostPrioritaryDelivery() {
        EntityManager em = Persistance.getEntityManager();
        throw new RuntimeException("nonimplemented");
//todo
    }
}

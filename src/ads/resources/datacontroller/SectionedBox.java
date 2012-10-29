/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.Box;
import ads.resources.data.Delivery;
import ads.resources.data.Office;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author mgamell
 */
public class SectionedBox {

    public SectionedBox() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }
    
    public static int allocateBox(Delivery delivery) {
        EntityManager em = Persistance.getEntityManager();
        Box box;
        try {
            box = (Box) em.createNamedQuery("Box.searchEmpty")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch(NonUniqueResultException e) {
            throw e;
        } catch(NoResultException e) {
            throw e;
        }
        if(box.getDelivery() == null) {
            throw new RuntimeException("Box.searchEmpty returned a box nonempty!");
        }
        box.setDelivery(delivery);
        return box.getId();
    }

    public static int deallocateBox(Delivery delivery) {
        EntityManager em = Persistance.getEntityManager();
        Box box = (Box) em.createNamedQuery("Box.searchDelivery")
                .setParameter("sender", delivery.getSender())
                .setParameter("receiver", delivery.getReceiver())
                .setParameter("timestampfield", delivery.getTimestampField())
                .getSingleResult();
        if(!box.getDelivery().equals(delivery)) {
            throw new RuntimeException("Box.searchDelivery returned a box incorrect!");
        }
        box.setDelivery(null);
        return box.getId();
    }


    public static boolean isFull() {
        EntityManager em = Persistance.getEntityManager();
        try {
            Box box = (Box) em.createNamedQuery("Box.searchEmpty")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch(NonUniqueResultException e) {
            throw e;
        } catch(NoResultException e) {
            return true;
        }
        return false;
    }
    
}

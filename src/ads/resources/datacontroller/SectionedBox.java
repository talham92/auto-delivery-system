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
/**
 * A class used to section the box into different segments and allocate these
 * segments to the booked delivery.
 */
public class SectionedBox {

    /**
     * SectionedBox deal with the exception in sectioning box.
     *
     * @throws Exception
     *     
*/
    public SectionedBox() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }

    /**
     * allocateBox is a function of allocating box to the delivery.
     *
     * @param delivery
     * @return
     *     
*/
    public static int allocateBox(Delivery delivery) {
        EntityManager em = Persistance.getEntityManager();
        Box box;
        try {
            box = (Box) em.createNamedQuery("Box.searchEmpty")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NonUniqueResultException e) {
            throw e;
        } catch (NoResultException e) {
            throw e;
        }
        if (box.getDelivery() != null) {
            throw new RuntimeException("Box.searchEmpty returned a box nonempty!");
        }
        em.getTransaction().begin();
        box.setDelivery(delivery);
        em.getTransaction().commit();
//System.out.println("ALLOCATED BOX: "+);
//Thread.sleep(10000);
        return box.getBoxId();
    }

    /**
     * deallocateBox is a function of deleting the allocated box.
     *
     * @param delivery
     * @return
     *     
*/
    public static int deallocateBox(Delivery delivery) {
        EntityManager em = Persistance.getEntityManager();
        Box box = (Box) em.createNamedQuery("Box.searchDelivery")
                .setParameter("delivery", delivery)
                .getSingleResult();
        if (!box.getDelivery().equals(delivery)) {
            throw new RuntimeException("Box.searchDelivery returned a box incorrect!");
        }
        em.getTransaction().begin();
        box.setDelivery(null);
        em.getTransaction().commit();
        return box.getBoxId();
    }

    /**
     * ifFull is a function of determining if the box is full.
     *
     * @param em a parameter about the persistence of the delivery
     * @return True or False
     *     
*/
    public static boolean isFull() {
        EntityManager em = Persistance.getEntityManager();
        try {
            em.createNamedQuery("Box.searchEmpty")
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NonUniqueResultException e) {
            throw e;
        } catch (NoResultException e) {
            return true;
        }
        return false;
    }
}

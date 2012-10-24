/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author mgamell
 */
public class Persistance {
    private static EntityManagerFactory emf;
    private static EntityManager em = null;

    private Persistance() throws Exception {
        throw new Exception("Don't try to instantiate Persistance");
    }

    public static void initPersistance() {
        emf = Persistence.createEntityManagerFactory("adsPU");
        em = emf.createEntityManager();
    }

    public static void deinitPersistance() {
        em.close();
        emf.close();
    }

    public static EntityManager getEntityManager() {
        if(em == null) {
            throw new RuntimeException("getEntityManager called before initPersistance");
        }
        return em;
    }
}

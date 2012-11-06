/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.ADSUser;
import ads.resources.data.Office;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class UserController {
    public static final int userNotCorrect=0;
    public static final int userCorrect_Admin=1;
    public static final int userCorrect_NotAdmin=2;

    public UserController() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }

    public static boolean checkPassword(ADSUser user, String password) {
        return user.getPassword().equals(password);
    }
    
    public static int checkLogin(String username, String password) {
        EntityManager em = Persistance.getEntityManager();
        try {
            ADSUser u = em.find(ADSUser.class, username);
            // Note that the find in the database is case insensitive. Then, we
            // can't worry only about password, but we need to check also the username
            if (u == null || !u.getUsername().equals(username) || !UserController.checkPassword(u, password))
                return userNotCorrect;
            else{
                if(u.isAdmin())
                    return userCorrect_Admin;
                else
                    return userCorrect_NotAdmin;
            }
        } catch(Exception ex) {
            return userNotCorrect;
        }
    }

    public static ADSUser findUser(String username) {
        EntityManager em = Persistance.getEntityManager();
        try {
            return em.find(ADSUser.class, username);
        } catch(Exception ex) {
            return null;
        }
    }
    
    public static void removeUsers() {
        EntityManager em = Persistance.getEntityManager();
        Set<ADSUser> results = new HashSet<>(20);
        try {
            results.addAll(em.createNamedQuery("User.findAll").getResultList());
            em.getTransaction().begin();
            for(ADSUser o : results) {
                if(o.isAdmin()) {
                    o.setOffice(null);
                    em.persist(o);
                } else {
                    em.remove(o);
                }
            }
            em.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.logic.EmailChecker;
import ads.logic.ServerNonInitializedException;
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
    
    
    public static String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) {
        EntityManager em = Persistance.getEntityManager();
        Office office;
        // Check business rules
        if (!EmailChecker.checkEmail(email)) return "The e-mail address is not correct";
        // 1. A user with a repeated name.
        if (!em.createNamedQuery("User.searchByName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList()
                .isEmpty())
            return "A user with a name "+firstName+" "+lastName+" already exist";
        // 2. A user with a repeated username
        if (em.find(ADSUser.class, username)!=null)
            return "A user with a username "+username+" already exist";

        office = em.find(Office.class, roomNumber);

        // 3. The office does not exist
        if (office == null)
            return "The room number does not exist";

        // 4. The e-mail address syntax is not correct
        if (!EmailChecker.checkEmail(email))
            return "The e-mail address is not correct";
        
        // 5. The password does not match with the repeated password
        if (!password.equals(password1)) return "The password does not match with the repeated password";

        
        // Everything is correct, create and persist the user
        em.getTransaction().begin();
        try {
            ADSUser u = new ADSUser(firstName, lastName, office, email, username, password);
            em.persist(u);
            em.getTransaction().commit();
            // Everything went well, return null
            return null;
        }
        catch(Exception ex)
        {
            //em.getTransaction().rollback();
            System.out.println(ex.toString());
            ex.printStackTrace();
            em.getTransaction().rollback();
            // There was an error, retorn the appropiate error message
            return "Unexpected error occurred when storing user information";
        }
    }

    
    public static Set<ADSUser> searchUser_NameOffice(String name, String office) {
        EntityManager em = Persistance.getEntityManager();

        Set<ADSUser> results = new HashSet<>(20);
        try {
            if(name != null && !name.equals("")) {
                results.addAll(em.createNamedQuery("User.searchByPartialNameES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameSSES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameSES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameCFLSES")
                .setParameter("name", name)
                .getResultList());
            }
            if(office != null && !office.equals("")) {
                results.addAll(em.createNamedQuery("User.searchByOffice")
                .setParameter("office", office)
                .getResultList());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }
    
    public static void addAdmin() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("Admin", "istrator", null, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        em.getTransaction().commit();
    }

}

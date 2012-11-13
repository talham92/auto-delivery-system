/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.logic.EmailChecker;
import ads.resources.data.ADSUser;
import ads.resources.data.Office;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */

/** 
* @class UserController  
* @a class providing a controlling interface to users. 
*/ 

public class UserController {
    
/**
* @param userNotCorrect a parameter about if the user of the system is correct
* @param userCorrect_Admin a parameter assign to the user if he/she is a administrator
* @param userCorrect_NotAdmin a parameter assign to the user if he/she is not a administrator 
*/
    
    public static final int userNotCorrect=0;
    public static final int userCorrect_Admin=1;
    public static final int userCorrect_NotAdmin=2;
    private static Logger logger = Logger.getLogger(UserController.class.getName());

/**  
* @UserController is a function used to solve the exception case on the controlling. 
*  
*/
    
    public UserController() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }
    
/**  
* @checkPassword is a function used to check if the password inputed by the user is equaled to that saved in the system. 
*  
*/
    public static boolean checkPassword(ADSUser user, String password) {
        return user.getPassword().equals(password);
    }
    
/**  
* @checkLogin is a function used to classify the login into incorrect, correct by administrator, and correct by user but not administrator. 
*  
*/
    public static int checkLogin(String username, String password) {
        EntityManager em = Persistance.getEntityManager();
        try {
            ADSUser u = em.find(ADSUser.class, username);
         /**
         *Note that the find in the database is case insensitive. Then, we
         *can't worry only about password, but we need to check also the username
         */
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

    
/**  
* @findUser is a function about the persistence of the user account
*  
*/  
    public static ADSUser findUser(String username) {
        EntityManager em = Persistance.getEntityManager();
        try {
            return em.find(ADSUser.class, username);
        } catch(Exception ex) {
            return null;
        }
    }
    
    
/**  
* @removeUsers is a function of removing user accounts from the system
*  
*/    
    public static void removeUsers() {
        EntityManager em = Persistance.getEntityManager();
        Set<ADSUser> results = new HashSet(20);
        try {
            results.addAll(em.createNamedQuery("User.findAll").getResultList());
            em.getTransaction().begin();
            for(ADSUser o : results) {
                if(o.isAdmin()) {
                    o.setOffice(null);
                    em.merge(o);
                } else {
                    em.remove(o);
                }
            }
            em.getTransaction().commit();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
/**  
* @register is a function of user registration
*  
*/ 
    public static String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) {
        EntityManager em = Persistance.getEntityManager();
        Office office;
        try {
            /**
            *Check business rules
            */
            if (!EmailChecker.checkEmail(email)) return "The e-mail address is not correct";
            /**
            * 1. A user with a repeated name.
            */
            if (!em.createNamedQuery("User.searchByName")
                    .setParameter("firstName", firstName)
                    .setParameter("lastName", lastName)
                    .getResultList()
                    .isEmpty())
                return "A user with a name "+firstName+" "+lastName+" already exist";
            /**
            * 2. A user with a repeated username
            */
            if (em.find(ADSUser.class, username)!=null)
                return "A user with a username "+username+" already exist";

            try {
                office = (Office) em.createNamedQuery("Office.findByOfficeAdress")
                        .setParameter("officeAddress", roomNumber)
                        .setMaxResults(1)
                        .getSingleResult();
            } catch(Exception ex) {
            /**
            * 3. The office does not exist
            */
                return "The room number does not exist (1)";
            }
            
            /**
            * 4. The office does not exist
            */
            if (office == null)
                return "The room number does not exist (2)";
            /**
            * 5. The e-mail address syntax is not correct
            */
            if (!EmailChecker.checkEmail(email))
                return "The e-mail address is not correct";
            /**
            * 6. The password does not match with the repeated password
            */
            if (!password.equals(password1)) return "The password does not match with the repeated password";

            /**
            * Everything is correct, create and persist the user
            */
            em.getTransaction().begin();
            try {
                ADSUser u = new ADSUser(firstName, lastName, office, email, username, password);
                em.merge(u);
                em.getTransaction().commit();
                /**
                * Everything went well, return null
                */
                return null;
            }
            catch(Exception ex)
            {
                //em.getTransaction().rollback();
                logger.severe(ex.getMessage());
                em.getTransaction().rollback();
                /**
                * There was an error, retorn the appropiate error message
                */
                return "Unexpected error occurred when storing user information";
            }
        } catch(Exception ex) {
            logger.severe(ex.getMessage());
            /**
            * There was an error, retorn the appropiate error message
            */
            return "Unexpected error occurred when storing user information: "+ex.toString();
        }
    }

/**  
* @searchUser_NameOffice is a function of searching the user's name and office
*  
*/ 
    public static Set<ADSUser> searchUser_NameOffice(String name, String office) {
/**
* @param em a parameter about the persistence of the user account
* @return searching results of name and office
*/
        EntityManager em = Persistance.getEntityManager();

        Set<ADSUser> results = new HashSet(20);
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
    
/**  
* @addAdmin is a function of setting the new ADSUser as administrator
*  
*/
    public static void addAdmin() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("Admin", "istrator", null, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.merge(u);
        em.getTransaction().commit();
    }

}

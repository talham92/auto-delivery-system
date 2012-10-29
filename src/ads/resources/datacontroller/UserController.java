/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.ADSUser;
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
}

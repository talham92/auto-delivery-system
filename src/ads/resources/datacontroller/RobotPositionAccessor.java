/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.Office;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */

/** 
* @class RobotPositionAccessor
* @a class designed to get and update the position of the robot. 
*/ 

public class RobotPositionAccessor {
/**  
* @isMoving is a function of recording if the robot is moving. 
*  
*/   
    public static boolean isMoving() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        return r.isIsMoving();
    }
    
/**  
* @setMoving is a function of setting the current stage of the robot as moving. 
*  
*/  
    public static void setMoving(boolean isMoving) {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        r.setIsMoving(isMoving);
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }
    
/**  
* @RobotPositionAccessor is the initiative function of the class. 
*  
*/  
    public RobotPositionAccessor() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }
/**  
* @initiating the robot position. 
*  
*/
    public static void init() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = new ads.resources.data.RobotPosition(FloorMap.getStartPoint(), false);
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }
    
/**  
* @getRobotPosition is a function about getting the robot position from current point to the next point. 
*  
*/
    public static Office getRobotPosition() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        return r.getLastKnownPosition();
    }
    
/**  
* @updateRobotPositionToNext is a function about updating the robot position from current point to the next point. 
*  
*/
    public static void updateRobotPositionToNext() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        r.setLastKnownPosition(r.getLastKnownPosition().getNextOffice());
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }
}

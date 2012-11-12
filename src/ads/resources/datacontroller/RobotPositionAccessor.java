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
public class RobotPositionAccessor {

    public static boolean isMoving() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        return r.isIsMoving();
    }

    public static void setMoving(boolean isMoving) {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        r.setIsMoving(isMoving);
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }

    public RobotPositionAccessor() throws Exception {
        throw new Exception("Don't try to instantiate me");
    }

    public static void init() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = new ads.resources.data.RobotPosition(FloorMap.getStartPoint(), false);
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }

    public static Office getRobotPosition() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        return r.getLastKnownPosition();
    }

    public static void updateRobotPositionToNext() {
        EntityManager em = Persistance.getEntityManager();
        ads.resources.data.RobotPosition r = em.find(ads.resources.data.RobotPosition.class, 1L);
        r.setLastKnownPosition(r.getLastKnownPosition().getNextOffice());
        em.getTransaction().begin();
        em.merge(r);
        em.getTransaction().commit();
    }
}

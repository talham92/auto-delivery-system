/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.logic.ServerInitializedException;
import ads.logic.ServerNonInitializedException;
import ads.resources.data.Office;
import ads.resources.data.Office;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.Persistance;
import adsrobotstub.RobotStub;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
/**
 * A class designed to create map of the floor for the current routine.
 */
public class FloorMap {

    private static Logger logger = Logger.getLogger(FloorMap.class.getName());

    /**
     * calculateNumOfHops is a function used to count the hops number for the
     * delivery.
     *
     * @param origin
     * @param destination
     * @return
     *     
*/
    public static int calculateNumOfHops(Office origin, Office destination) {
        return giveDistanceFromCurrentToDestination(origin, destination);
    }

    /**
     *
     * @return
     */
    public static int getMaxNumOfHops() {
        return giveLengthBtwStartEnd();
    }

    /**
     * getStartPoint is a function used to find the start point for the
     * delivery.
     *
     * @return
     *     
*/
    public static Office getStartPoint() {
        EntityManager em = Persistance.getEntityManager();
        Office o = (Office) em.createNamedQuery("Office.findByOfficeAdress")
                .setParameter("officeAddress", "start")
                .getSingleResult();
        return o;
    }

    /**
     * giveDistanceFromCurrentToDestination is a function use to let the user
     * know the distance between current position of the robot to the
     * destination.
     *
     * @param current
     * @param dest
     * @return
     *     
*/
    public static int giveDistanceFromCurrentToDestination(Office current, Office dest) {
        /**
         * @param totalDist a parameter of the total distance
         * @param temp a parameter of current position
         * @return totalDist when current position equals to destination
         * position
         */
        int totalDist = 0;
        Office temp = current;
        while (!temp.equals(dest)) {
            logger.finest("    partial totalDist = " + totalDist + "  currentOffice = " + temp.getOfficeAddress());
            totalDist += Math.abs(Integer.parseInt(temp.getNextOfficeDist()));
            temp = temp.getNextOffice();
        }
        logger.finest("totalDist = " + totalDist + "  currentOffice = " + temp.getOfficeAddress());
        return totalDist;
    }

    /**
     * giveLengthBtwStartEnd is a function use to let the user know the distance
     * with respect to the office between start position to the destination.
     *
     * @return
     *     
*/
    public static int giveLengthBtwStartEnd() {
        Iterator itr;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        /**
         * @exception find the office with start node
         */
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                .setParameter("officeAddress", "start")
                .getResultList());

        itr = results.iterator();
        Office startN = (Office) itr.next();
        results.clear();
        /**
         * @exception find the office with end node
         */
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                .setParameter("officeAddress", "end")
                .getResultList());
        itr = results.iterator();
        Office endN = (Office) itr.next();
        return giveDistanceFromCurrentToDestination(startN, endN);
    }

    /**
     * createOffice is a function use to create a data structure office.
     *
     * @param office
     *     
*/
    public static void createOffice(Office office) {
        /**
         * @exception persist the office entity to the database
         */
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        em.merge(office);
        em.getTransaction().commit();
    }

    /**
     * clearOffices is a function use to clear the created data structure
     * office.
     *
     * @return
     *     
*/
    public static String clearOffices() {
        String rresult;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        try {
            results.addAll(em.createNamedQuery("Office.findAll").getResultList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        /**
         * First check whether there is already a created map or not in the
         * database
         */
        if (results.size() != 0) {
            rresult = "preCreatedMapDeleted";
        } else {
            rresult = "noPreCreatedMap";
        }
        em.getTransaction().begin();
        Iterator itr = results.iterator();
        while (itr.hasNext()) {
            Office o = (Office) itr.next();
            /**
             * Office o2=em.find(Office.class, o.getOfficeAddress());
             */
            em.remove(o);
        }
        em.getTransaction().commit();
//        Office o=em.find(Office.class, "101");
//        em.getTransaction().begin();
//        em.remove(o);
//        em.getTransaction().commit();
        return rresult;
    }

    /**
     * createLinksBtwOffices is a function use to create a link between two
     * offices.
     *     
*/
    public static void createLinksBtwOffices() {
        String preOfficeAddress, nextOfficeAddress;
        Set<Office> result = new HashSet(20);

        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        try {
            results.addAll(em.createNamedQuery("Office.findAll").getResultList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (Office o : results) {
            logger.info("office! " + o.getOfficeAddress());
            /**
             * Find and link the offices Link preOffice
             */
            preOfficeAddress = o.getPreOfficeAddress();
            logger.info("office! preofficeaddress " + o.getPreOfficeAddress());

            /**
             * if the office is start this is the case
             */
            if (preOfficeAddress != null) {
                logger.info("office preOfficeAddress is !null " + preOfficeAddress);
                result.clear();
                /**
                 * Assuming only one office with this name, this is assured in
                 * the map creation process
                 */
                result.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                        .setParameter("officeAddress", preOfficeAddress)
                        .getResultList());
                if (result.size() != 1) {
//                    System.out.println("Some problem at finding pre office:"+preOfficeAddress+" result_size: "+result.size());
//                    for(Office oy : result) {
//                        System.out.println(oy.getOfficeAddress());
//                    }
                    logger.severe("office preOfficeAddress does not exist ");
                    throw new RuntimeException();
                } else {
                    Iterator itr = result.iterator();
                    o.setPreOffice((Office) itr.next());
                    em.getTransaction().begin();
                    em.persist(o);
                    em.getTransaction().commit();
                }
            }
            /**
             * Link nextOffice
             */
            nextOfficeAddress = o.getNextOfficeAddress();
            /**
             * if the office is end this is the case
             */
            if (nextOfficeAddress != null) {
                result.clear();
                /**
                 * Assuming only one office with this name, this is assured in
                 * the map creation process
                 */
                result.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                        .setParameter("officeAddress", nextOfficeAddress)
                        .getResultList());

                if (result.size() != 1) {
//                    System.out.println("Some problem at finding next office: "+nextOfficeAddress+"result_size: "+result.size());
//                    for(Office oy : result) {
//                        System.out.println(oy.getOfficeAddress());
//                    }
                    throw new RuntimeException();
                } else {
                    Iterator itr = result.iterator();
                    o.setNextOffice((Office) itr.next());
                }
            }
        }
        //goFromStartToEnd();
    }

    /**
     * getMapDrawingArray is a function use to get all office address used to
     * create the map.
     *
     * @return
     *     
*/
    public static ArrayList<String[]> getMapDrawingArray() {
        ArrayList<String[]> r = new ArrayList();

        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                .setParameter("officeAddress", "start")
                .getResultList());

        Iterator itr = results.iterator();
        Office so = (Office) itr.next();

        while (true) {
            String[] ts = new String[5];
            ts[0] = so.getOfficeAddress();
            ts[1] = so.getNextOfficeDir();
            ts[2] = so.getNextOfficeDist();
            ts[3] = so.getPreOfficeDir();
            ts[4] = so.getPreOfficeDist();
            r.add(ts);

            if (so.getNextOffice().getOfficeAddress().equals("start")) {
                break;
            }
            so = so.getNextOffice();
        }

        return r;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import ads.resources.data.Office;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.Persistance;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class FloorMap {
    public static int calculateNumOfHops(Office origin, Office destination) {
        return giveDistanceFromCurrentToDestination(origin, destination);
    }
    public static int getMaxNumOfHops() {
        return giveLengthBtwStartEnd();
    }

    public static int getIdPoint0() {
        //todo
        return 1;
    }

    public static int giveDistanceFromCurrentToDestination(Office current, Office dest)
    {
        int totalDist=0;
        Office temp=current;
        while(!temp.equals(dest))
        {
            System.out.println("    partial totalDist = " +totalDist + "  currentOffice = "+temp.getOfficeAddress());
            totalDist += Math.abs(Integer.parseInt(temp.getNextOfficeDist()));
            temp=temp.getNextOffice();
        }
        System.out.println("totalDist = " +totalDist + "  currentOffice = "+temp.getOfficeAddress());
        return totalDist;
    }


    public static int giveLengthBtwStartEnd()
    {
        Iterator itr;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet<>(20);
        //find the office with start node
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", "start")
                            .getResultList());
        
        itr=results.iterator();
        Office startN=(Office) itr.next();
        results.clear();
        //find the office with end node
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", "end")
                            .getResultList());
        itr=results.iterator();
        Office endN=(Office) itr.next();
        return giveDistanceFromCurrentToDestination(startN, endN);
    }
}

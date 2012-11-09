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
public class FloorMap {
    private static Logger logger = Logger.getLogger(FloorMap.class.getName());

    public static int calculateNumOfHops(Office origin, Office destination) {
        return giveDistanceFromCurrentToDestination(origin, destination);
    }
    public static int getMaxNumOfHops() {
        return giveLengthBtwStartEnd();
    }

    public static Office getStartPoint() {
        EntityManager em = Persistance.getEntityManager();
        Office o = (Office) em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", "start")
                            .getSingleResult();
        return o;
    }

    public static int giveDistanceFromCurrentToDestination(Office current, Office dest)
    {
        int totalDist=0;
        Office temp=current;
        while(!temp.equals(dest))
        {
            logger.finest("    partial totalDist = " +totalDist + "  currentOffice = "+temp.getOfficeAddress());
            totalDist += Math.abs(Integer.parseInt(temp.getNextOfficeDist()));
            temp=temp.getNextOffice();
        }
        logger.finest("totalDist = " +totalDist + "  currentOffice = "+temp.getOfficeAddress());
        return totalDist;
    }


    public static int giveLengthBtwStartEnd()
    {
        Iterator itr;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
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
    
    public static void createOffice(Office office) {
        //persist the office entity to the database
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        em.persist(office);  
        em.getTransaction().commit();
    }
    
    
    public static String clearOffices() {
        String rresult;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        try {
             results.addAll(em.createNamedQuery("Office.findAll").getResultList());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        //First check whether there is already a created map or not in the database
        if(results.size()!=0)
            rresult="preCreatedMapDeleted";
        else
            rresult="noPreCreatedMap";
        em.getTransaction().begin();
        Iterator itr=results.iterator();
        while(itr.hasNext())
        {
            Office o=(Office) itr.next();
            //Office o2=em.find(Office.class, o.getOfficeAddress());
            em.remove(o);
        }
        em.getTransaction().commit();
//        Office o=em.find(Office.class, "101");
//        em.getTransaction().begin();
//        em.remove(o);
//        em.getTransaction().commit();
        return rresult;
    }

    public static void createLinksBtwOffices()
    {
        //Dummy vrs
        String preOfficeAddress, nextOfficeAddress;
        Set<Office> result = new HashSet(20);
        
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        try {
             results.addAll(em.createNamedQuery("Office.findAll").getResultList());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        for(Office o : results) {
            //Find and link the offices
            //Link preOffice
            preOfficeAddress=o.getPreOfficeAddress();
            
            if(preOfficeAddress!=null)//if the office is start this is the case
            {
                result.clear();
                result.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                                  .setParameter("officeAddress", preOfficeAddress)
                                  .getResultList()); //Assuming only one office with this name, this is assured in the map creation process
                if(result.size()!=1) {
//                    System.out.println("Some problem at finding pre office:"+preOfficeAddress+" result_size: "+result.size());
//                    for(Office oy : result) {
//                        System.out.println(oy.getOfficeAddress());
//                    }
                    throw new RuntimeException();
                }
                else
                {
                    Iterator itr=result.iterator();
                    o.setPreOffice((Office) itr.next());
                }
            }
            //Link nextOffice
            nextOfficeAddress=o.getNextOfficeAddress();
            if(nextOfficeAddress!=null)//if the office is end this is the case
            {
                result.clear();
                result.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", nextOfficeAddress)
                            .getResultList()); //Assuming only one office with this name, this is assured in the map creation process
            
                if(result.size()!=1) {
//                    System.out.println("Some problem at finding next office: "+nextOfficeAddress+"result_size: "+result.size());
//                    for(Office oy : result) {
//                        System.out.println(oy.getOfficeAddress());
//                    }
                    throw new RuntimeException();
                }
                else
                {
                    Iterator itr=result.iterator();
                    o.setNextOffice((Office) itr.next());
                }
            }
        }
        //goFromStartToEnd();
    }

    public static ArrayList<String[]> getMapDrawingArray() {
        ArrayList<String[]> r=new ArrayList();
        
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet(20);
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", "start")
                            .getResultList());
        
        Iterator itr=results.iterator();
        Office so=(Office) itr.next();
        
        while(true){
            String[] ts=new String[5];
            ts[0]=so.getOfficeAddress();
            ts[1]=so.getNextOfficeDir();
            ts[2]=so.getNextOfficeDist();
            ts[3]=so.getPreOfficeDir();
            ts[4]=so.getPreOfficeDist();
            r.add(ts);
            
            if(so.getOfficeAddress().equals("end")) {
                break;
            }
            so=so.getNextOffice();
        }

        return r;
    }

}

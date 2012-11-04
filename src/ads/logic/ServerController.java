/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.presentation.AdminCreateFloorMapView;
import ads.resources.data.ADSUser;
import ads.resources.data.Office;
import ads.resources.data.Persistance;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class ServerController implements ServerControllerInterface {
    private static ServerController singleton = new ServerController();
    private static final int userNotCorrect=0;
    private static final int userCorrect_Admin=1;
    private static final int userCorrect_NotAdmin=2;
    private DeliveryCoordinator delCoordinator;

    public static ServerController getInstance() {
        return singleton;
    }
    private Office Office;

    private ServerController() {
        Persistance.initPersistance();
        insertTestingDataSet();
        delCoordinator = DeliveryCoordinator.getInstance();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initRMI();
    }

    private void insertTestingDataSet() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        Office o = new Office("601");
        em.persist(o);
        o = new Office("602");
        em.persist(o);
        o = new Office("603");
        em.persist(o);
        o = new Office("604");
        em.persist(o);
        o = new Office("605");
        em.persist(o);
        o = new Office("606");
        em.persist(o);
        o = new Office("admin");
        em.persist(o);
        ADSUser u = new ADSUser("Admin", "istrator", null, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        //Add some additional users for test purposes
        o = new Office("101");
        em.persist(o);
        u = new ADSUser("mehmet", "aktas", null, "mfa@gmail.com", "mfa", "1111");
        em.persist(u);
        //
        o = new Office("102");
        em.persist(o);
        u = new ADSUser("ali", "veli", null, "aliveli@hotmail.com", "aliveli", "2222");
        em.persist(u);
        
        em.getTransaction().commit();
    }

    private static void initRMI() {
        try {
            ServerController obj = ServerController.getInstance();
            ServerControllerInterface stub = (ServerControllerInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("ServerControllerInterface");
            } catch (NotBoundException ex) {}

            registry.bind("ServerControllerInterface", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private void deinitRMI() {
        try {
            // Unbind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind("ServerControllerInterface");
        } catch (NotBoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stopServer(String username, String password) {
        deinitRMI();
        Persistance.deinitPersistance();
        System.exit(0);
    }

    @Override
    public int checkLogin(String username, String password) {
        EntityManager em = Persistance.getEntityManager();
        try {
            ADSUser u = em.find(ADSUser.class, username);
            // Note that the find in the database is case insensitive. Then, we
            // can't worry only about password, but we need to check also the username
            if (u == null || !u.getUsername().equals(username) || !u.getPassword().equals(password))
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
    
    @Override
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) {
        EntityManager em = Persistance.getEntityManager();
        Office office = new Office(roomNumber);
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

    @Override
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) {
        EntityManager em = Persistance.getEntityManager();

        if(this.checkLogin(username, password)==userNotCorrect) {
            return null;
        }
        
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

    @Override
    public void bookDelivery(String username, String password, String urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException{
        if(this.checkLogin(username, password)==userNotCorrect) {
            return;
        }

        delCoordinator.bookDelivery(urgency, targetListUsernames, username);
    }

    @Override
    public void officeCreated(Office office) {
        //persist the office entity to the database
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        em.persist(office);  
        em.getTransaction().commit();
    }

    @Override
    public String clearOffices() {
        String rresult;
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet<>(20);
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
        Iterator itr=results.iterator();
        while(itr.hasNext())
        {
            Office o=(Office) itr.next();
            //Office o2=em.find(Office.class, o.getOfficeAddress());
            em.getTransaction().begin();
            em.remove(o);
            em.getTransaction().commit();
        }
//        Office o=em.find(Office.class, "101");
//        em.getTransaction().begin();
//        em.remove(o);
//        em.getTransaction().commit();
        return rresult;
    }
    @Override
    public void createLinksBtwOffices()
    {
        //Dummy vrs
        String preOfficeAddress, nextOfficeAddress;
        Set<Office> result = new HashSet<>(20);
        
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet<>(20);
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
    public void goFromStartToEnd()
    {
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet<>(20);
        results.addAll(em.createNamedQuery("Office.findByOfficeAdress")
                            .setParameter("officeAddress", "start")
                            .getResultList());
        
        Iterator itr=results.iterator();
        Office so=(Office) itr.next();
        System.out.println(so.getOfficeAddress());
        so =so.getNextOffice();
        System.out.println(so.getOfficeAddress());
        so =so.getNextOffice();
        System.out.println(so.getOfficeAddress());
    }

    @Override
    public ArrayList<String[]> getMapDrawingArray() throws RemoteException {
        ArrayList<String[]> r=new ArrayList<>();
        
        EntityManager em = Persistance.getEntityManager();
        Set<Office> results = new HashSet<>(20);
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

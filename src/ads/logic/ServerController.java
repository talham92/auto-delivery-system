/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.communication.ServerCommunicator;
import ads.presentation.AdminCreateFloorMapView;
import ads.resources.data.ADSUser;
import ads.resources.data.Box;
import ads.resources.data.Delivery;
import ads.resources.data.DeliveryStep;
import ads.resources.data.Office;
import ads.resources.data.RobotPosition;
import ads.resources.datacontroller.DeliveryHistory;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.RobotPositionAccessor;
import ads.resources.datacontroller.UserController;
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
    private DeliveryCoordinator delCoordinator;

    private static final int STATE_SYSTEM_NON_INITIALIZED = 0;
    private static final int STATE_SYSTEM_INITIALIZED = 1;
    private int state;

    private void checkSystemInitialized() throws ServerNonInitializedException {
        if(state != STATE_SYSTEM_INITIALIZED) {
            throw new ServerNonInitializedException("Server is not initialized!");
        }
    }
    
    private void checkSystemNonInitialized() throws ServerInitializedException {
        if(state != STATE_SYSTEM_NON_INITIALIZED) {
            throw new ServerInitializedException("Server is initialized!");
        }
    }

    //todo: eliminate everything that calls entitymanager and move the code to datacontroller package
    public static ServerController getInstance() {
        return singleton;
    }

    private ServerController() {
        state = STATE_SYSTEM_NON_INITIALIZED;
        Persistance.initPersistance();
        addAdmin();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initRMI();
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
    public void initializeWithTestingData() throws ServerInitializedException {
        checkSystemNonInitialized();
        Persistance.deleteAllPersistanceRecords();
        insertTestingDataSet();
        ServerCommunicator.init();
        RobotPositionAccessor.init();
        delCoordinator = DeliveryCoordinator.getInstance();
        state = STATE_SYSTEM_INITIALIZED;
    }
    
    @Override
    public void initializeSystem() throws ServerInitializedException {
        checkSystemNonInitialized();
        // todo: check that we have offices
        ServerCommunicator.init();
        RobotPositionAccessor.init();
        delCoordinator = DeliveryCoordinator.getInstance();
        state = STATE_SYSTEM_INITIALIZED;
    }
    
    private void insertTestingDataSet() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        // String preOfficeDir, String preOfficeDist, String nextOfficeDir, String nextOfficeDist
        // String officeAddress, String preOfficeDir, String preOfficeDist, String nextOfficeDir, String nextOfficeDist, Office nextOffice, Office preOffice
        Office o1 = new Office("start", "y", "-12", "x", "10", (Office)null, null);
        Office o2 = new Office("602", "x", "10", "y", "7", null, o1);
        Office o3 = new Office("603", "y", "7", "y", "5", null, o2);
        Office o4 = new Office("604", "y", "5", "x", "-10", null, o3);
        Office o5 = new Office("end", "x", "-10", "y", "-12", null, o4);
        o1.setPreviousOffice(o5);
        o1.setNextOffice(o2);
        o2.setNextOffice(o3);
        o3.setNextOffice(o4);
        o4.setNextOffice(o5);
        o5.setNextOffice(o1);

        em.persist(o1);
        em.persist(o2);
        em.persist(o3);
        em.persist(o4);
        em.persist(o5);
        
        ADSUser u = new ADSUser("Admin", "istrator", o1, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        //Add some additional users for test purposes
        u = new ADSUser("mehmet", "aktas", o2, "mfa@gmail.com", "mfa", "1111");
        em.persist(u);
        u = new ADSUser("marc", "gamell", o5, "marcgamell@gmail.com", "mgamell", "a");
        em.persist(u);
        u = new ADSUser("ali", "veli", o4, "aliveli@hotmail.com", "aliveli", "2222");
        em.persist(u);
        
        Box b;
        b = new Box();
        em.persist(b);
        b = new Box();
        em.persist(b);
        b = new Box();
        em.persist(b);
        
        em.getTransaction().commit();
    }

    @Override
    public void stopServer(String username, String password) {
        deinitRMI();
        Persistance.deinitPersistance();
        System.exit(0);
    }

    @Override
    public int checkLogin(String username, String password) throws ServerNonInitializedException  {
        int login = UserController.checkLogin(username, password);
        if(login != UserController.userCorrect_Admin)
            checkSystemInitialized();
        return login;
    }
    
    @Override
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws ServerNonInitializedException  {
        checkSystemInitialized();
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

    @Override
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws ServerNonInitializedException  {
        checkSystemInitialized();
        EntityManager em = Persistance.getEntityManager();

        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
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
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException, ServerNonInitializedException {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return;
        }

        delCoordinator.bookDelivery(urgency, targetListUsernames, username);
    }

    @Override
    public SystemStatus getSystemStatus(String username, String password) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)!=UserController.userCorrect_Admin) {
            throw new RemoteException("You are not the administrator!");
        }
        return new SystemStatus(RobotPositionAccessor.getRobotPosition().getOfficeAddress(), RobotPositionAccessor.isMoving());
    }
    
    @Override
    public void officeCreated(Office office) throws ServerInitializedException  {
        checkSystemNonInitialized();
        //persist the office entity to the database
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        em.persist(office);  
        em.getTransaction().commit();
    }

    @Override
    public List<Delivery> getUserDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            throw new RemoteException("You don't have enough permission!");
        }
        ADSUser sender = UserController.findUser(username);
        return DeliveryHistory.getUserDeliveryList(sender);
    }
    
    @Override
    public String clearOffices() throws ServerInitializedException  {
        checkSystemNonInitialized();
        //todo: check if there are users... if true, throw exception... if false, clearOffices
        //todo: put the following function as reachable by the user
        //todo: check admin login in all admin functions
        UserController.removeUsers();
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
    @Override
    public void createLinksBtwOffices() throws ServerInitializedException 
    {
        checkSystemNonInitialized();
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

    @Override
    public List<String[]> getUserDeliveryDetails(String username, String password, int deliveryId) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            throw new RemoteException("You don't have enough permission!");
        }
        ADSUser sender = UserController.findUser(username);
        Delivery delivery = DeliveryHistory.getDelivery(deliveryId);
        if(!delivery.getSender().equals(sender)) {
            throw new RemoteException("You don't have enough permission!");
        }
        List<DeliveryStep> deliveryStates = delivery.getStateList();
        List<String[]> r = new ArrayList(deliveryStates.size());
        for (DeliveryStep ds : deliveryStates) {
             r.add(new String[]{ds.getTimeCreation().toString(), ds.getClass().getSimpleName()});
        }
        
        return r;
    }
    
    @Override
    public ArrayList<String[]> getMapDrawingArray() throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
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

    private void addAdmin() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("Admin", "istrator", null, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        em.getTransaction().commit();
    }

}

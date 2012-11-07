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
import ads.resources.datacontroller.FloorMap;
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
        UserController.addAdmin();
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
        return UserController.register(firstName, lastName, roomNumber, email, username, password, password1);
    }
//todo: check the system is working if the server is REMOTE from the client
//todo: autocomplete when searching!!!
    @Override
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return null;
        }
        return UserController.searchUser_NameOffice(name, office);
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
        if(this.checkLogin(username, password)!=UserController.userCorrect_Admin) {
            throw new RemoteException("You are not the administrator!");
        }
        
        if(state == ServerController.STATE_SYSTEM_INITIALIZED) {
            Office position = RobotPositionAccessor.getRobotPosition();
            Set<ADSUser> users = UserController.searchUser_NameOffice(null, position.getOfficeAddress());
            String names = "";
            for(ADSUser u : users) {
                names += u.getFirstName() + " " + u.getLastName() + ", ";
            }
            if(!names.isEmpty()) {
                names = names.substring(0, names.length()-2);
            }
            return new SystemStatus(position.getOfficeAddress(), RobotPositionAccessor.isMoving(), true, names);
        } else if(state == ServerController.STATE_SYSTEM_NON_INITIALIZED) {
            return new SystemStatus("", false, false, "");
        } else {
            throw new RemoteException("server state is different than STATE_SYSTEM_INITIALIZED and STATE_SYSTEM_NON_INITIALIZED");
        }

    }

    @Override
    public void createOffice(Office office) throws ServerInitializedException  {
        checkSystemNonInitialized();
        FloorMap.createOffice(office);
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
    public List<Delivery> getDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)!=UserController.userCorrect_Admin) {
            throw new RemoteException("You don't have enough permission!");
        }
        return DeliveryHistory.getDeliveryList();
    }
    
    @Override
    public String clearOffices() throws ServerInitializedException  {
        checkSystemNonInitialized();
        //todo: check if there are users... if true, throw exception... if false, clearOffices
        //todo: put the following function as reachable by the user
        //todo: check admin login in all admin functions
        UserController.removeUsers();
        return FloorMap.clearOffices();
    }
    
    @Override
    public void createLinksBtwOffices() throws ServerInitializedException 
    {
        checkSystemNonInitialized();
        FloorMap.createLinksBtwOffices();
    }

    @Override
    public List<String[]> getDeliveryDetails(String username, String password, int deliveryId) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        int login = this.checkLogin(username, password);
        if(login==UserController.userNotCorrect) {
            throw new RemoteException("You don't have enough permission!");
        }
        ADSUser sender = UserController.findUser(username);
        Delivery delivery = DeliveryHistory.getDelivery(deliveryId);
        if(!delivery.getSender().equals(sender) && login!=UserController.userCorrect_Admin) {
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
    public ArrayList<String[]> getMapDrawingArray() throws RemoteException {
        //checkSystemInitialized();
        return FloorMap.getMapDrawingArray();
    }

}

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

//todo: fix imports in the whole project
//todo: check the system is working if the server is REMOTE from the client
//todo: autocomplete when searching!!!

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

    public static ServerController getInstance() {
        return singleton;
    }

    private ServerController() {
        state = STATE_SYSTEM_NON_INITIALIZED;
        Persistance.initPersistance();
        UserController.addAdmin();
        ServerCommunicator.init();
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
            Registry registry = LocateRegistry.getRegistry("spring.rutgers.edu");
            try {
                registry.unbind("ServerControllerInterface");
            } catch (Exception ex) {}

            try {
                registry.bind("ServerControllerInterface", stub);
            } catch (Exception ex) {
                // check local registry
                registry = LocateRegistry.getRegistry();
                try {
                    registry.unbind("ServerControllerInterface");
                } catch (NotBoundException ex1) {}
                registry.bind("ServerControllerInterface", stub);
            }
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
        insertBigTestingDataSet();
        RobotPositionAccessor.init();
        delCoordinator = DeliveryCoordinator.getInstance();
        state = STATE_SYSTEM_INITIALIZED;
    }
    
    @Override
    public void initializeSystem() throws ServerInitializedException {
        checkSystemNonInitialized();
        // todo: check that we have offices
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

    private void insertBigTestingDataSet() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        // Creating map datas
        Office os = new Office("start", "y", "-12", "x", "10", (Office)null, null);
        Office o1 = new Office("602", "x", "10", "x", "7", null, os);
        Office o2 = new Office("603", "x", "7", "x", "15", null, o1);
        Office o3 = new Office("604", "x", "5", "x", "10", null, o2);
        Office o4 = new Office("605", "x", "10", "y", "5", null, o3);
        Office o5 = new Office("606", "y", "5", "y", "12", null, o4);
        Office o6 = new Office("607", "y", "12", "x", "-10", null, o5);
        Office o7 = new Office("608", "x", "-10", "x", "-7", null, o6);
        Office o8 = new Office("609", "x", "-7", "x", "-15", null, o7);
        Office o9 = new Office("610", "x", "-15", "x", "-10", null, o8);
        Office o10 = new Office("611", "x", "-7", "x", "-15", null, o9);
        Office oe = new Office("end", "x", "-15", "y", "-17", null, o10);
        os.setPreviousOffice(oe);
        os.setNextOffice(o1);
        o1.setNextOffice(o2);
        o2.setNextOffice(o3);
        o3.setNextOffice(o4);
        o4.setNextOffice(o5);
        o5.setNextOffice(o6);
        o6.setNextOffice(o7);
        o7.setNextOffice(o8);
        o8.setNextOffice(o9);
        o9.setNextOffice(o10);
        o10.setNextOffice(oe);
        oe.setNextOffice(os);

        em.persist(os);
        em.persist(o1);
        em.persist(o2);
        em.persist(o3);
        em.persist(o4);
        em.persist(o5);
        em.persist(o6);
        em.persist(o7);
        em.persist(o8);
        em.persist(o9);
        em.persist(o10);
        em.persist(oe);
        // Creating the users
        ADSUser u = new ADSUser("Admin", "istrator", o1, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        //Add some additional users for test purposes
        // Users for o1: 602
        u = new ADSUser("yi", "zhao", o1, "yz336@rutgers.edu", "yz", "1111");
        em.persist(u);
        u = new ADSUser("yanze", "zhang", o1, "yanze.zhang@rutgers.edu", "yanzez", "1111");
        em.persist(u);
        // Users for o2: 603
        u = new ADSUser("daihou", "wang", o2, "daihou.wang@rutgers.edu", "dw", "1111");
        em.persist(u);
        u = new ADSUser("anu", "tom", o2, "anuliz.tom@rutgers.edu", "at", "1111");
        em.persist(u);
        u = new ADSUser("siddhesh", "surve", o2, "siddhesh.surve@rutgers.edu", "ss", "1111");
        em.persist(u);
        // Users for o3: 604
        u = new ADSUser("jagbir", "singh", o3, "jagbir.singh@rutgers.edu", "js", "1111");
        em.persist(u);
        u = new ADSUser("swapnil", "sarode", o3, "swapnil.sarode@rutgers.edu", "swapnils", "1111");
        em.persist(u);
        // Users for o4: 605
        u = new ADSUser("brien", "range", o4, "brien.range@rutgers.edu", "br", "1111");
        em.persist(u);
        u = new ADSUser("vinayak", "pothineni", o4, "vinayak.pothineni@rutgers.edu", "vp", "1111");
        em.persist(u);
        u = new ADSUser("prasoon", "mishra", o4, "prasoon.mishra@rutgers.edu", "pp", "1111");
        em.persist(u);
        u = new ADSUser("zhongzhou", "li", o4, "vinayak.pothineni@rutgers.edu", "zv", "1111");
        em.persist(u);
        // Users for o5: 606
        u = new ADSUser("ivan", "marsic", o5, "marsic@ece.rutgers.edu", "ivanmarsic", "1111");
        em.persist(u);
        // Users for o6: 607
        u = new ADSUser("kevin", "kobilinski", o6, "kobi@eden.rutgers.edu", "kk", "1111");
        em.persist(u);
        u = new ADSUser("abdul", "abdul", o6, "abdul@eden.rutgers.edu", "ah", "1111");
        em.persist(u);
        u = new ADSUser("chao", "han", o6, "ch577@eden.rutgers.edu", "hc", "1111");
        em.persist(u);
        u = new ADSUser("yao", "ge", o6, "yaoge@eden.rutgers.edu", "yg", "1111");
        em.persist(u);
        // Users for o7: 608
        u = new ADSUser("li", "liu", o7, "li.liu4016@rutgers.edu", "ll", "1111");
        em.persist(u);
        // Users for o8: 609
        u = new ADSUser("junwei", "zhao", o8, "junwei.zhao@rutgers.edu", "jz", "1111");
        em.persist(u);
        u = new ADSUser("xu", "bingbing", o8, "bingbing.xu@rutgers.edu", "bz", "1111");
        em.persist(u);
        // Users for o9: 610
        u = new ADSUser("anusha", "vutukuri", o9, "anusha.vutukuri@rutgers.edu", "av", "1111");
        em.persist(u);
        u = new ADSUser("mehmet", "aktas", o9, "mfatihaktas@gmail.com", "mfa", "1111");
        em.persist(u);        
        // Users for o10: 611
        u = new ADSUser("marc", "gamell", o10, "marc.gamell@rutgers.edu", "mgamell", "a");
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

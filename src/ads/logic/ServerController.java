
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
import com.sun.org.apache.xalan.internal.xsltc.compiler.FlowList;
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
 * ServerController controls the server operations and it implements
 * the ServerControllerInterface.
 * 
 */
public class ServerController implements ServerControllerInterface {
    // Define a private SeverController object singleton
    private static ServerController singleton = new ServerController();
    // Define a DeliveryCoordinator delCoordinator
    private DeliveryCoordinator delCoordinator;
    // Define default int STATE_SYSTEM_NON_INITIALIZED is 0, 
    //STATE_SYSTEM_INITIALIZED is 1
    private static final int STATE_SYSTEM_NON_INITIALIZED = 0; 
    private static final int STATE_SYSTEM_INITIALIZED = 1;
    private int state;
    private static final Logger logger = Logger.getLogger(ServerController.class.getName());
    
/**
 *  Check whether the system is initialized.
 * @throws ServerNonInitializedException  If state is non initialized
 */
    private void checkSystemInitialized() throws ServerNonInitializedException {
        if(state != STATE_SYSTEM_INITIALIZED) {
            throw new ServerNonInitializedException("Server is not initialized!");
        }
    }
    /**
     * Check whether the system is non-initialized
     * @throws ServerInitializedException    If state is initialized
     */
    private void checkSystemNonInitialized() throws ServerInitializedException {
        if(state != STATE_SYSTEM_NON_INITIALIZED) {
            throw new ServerInitializedException("Server is initialized!");
        }
    }
/**
 * Get an object of class ServerController
 * @return Singleton, which is defined as an object of ServerController
 */
    public static ServerController getInstance() {
        return singleton;
    }
/**
 * ServerController constructor.
 * Set state is non initialized; initialize the Persistance; Add Admin to 
 * the database; and initialize the ServerCommunicator.
 * 
 * @see Persistance#initPersistance() 
 * @see UserController#addAdmin() 
 * @see ServerCommunicator#init() 
 */
    private ServerController() {
        state = STATE_SYSTEM_NON_INITIALIZED;
        Persistance.initPersistance();
        UserController.addAdmin();
        ServerCommunicator.init();
    }
    
 /**
  * Main function initialize RMI
  * @see ServerController#initRMI() 
  */
    public static void main(String[] args) {
        initRMI();
    }
    
    /**
     * Initialize RMI.
     * Export the ServerController object so that it can receive invocations of 
     * its remote methods from remote clients,then bind the remote object's
     * stub in the registry. 
     */

    private static void initRMI() {
        try {
            // Define a ServerController object
            ServerController obj = ServerController.getInstance();
            // UnicastRemoteObject.exportObject method exports the supplied 
            //remote object so that it can receive invocations of its remote 
            //methods from remote clients
            ServerControllerInterface stub = (ServerControllerInterface) UnicastRemoteObject.exportObject(obj, 0);
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(); 
            try {
                registry.unbind("ServerControllerInterface");
            } catch (Exception ex) {}
            // Binds a remote reference to the "ServerControllerInterface" in
           // this registry
            registry.bind("ServerControllerInterface", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }
    
    /**
     * Deinitialize RMI.
     * 
     * Removes the binding for the specified ServerControllerInterface in
     * the registry. Catch NotBoundException if ServerControllerInterface
     * is not currently bound. Catch AccessException if this registry is local
     * and it denies the caller access to perform this operation. Catch 
     * RemoteException if remote communication with the
     * registry failed.
    */
    private void deinitRMI() {
        try {
            // Unbind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind("ServerControllerInterface");
        }
       // Catch NotBoundException if ServerControllerInterface is not currently bound
        catch (NotBoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        //Catch AccessException  if this registry is local and 
       //it denies the caller access to perform this operation.
        catch (AccessException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        // Catch RemoteException if remote communication with the
        // registry failed
        catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/**
 * Initialize with testing data.
 * First, check whether the system is initialized or not, then delete all 
 * persistance records, insert the testing data, and initialize the
 * RobotPositionAccessor, and get a DeliveryCoordinator object, and define the 
 * state is 1.
 * @see ServerController#checkSystemInitialized() 
 * @see Persistance#deleteAllPersistanceRecords() 
 * @see ServerController#insertBigTestingDataSet() 
 * @throws ServerInitializedException  If the Server is not initialized successfully
 */
    @Override
    public void initializeWithTestingData() throws ServerInitializedException {
        checkSystemNonInitialized();
        Persistance.deleteAllPersistanceRecords();
        insertBigTestingDataSet();
        RobotPositionAccessor.init();
        delCoordinator = DeliveryCoordinator.getInstance();
        state = STATE_SYSTEM_INITIALIZED;
    }
    
    /**
     * Initialize the system.
     * First check weather the system is initialized, initialize the
     * RobotPositionAccessor,get a DeliveryCoordinator object, and define the 
     * state is 1.
     * @throws ServerInitializedException 
     */
    @Override
    public void initializeSystem() throws ServerInitializedException {
        checkSystemNonInitialized();
        // todo: check that we have offices
        RobotPositionAccessor.init();
        delCoordinator = DeliveryCoordinator.getInstance();
        state = STATE_SYSTEM_INITIALIZED;
    }
    /**
     * Insert Testing data set.
     * Create some Office, ADSUser and Box objects, and put them in database.
     *
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

        em.merge(o1);
        em.merge(o2);
        em.merge(o3);
        em.merge(o4);
        em.merge(o5);
        
        ADSUser u = new ADSUser("Admin", "istrator", o1, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.merge(u);
        //Add some additional users for test purposes
        u = new ADSUser("mehmet", "aktas", o2, "mfa@gmail.com", "mfa", "1111");
        em.merge(u);
        u = new ADSUser("marc", "gamell", o5, "marcgamell@gmail.com", "mgamell", "a");
        em.merge(u);
        u = new ADSUser("ali", "veli", o4, "aliveli@hotmail.com", "aliveli", "2222");
        em.merge(u);
        // Add some box for test purposes
        Box b;
        b = new Box();
        em.merge(b);
        b = new Box();
        em.merge(b);
        b = new Box();
        em.merge(b);
        
        em.getTransaction().commit();
    }
/**
 * Insert testing data.
 * Create some Office, ADSUser and Box objects, and put them in database.
 */
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

        em.merge(os);
        em.merge(o1);
        em.merge(o2);
        em.merge(o3);
        em.merge(o4);
        em.merge(o5);
        em.merge(o6);
        em.merge(o7);
        em.merge(o8);
        em.merge(o9);
        em.merge(o10);
        em.merge(oe);
        // Creating the users
        ADSUser u = new ADSUser("Admin", "istrator", o1, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.merge(u);
        //Add some additional users for test purposes
        // Users for o1: 602
        u = new ADSUser("yi", "zhao", o1, "yz336@rutgers.edu", "yz", "1111");
        em.merge(u);
        u = new ADSUser("yanze", "zhang", o1, "yanze.zhang@rutgers.edu", "yanzez", "1111");
        em.merge(u);
        // Users for o2: 603
        u = new ADSUser("daihou", "wang", o2, "daihou.wang@rutgers.edu", "dw", "1111");
        em.merge(u);
        u = new ADSUser("anu", "tom", o2, "anuliz.tom@rutgers.edu", "at", "1111");
        em.merge(u);
        u = new ADSUser("siddhesh", "surve", o2, "siddhesh.surve@rutgers.edu", "ss", "1111");
        em.merge(u);
        // Users for o3: 604
        u = new ADSUser("jagbir", "singh", o3, "jagbir.singh@rutgers.edu", "js", "1111");
        em.merge(u);
        u = new ADSUser("swapnil", "sarode", o3, "swapnil.sarode@rutgers.edu", "swapnils", "1111");
        em.merge(u);
        // Users for o4: 605
        u = new ADSUser("brien", "range", o4, "brien.range@rutgers.edu", "br", "1111");
        em.merge(u);
        u = new ADSUser("vinayak", "pothineni", o4, "vinayak.pothineni@rutgers.edu", "vp", "1111");
        em.merge(u);
        u = new ADSUser("prasoon", "mishra", o4, "prasoon.mishra@rutgers.edu", "pp", "1111");
        em.merge(u);
        u = new ADSUser("zhongzhou", "li", o4, "vinayak.pothineni@rutgers.edu", "zv", "1111");
        em.merge(u);
        // Users for o5: 606
        u = new ADSUser("ivan", "marsic", o5, "marsic@ece.rutgers.edu", "ivanmarsic", "1111");
        em.merge(u);
        // Users for o6: 607
        u = new ADSUser("kevin", "kobilinski", o6, "kobi@eden.rutgers.edu", "kk", "1111");
        em.merge(u);
        u = new ADSUser("abdul", "abdul", o6, "abdul@eden.rutgers.edu", "ah", "1111");
        em.merge(u);
        u = new ADSUser("chao", "han", o6, "ch577@eden.rutgers.edu", "hc", "1111");
        em.merge(u);
        u = new ADSUser("yao", "ge", o6, "yaoge@eden.rutgers.edu", "yg", "1111");
        em.merge(u);
        // Users for o7: 608
        u = new ADSUser("li", "liu", o7, "li.liu4016@rutgers.edu", "ll", "1111");
        em.merge(u);
        // Users for o8: 609
        u = new ADSUser("junwei", "zhao", o8, "junwei.zhao@rutgers.edu", "jz", "1111");
        em.merge(u);
        u = new ADSUser("xu", "bingbing", o8, "bingbing.xu@rutgers.edu", "bz", "1111");
        em.merge(u);
        // Users for o9: 610
        u = new ADSUser("anusha", "vutukuri", o9, "anusha.vutukuri@rutgers.edu", "av", "1111");
        em.merge(u);
        u = new ADSUser("mehmet", "aktas", o9, "mfatihaktas@gmail.com", "mfa", "1111");
        em.merge(u);        
        // Users for o10: 611
        u = new ADSUser("marc", "gamell", o10, "marc.gamell@rutgers.edu", "mgamell", "a");
        em.merge(u);
        // Create some Box object fot test purposes
        Box b;
        b = new Box();
        em.merge(b);
        b = new Box();
        em.merge(b);
        b = new Box();
        em.merge(b);
        
        em.getTransaction().commit();
    }
    /**
     * Stop Server.
     * Deiniitilze the RMI and Persistance, exit the system.
     * 
     * @param username The String of username
     * @param password The String of password
     * 
     * @see ServerController#deinitRMI() 
     * @see Persistance#deinitPersistance() 
     * @see System#exit() 
     */
    @Override
    public void stopServer(String username, String password) {
        deinitRMI();
        Persistance.deinitPersistance();
        System.exit(0);
    }
/**
 * Check whether the Admin login.
 * 
 * @param username The String of username
 * @param password The String of password 
 * @return int login, which is user-defined.
 * @throws ServerNonInitializedException  If the Server is not initialized
 * 
 * @see UserController#checkLogin(String,String) 
 */
    @Override
    public int checkLogin(String username, String password) throws ServerNonInitializedException  {
        // Check the login 
        int login = UserController.checkLogin(username, password);
        // If t is not correct Admin login, check wether the sytem is initialized.
        if(login != UserController.userCorrect_Admin)
            checkSystemInitialized();
        return login;
    }
    /**
     * Helps with the user's register.
     * Check the validity of email format, username, office, password format
     * when user registers
     * 
     * @param firstName  The first name of user
     * @param lastName   The last name of user
     * @param roomNumber The room number
     * @param email      The email String
     * @param username   User's username
     * @param password   String of password
     * @param password1  The repeated password String
     * @return           String showing the scenario when user registers
     * @throws ServerNonInitializedException  If the Server is not initialized
     */
    @Override
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws ServerNonInitializedException  {
        checkSystemInitialized();
        return UserController.register(firstName, lastName, roomNumber, email, username, password, password1);
    }
    /**
     * Search user by name or office number.
     * @param username The String of username
     * @param password The String of password
     * @param name    The String of username
     * @param office  The String of office
     * @return   The searching results, which is an object of ADSUser
     * @throws ServerNonInitializedException  If the Server is not initialized
     * 
     * @see UserController#searchUser_NameOffice(String,String) 
     */
    @Override
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws ServerNonInitializedException  {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return null;
        }
        return UserController.searchUser_NameOffice(name, office);
    }
    /**
     * Book delivery.
     * @param username The String of username
     * @param password The String of password
     * @param urgency  The priority of delivery
     * @param targetListUsernames The list of receivers
     * @throws RemoteException  If execution of a remote method call
     * @throws NonBookedDeliveryException  If there is NonBookedDeliveryException
     * @throws ServerNonInitializedException If the server is non initialized
     * 
     *@see DeliveryCoordinator#bookDelivery(double,List,String) 
     */
    @Override
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException, ServerNonInitializedException {
        checkSystemInitialized();
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return;
        }

        delCoordinator.bookDelivery(urgency, targetListUsernames, username);
    }
/**
 * Get system status, including where is the robot and whether it is 
 * moving
 * @param username The String of username
 * @param password The String of password
 * @return The SystemStatus Object
 * @throws RemoteException If execution of a remote method call
 * @throws ServerNonInitializedException If the server is non initialized
 */
    @Override
    public SystemStatus getSystemStatus(String username, String password) throws RemoteException, ServerNonInitializedException  {
        // check whether it is the Admin
        if(this.checkLogin(username, password)!=UserController.userCorrect_Admin) {
            throw new RemoteException("You are not the administrator!");
        }
        
        if(state == ServerController.STATE_SYSTEM_INITIALIZED) {
            // Create Office object position
            Office position = RobotPositionAccessor.getRobotPosition();
            // Set ADSUser
            Set<ADSUser> users = UserController.searchUser_NameOffice(null, position.getOfficeAddress());
            String names = "";
            for(ADSUser u : users) {
                names += u.getFirstName() + " " + u.getLastName() + ", ";
            }
            if(!names.isEmpty()) {
                names = names.substring(0, names.length()-2);
            }
            // Get system status
            return new SystemStatus(position.getOfficeAddress(), RobotPositionAccessor.isMoving(), true, names);
        } else if(state == ServerController.STATE_SYSTEM_NON_INITIALIZED) {
            return new SystemStatus("", false, false, "");
        } else {
            throw new RemoteException("server state is different than STATE_SYSTEM_INITIALIZED and STATE_SYSTEM_NON_INITIALIZED");
        }

    }
/**
 * Create Office object to database
 * @param office The Office object
 * @throws ServerInitializedException If the server is initialized
 * 
 * @see FloorMap#createOffice(Office) 
 */
    @Override
    public void createOffice(Office office) throws ServerInitializedException  {
        checkSystemNonInitialized();
        FloorMap.createOffice(office);
    }
/**
 * Get User Delivery list
 * @param username The String of username
 * @param password The String of password
 * @return List of Delivery
 * @throws RemoteException If execution of a remote method call
 * @throws ServerNonInitializedException If the server is non initialized
 * 
 * @see DeliveryHistory#getUserDeliveryList(ads.resources.data.ADSUser) 
 */
    @Override
    public List<Delivery> getUserDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        // If user is not correct, then throw remote exception
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            throw new RemoteException("You don't have enough permission!");
        }
        ADSUser sender = UserController.findUser(username);
        // Get user delivery list
        return DeliveryHistory.getUserDeliveryList(sender);
    }
    /**
     * Get Delivery List
     * @param username The String of username
     * @param password The String of password
     * @return List of Delivery
     * @throws RemoteException If execution of a remote method call
     * @throws ServerNonInitializedException If the server is non initialized
     * 
     * @see DeliveryHistory#getDeliveryList() 
     */
    @Override
    public List<Delivery> getDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        // If user is not correct, then throw remote exception
        if(this.checkLogin(username, password)!=UserController.userCorrect_Admin) {
            throw new RemoteException("You don't have enough permission!");
        }
        // Get delivery list
        return DeliveryHistory.getDeliveryList();
    }
    /**
     * Clear Offices
     * @return String showing office is cleared
     * @throws ServerInitializedException  If the server is initialized 
     * 
     * @see FloorMap#clearOffices() 
     */
    @Override
    public String clearOffices() throws ServerInitializedException  {
        checkSystemNonInitialized();
        //todo: check if there are users... if true, throw exception... if false, clearOffices
        //todo: put the following function as reachable by the user
        //todo: check admin login in all admin functions
        UserController.removeUsers();
        return FloorMap.clearOffices();
    }
    /**
     * Create links between offices
     * @throws ServerInitializedException  If the server is initialized 
     * 
     * @see FloorMap#createLinksBtwOffices() 
     */
    @Override
    public void createLinksBtwOffices() throws ServerInitializedException 
    {
        logger.info("createLinksBtwOffices 1");
        checkSystemNonInitialized();
        logger.info("createLinksBtwOffices 2");
        FloorMap.createLinksBtwOffices();
        logger.info("createLinksBtwOffices 3");
    }
/**
 * Get the details of delivery
 * @param username The String of username
 * @param password The String of password
 * @param deliveryId The number of delivery ID
 * @return List of delivery details
 * @throws RemoteException If execution of a remote method call
 * @throws ServerNonInitializedException If Server is not initialized
 */
    @Override
    public List<String[]> getDeliveryDetails(String username, String password, int deliveryId) throws RemoteException, ServerNonInitializedException  {
        checkSystemInitialized();
        //Check login
        int login = this.checkLogin(username, password);
        // If login is not correct
        if(login==UserController.userNotCorrect) {
            throw new RemoteException("You don't have enough permission!");
        }
        // Create ADSUser and Delivery object
        ADSUser sender = UserController.findUser(username);
        Delivery delivery = DeliveryHistory.getDelivery(deliveryId);
        // If it is not admin
        if(!delivery.getSender().equals(sender) && login!=UserController.userCorrect_Admin) {
            throw new RemoteException("You don't have enough permission!");
        }
        List<DeliveryStep> deliveryStates = delivery.getStateList();
        List<String[]> r = new ArrayList(deliveryStates.size());
        for (DeliveryStep ds : deliveryStates) {
             r.add(new String[]{ds.getTimeCreation().toString(), ds.getClass().getSimpleName()});
        }
        // Return the list of delivery details
        return r;
    }
    /**
     * Get the drawing map array, knowing the position point in the map
     * @return The Arraylist showing the map
     * @throws RemoteException If execution of a remote method call
     * 
     * @see FloorMap#getMapDrawingArray() 
     */
    @Override
    public ArrayList<String[]> getMapDrawingArray() throws RemoteException {
        //checkSystemInitialized();
        return FloorMap.getMapDrawingArray();
    }

}

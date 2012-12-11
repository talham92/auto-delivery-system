/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IntegrationTest;

import ads.logic.DeliveryCoordinator;
import ads.logic.EmailChecker;
import ads.logic.NonBookedDeliveryException;
import ads.logic.ServerController;
import ads.logic.ServerControllerInterface;
import ads.logic.ServerInitializedException;
import ads.logic.ServerNonInitializedException;
import ads.logic.SystemStatus;
import ads.presentation.AdminCreateFloorMapView;
import ads.presentation.AdminMainView;
import ads.presentation.*;
import ads.resources.communication.ServerCommunicatorStub;
import ads.resources.data.ADSUser;
import ads.resources.data.BookedDelivery;
import ads.resources.data.Box;
import ads.resources.data.DeliveredDelivery;
import ads.resources.data.Delivery;
import ads.resources.data.DeliveryStep;
import ads.resources.data.Office;
import ads.resources.data.PickedUpDelivery;
import ads.resources.data.RobotPosition;
import ads.resources.datacontroller.DeliveryHistory;
import ads.resources.datacontroller.FloorMap;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.RobotPositionAccessor;
import ads.resources.datacontroller.SectionedBox;
import ads.resources.datacontroller.UserController;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Qiannan
 */
public class IntegrationTest {
    private Office o2;
    private static final int STATE_SYSTEM_INITIALIZED = 1;
    private int state;
    private Office o5;
    private Office o4;
    SystemStatus instance = new SystemStatus("Robot is in office 02", true, true,"User is in office 03");
    private ClientController client;
    private ServerController server;
   
 
    public void DeliveryCoordinatorTest() {
    }
 
    public void InsertTestingData(){
        Persistance.initPersistance();
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("m", "a", o2, "mfa@gm", "mfa", "1111");
        em.persist(u);
        em.getTransaction().commit();
    }
   
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Success Scenario Test of bookDelivery method, of class DeliveryCoordinator.
     */
    @Test
    public void testBookDelivery_Correct() throws Exception {
        System.out.println("bookDelivery_Correct");
        InsertTestingData();
        double priority = 0.0;
        List<String> targetListUsername = new ArrayList();
        targetListUsername.add("mfa1");
        targetListUsername.add("mfa2");
        String username = "mfa";
        String receiverUsername;
        receiverUsername = "mfa1";
        DeliveryCoordinator instance = DeliveryCoordinator.getInstance();
        instance.bookDelivery(priority, targetListUsername, username);
        Persistance.deinitPersistance();
        // TODO review the generated test code and remove the default call to fail.
    }
    
    
     @Test(expected = Exception.class)
    public void testBookDelivery_UserNotCorrect() throws Exception {
        System.out.println("bookDelivery_UserNotCorrect");
        InsertTestingData();
        double priority = 0.0;
        List<String> targetListUsername = new ArrayList();
        targetListUsername.add("mfa1");
        targetListUsername.add("mfa2");
        String username = "m";
        String receiverUsername;
        receiverUsername = "mfa1";
        DeliveryCoordinator instance = DeliveryCoordinator.getInstance();
        instance.bookDelivery(priority, targetListUsername, username);
        Persistance.deinitPersistance();
        // TODO review the generated test code and remove the default call to fail.
    }
    
    
        @Test(expected = NonBookedDeliveryException.class)
    public void testBookDelivery_PriorityNotCorrect() throws NonBookedDeliveryException {
        System.out.println("bookDelivery_PriorityNotCorrect");
        
        double priority = -1.0;
        List<String> targetListUsername = new ArrayList();
        targetListUsername.add("mfa1");
        targetListUsername.add("mfa2");
        String username = "mfa";
        String receiverUsername;
        receiverUsername = "mfa1";
        DeliveryCoordinator instance = DeliveryCoordinator.getInstance();
        instance.bookDelivery(priority, targetListUsername, username);
        Persistance.deinitPersistance();
        // TODO review the generated test code and remove the default call to fail.
    }
    
     
    /**
     * Test of getInstance method, of class DeliveryCoordinator.
     */
    @Test
    public void testGetInstance_DeliveryCoordinator() {
        System.out.println("getInstance_DeliveryCoordinator");
        DeliveryCoordinator expResult = DeliveryCoordinator.getInstance();
        DeliveryCoordinator result = DeliveryCoordinator.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    
     /**
     * Test of checkEmail method, of class EmailChecker.
     */
    @Test
    public void testCheckEmail_NotCorrect() {
        System.out.println("checkEmail_NotCorrect");
        String email = "123hotmail.com";
        boolean expResult = true;
        boolean result = EmailChecker.checkEmail(email);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    
    
    
    @Test
public void testCheckEmail_Correct(){
      System.out.println("checkEmail_Correct");
        String email = "123@hotmail.com";
        boolean expResult = true;
        boolean result = EmailChecker.checkEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.   
}
    
    /**
     * Test of getInstance method, of class ServerController.
     */
    
    @Test
    public void testGetInstance_ServerController() {
        System.out.println("getInstance_ServerController");
        ServerController expResult = ServerController.getInstance();
        ServerController result = ServerController.getInstance();
        assertEquals(expResult, result);
    }

    /**
     * Test of main method, of class ServerController.
     */
    @Test
    public void testMain() {
        System.out.println("main");
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
        }

    }

 
    /**
     * Test of checkLogin method, of class ServerController.
     */
    @Test
    public void testCheckLogin_UserCorrect_NotAdmin() throws Exception {
        System.out.println("checkLogin_UserCorrect_NotAdmin");
        Persistance.initPersistance();
       // EntityManager em = Persistance.getEntityManager();
        //em.getTransaction().begin();
        //ADSUser u = new ADSUser("m", "a", o2, "mfa@gm", "mfa", "1111");
        //em.persist(u);
        //em.getTransaction().commit();
        String username = "mfa";
        String password = "1111";
        int expResult = 2;
        int result = UserController.checkLogin(username, password);
        assertEquals(expResult, result);
      
    }
    
     @Test
    public void testCheckLogin_UserNotCorrect() throws Exception {
        System.out.println("checkLogin_UserNotCorrect");
        String username = "m";
        String password = "1111";
        int expResult = 0;
        int result = UserController.checkLogin(username, password);
        assertEquals(expResult, result);
      
    }
     
      @Test(expected = Exception.class)
    public void testCheckLogin_UserCorrect_Admin() throws Exception {
        System.out.println("checkLogin_UserCorrect_Admin");
        InsertTestingData();
        String username = "admin";
        String password = "admin";
        int expResult = 1;
        int result = UserController.checkLogin(username, password);
  
      
    }
     

    /**
     * Test of register method, of class ServerController.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        String firstName = "1";
        String lastName = "2";
        String roomNumber = "901";
        String email = "qiannan@hotmail.com";
        String username = "1";
        String password = "2";
        String password1 = "2";
        String expResult = "The room number does not exist (1)";
        String result = UserController.register(firstName, lastName, roomNumber, email, username, password, password1);
        assertEquals(expResult, result);
      
    }


    /**
     * Test of getSystemStatus method, of class ServerController.
     */
    @Test(expected = Exception.class)
    public void testGetSystemStatus() throws Exception {
        System.out.println("getSystemStatus");
        Persistance.initPersistance();
        String username = "mfa";
        String password = "1111";
        ServerController instance = ServerController.getInstance();
        instance.initializeSystem();
    
         // Get system status
      //  SystemStatus expResult = new SystemStatus(position.getOfficeAddress(), RobotPositionAccessor.isMoving(), true, "admin");
       SystemStatus result = instance.getSystemStatus(username, password);
      // SystemStatus expResult = ServerController.getInstance().getSystemStatus(username,password);
      
   
    }

    /**
     * Test of createOffice method, of class ServerController.
     */
    @Test
    public void testCreateOffice() throws Exception {
        System.out.println("createOffice");
        Office office = new Office("604", "y", "5", "x", "-10", null, o2);
        ServerController instance = ServerController.getInstance();
        instance.createOffice(office);
        
    }

    /**
     * Test of getUserDeliveryList method, of class ServerController.
     */
    @Test (expected = Exception.class)
    public void testGetUserDeliveryList() throws Exception {
        System.out.println("getUserDeliveryList");
        String username = "mfa";
        String password = "1111";
       ServerController instance = ServerController.getInstance();
        instance.initializeSystem();
        Persistance.initPersistance();
        ADSUser sender = UserController.findUser(username);
       List<Delivery>expResult = DeliveryHistory.getUserDeliveryList(sender);
       List<Delivery>result = instance.getUserDeliveryList(username, password);
       
       assertEquals(expResult, result);
      
    }

    /**
     * Test of getDeliveryList method, of class ServerController.
     */
    @Test (expected = Exception.class)
    public void testGetDeliveryList() throws Exception {
        System.out.println("getDeliveryList");
        String username = "mfa";
        String password = "1111";
        ServerController instance = ServerController.getInstance();
        instance.initializeSystem();
        List<Delivery>result = instance.getDeliveryList(username, password);
       List<Delivery>expResult = DeliveryHistory.getDeliveryList();
       assertEquals(expResult, result);
    }

    /**
     * Test of clearOffices method, of class ServerController.
     */
    @Test 
    public void testClearOffices() throws Exception {
        System.out.println("clearOffices");
        ServerController instance1 = ServerController.getInstance();
        String expResult = "preCreatedMapDeleted";
        String result = instance1.clearOffices();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of createLinksBtwOffices method, of class ServerController.
     */
    @Test
    public void testCreateLinksBtwOffices() throws Exception {
        System.out.println("createLinksBtwOffices");
        ServerController instance1= ServerController.getInstance();
        instance1.createLinksBtwOffices();
   
    }

    /**
     * Test of getDeliveryDetails method, of class ServerController.
     */
    @Test(expected = Exception.class)
    public void testGetDeliveryDetails() throws Exception {
        System.out.println("getDeliveryDetails");
        String username = "mfa";
        String password = "1111";
        int deliveryId = 0;
        ServerController instance = ServerController.getInstance();
        Delivery delivery = DeliveryHistory.getDelivery(deliveryId);
        List<DeliveryStep> deliveryStates = delivery.getStateList();
        List<String[]> result = new ArrayList(deliveryStates.size());
        for (DeliveryStep ds : deliveryStates) {
             result.add(new String[]{ds.getTimeCreation().toString(), ds.getClass().getSimpleName()});
        }
        // Return the list of delivery details
 
        //List<String> expResult = Arrays.asList("","") ;
        List expResult = instance.getDeliveryDetails(username, password, deliveryId);
 
    }

    /**
     * Test of getMapDrawingArray method, of class ServerController.
     */
    @Test(expected=Exception.class)
    public void testGetMapDrawingArray() throws Exception {
        System.out.println("getMapDrawingArray");
        ServerController instance = ServerController.getInstance();
        ArrayList expResult = FloorMap.getMapDrawingArray();
        ArrayList result = instance.getMapDrawingArray();
        assertEquals(expResult, result);
       
    }
    
     private void initializeSystem() throws ServerInitializedException {
       
        state = STATE_SYSTEM_INITIALIZED;
    }
      
    /**
     * Test of getRobotPosition method, of class SystemStatus.
     */
    @Test
    public void testGetRobotPosition() {
        System.out.println("getRobotPosition");
        String expResult = "Robot is in office 02";
        String result = instance.getRobotPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRobotPosition method, of class SystemStatus.
     */
    @Test
    public void testSetRobotPosition() {
        System.out.println("setRobotPosition");
        String robotPosition = "Robot is in office 02";
        instance.setRobotPosition(robotPosition);
     
    }

    /**
     * Test of isRobotIsMoving method, of class SystemStatus.
     */
    @Test
    public void testIsRobotIsMoving() {
        System.out.println("isRobotIsMoving");
        boolean expResult = true;
        boolean result = instance.isRobotIsMoving();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRobotIsMoving method, of class SystemStatus.
     */
    @Test
    public void testSetRobotIsMoving() {
        System.out.println("setRobotIsMoving");
        boolean robotIsMoving = true;
        instance.setRobotIsMoving(robotIsMoving);
    }

    /**
     * Test of isServerInitialized method, of class SystemStatus.
     */
    @Test
    public void testIsServerInitialized() {
        System.out.println("isServerInitialized");
        boolean expResult = true;
        boolean result = instance.isServerInitialized();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of setServerInitialized method, of class SystemStatus.
     */
    @Test
    public void testSetServerInitialized() {
        System.out.println("setServerInitialized");
        boolean serverInitialized = true;
        instance.setServerInitialized(serverInitialized);
     
    }

    /**
     * Test of getUsersInPosition method, of class SystemStatus.
     */
    @Test
    public void testGetUsersInPosition() {
        System.out.println("getUsersInPosition");
        String expResult = "User is in office 03";
        String result = instance.getUsersInPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsersInPosition method, of class SystemStatus.
     */
    @Test
    public void testSetUsersInPosition() {
        System.out.println("setUsersInPosition");
        String usersInPosition = "User is in office 03";
        instance.setUsersInPosition(usersInPosition);
    }
    
      /**
     * Test of drawAction method, of class AdminCreateFloorMapView.
     */
    @Test (expected = Exception.class)
    public void testDrawAction() throws Exception{
        System.out.println("drawAction");
        AdminCreateFloorMapView floormap = new AdminCreateFloorMapView();
        floormap.drawAction();
    }

    /**
     * Test of getOutputText method, of class AdminCreateFloorMapView.
     */
    @Test
    public void testGetOutputText() {
        System.out.println("getOutputText");
        
        AdminCreateFloorMapView floormap = new AdminCreateFloorMapView();
        JTextArea result = floormap.getOutputText();
    }

    /**
     * Test of main method, of class AdminCreateFloorMapView.
     */
    @Test
    public void testMain_AdminCreateFloorMapView() {
        System.out.println("Main_AdminCreateFloorMapView");
        String[] arasdas = {"s","t","a","r","t"};
        AdminCreateFloorMapView.main(arasdas);
    }
    
 
    /**
     * Test of setNonInitializedAppearance method, of class AdminMainView.
     */
    @Test (expected = Exception.class)
    public void testSetNonInitializedAppearance() throws ServerNonInitializedException{
        System.out.println("setNonInitializedAppearance");
        
        //Create the AdminMainView window and show it to the administrator
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(client).setVisible(true);
            }
        });
        
        AdminMainView AdminMain = new AdminMainView(client);
        AdminMain.setNonInitializedAppearance();
    }

    /**
     * Test of setInitializedAppearance method, of class AdminMainView.
     */
    @Test (expected = Exception.class)
    public void testSetInitializedAppearance() throws ServerInitializedException{
        System.out.println("setInitializedAppearance");
        
        //Create the AdminMainView window and show it to the administrator
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(client).setVisible(true);
            }
        });
        
        AdminMainView AdminMain = new AdminMainView(client);
        AdminMain.setInitializedAppearance();
    }
    
    
    /**
     * Test of stateNonLoggedIn method, of class ClientController.
     */
    @Test
    public void testStateNonLoggedIn_0arg() {
        System.out.println("* * testStateNonLoggedIn_0arg()");
        //UserMainView viewToDispose = new UserMainView();
        ClientController instance1 = null;
        // create a thread and run it to show Login window to user
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView(client).setVisible(true);
            }
        });
    }
    
    
    /**
     * Test of reset method, of class DeliveryStatusView.
     */
    @Test
    public void testReset() {
        System.out.println("reset");
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.reset();
    }

    /**
     * Test of resetDeliveriesTable method, of class DeliveryStatusView.
     */
    @Test
    public void testResetDeliveriesTable() {
        System.out.println("resetDeliveriesTable");
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.resetDeliveriesTable();
    }

    /**
     * Test of resetDeliveryDetailsTable method, of class DeliveryStatusView.
     */
    @Test
    public void testResetDeliveryDetailsTable() {
        System.out.println("resetDeliveryDetailsTable");
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.resetDeliveryDetailsTable();
    }

    /**
     * Test of addDelivery method, of class DeliveryStatusView.
     */
    @Test
    public void testAddDelivery() {
        System.out.println("addDelivery");
        
        // Create and display the UserMainView window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryStatusView(client).setVisible(true);
            }
        });

        int id = 0;
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        String usernameSender = "tony";
        String usernameReceiver = "emma";
        double priority = 0.9;
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        
        deliveryStatus.addDelivery(id, timestampField, usernameSender, usernameReceiver, priority);
    }

    /**
     * Test of addDeliveryDetail method, of class DeliveryStatusView.
     */
    @Test
    public void testAddDeliveryDetail() {
        System.out.println("addDeliveryDetail");
        
        // Create and display the UserMainView window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryStatusView(client).setVisible(true);
            }
        });
        
        String[] s = { "h", "e","l", "l", "o", "." };
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.addDeliveryDetail(s);
    }

    /**
     * Test of showDeliveryDetails method, of class DeliveryStatusView.
     */
    @Test
    public void testShowDeliveryDetails() {
        System.out.println("showDeliveryDetails");
        
        // Create and display the UserMainView window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryStatusView(client).setVisible(true);
            }
        });
        
        int deliveryId = 2;
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.showDeliveryDetails(deliveryId);
    }

    /**
     * Test of hideDeliveryDetails method, of class DeliveryStatusView.
     */
    @Test
    public void testHideDeliveryDetails() {
        System.out.println("hideDeliveryDetails");
        
        // Create and display the UserMainView window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryStatusView(client).setVisible(true);
            }
        });
        
        DeliveryStatusView deliveryStatus = new DeliveryStatusView (client);
        deliveryStatus.hideDeliveryDetails();
    }
    
   /**
     * Test of getDeliveryStatusView method, of class SystemStatusView.
     */
    @Test
    public void testGetDeliveryStatusView() {
        System.out.println("getDeliveryStatusView");
        //Persistance.initPersistance();
        //EntityManager em = Persistance.getEntityManager();
        //List<Delivery> deliveries = em.createNamedQuery("Delivery.searchAllDeliveryList")
        //    .getResultList();
        //System.out.println(deliveries);
        SystemStatusView systemStatus = new SystemStatusView(client);
        //DeliveryStatusView expResult = new DeliveryStatusView(client);
        DeliveryStatusView result = systemStatus.getDeliveryStatusView();
        //assertEquals(expResult, result);
    }
   
    @Test
    public void testUserMainViewConst() {
        System.out.println("* * Test testUserMainViewConst()");
        UserMainView main = new UserMainView(client);
    }
     /**
     * Test of init method, of class ServerCommunicator.
     */
    @Test
    public void testInit() {
        System.out.println("init");
        //ServerCommunicatorStub.init();
    }
    
    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("No public methods in this class");
    }
     /**
     * Test of hashCode method, of class ADSUser.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        int expResult = 110770295;
        int result = ads.hashCode();
        //System.out.println("hash code is" + ads.hashCode());
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class ADSUser.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        Object object = new ADSUser("Qiannan","Li",
                new Office("603","604","602","-x","5","y","6"), 
                "t@a.c", "twang","1111");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        boolean expResult = true;
        boolean result = ads.equals(object);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ADSUser.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "ads.data.User[ username=twang ]";
        String result = ads.toString();
        assertEquals(expResult, result);     
    }

    /**
     * Test of getFirstName method, of class ADSUser.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "Tuo";
        String result = ads.getFirstName();
        assertEquals(expResult, result);  
    }

    /**
     * Test of setFirstName method, of class ADSUser.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "Tony";
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        ads.setFirstName(firstName);
    }

    /**
     * Test of getLastName method, of class ADSUser.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");

        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "Wang";
        String result = ads.getLastName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastName method, of class ADSUser.
     */
    @Test
    public void testSetLastName() {
        System.out.println("setLastName");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String lastName = "WANG";
        ads.setLastName(lastName);
    }

    /**
     * Test of getEmail method, of class ADSUser.
     */
    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "t@a.c";
        String result = ads.getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmail method, of class ADSUser.
     */
    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
       
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String email = "tuowang@a.c";
        ads.setEmail(email);
    }

    /**
     * Test of getUsername method, of class ADSUser.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "twang";
        String result = ads.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsername method, of class ADSUser.
     */
    @Test
    public void testSetUsername() {
        System.out.println("setUsername");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  

        String username = "tonyw";
        ads.setUsername(username);
    }

    /**
     * Test of getPassword method, of class ADSUser.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        String expResult = "1111";
        String result = ads.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPassword method, of class ADSUser.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "1234";
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        ads.setPassword(password);
    }

    /**
     * Test of getOffice method, of class ADSUser.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        Office expResult = new Office("604","605","603","-x","3","y","4");
        Office result = ads.getOffice();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOffice method, of class ADSUser.
     */
    @Test
    public void testSetOffice() {
        System.out.println("setOffice");      
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        Office office = new Office("602","603","601","-y","9","x","5");
        ads.setOffice(office);
    }

    /**
     * Test of isAdmin method, of class ADSUser.
     */
    @Test
    public void testIsAdmin() {
        System.out.println("isAdmin");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        boolean expResult = false;
        boolean result = ads.isAdmin();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAdmin method, of class ADSUser.
     */
    @Test
    public void testSetAdmin() {
        System.out.println("setAdmin");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        boolean admin = true;

        ads.setAdmin(admin);
    }
    
      /**
     * Test of toString method, of class BookedDelivery.
     */
    @Test
    public void testToString_BookDelivery() {
        System.out.println("toString");
    
        ADSUser sender = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
                new Office("603","604","602","x","4","y","3"), 
                "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        //create object of BookedDelivery
        BookedDelivery bookDelivery = new BookedDelivery(timestampField,
                new Delivery (sender,receiver,timestampField, priority ));
        String expResult = "ads.data.BookedDelivery[ id=null ]";
        String result = bookDelivery.toString();
        assertEquals(expResult, result);  
    }
      /**
     * Test of getId method, of class Box.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Box box = new Box();
        int expResult = 0;
        int result = box.getId();
        assertEquals(expResult, result);
        //System.out.println("ID is " + box.getId());    
    }

    /**
     * Test of setId method, of class Box.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 2;
        Box box = new Box();
        box.setId(id);
    }

    /**
     * Test of getDelivery method, of class Box.
     */
    @Test
    public void testGetDelivery() {
        System.out.println("getDelivery");
        Box box = new Box();
        Delivery expResult = null;
        Delivery result = box.getDelivery();
        assertEquals(expResult, result);
        //System.out.println("result is " + box.getDelivery());    
    }

    /**
     * Test of setDelivery method, of class Box.
     */
    @Test
    public void testSetDelivery() {
        System.out.println("setDelivery");
        ADSUser sender = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
                new Office("603","604","602","x","4","y","3"), 
                "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        Delivery delivery = new Delivery (sender,receiver,timestampField, priority );
        
        Box box = new Box();
        
        box.setDelivery(delivery);
    }

    /**
     * Test of hashCode method, of class Box.
     */
    @Test
    public void testHashCode_Box() {
        System.out.println("hashCode");
        Box box = new Box();
        int expResult = 0;
        int result = box.hashCode();
        assertEquals(expResult, result);  
    }

    /**
     * Test of equals method, of class Box.
     */
    @Test
    public void testEquals_Test() {
        System.out.println("equals_Test");
        Box box1 = new Box();
        int id = 3;
        Object box2 = new Box();
        box1.setId(id);
        Box other = (Box)box2;
        other.setId(id);
        
        boolean expResult = true;
        boolean result = box1.equals(box2);
        assertEquals(expResult, result);
        }

    /**
     * Test of toString method, of class Box.
     */
    @Test
    public void testToString_Box() {
        System.out.println("toString_Box");
        Box box = new Box();
        String expResult = "ads.data.Box[ id=0 ]";
        String result = box.toString();
        assertEquals(expResult, result);   
    }
    
    /**
     * Test of toString method, of class DeliveredDelivery.
     */
    @Test
    public void testToString_DeliverdDelivery() {
        System.out.println("toString_DeliveredDelivery");
        ADSUser sender = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
                new Office("603","604","602","x","4","y","3"), 
                "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        DeliveredDelivery deliveredDelivery = new DeliveredDelivery(timestampField, 
                new Delivery(sender, receiver, timestampField, priority));
        String expResult = "ads.data.DeliveredDelivery[ id=null ]";
        String result = deliveredDelivery.toString();
        assertEquals(expResult, result);
        } 
    
    /**
     * Test of getId method, of class Delivery.
     */
    @Test
    public void testGetId_Delivery() {
        System.out.println("getId_Delivery");
        
       ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        int expResult = 0;
        int result = delivery.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Delivery.
     */
    @Test
    public void testSetId_Delivery() {
        System.out.println("setId_Delivery");
        
               ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        
        int id = 3;
        delivery.setId(id);
        }

    /**
     * Test of getPriority method, of class Delivery.
     */
    @Test
    public void testGetPriority() {
        System.out.println("getPriority");
               ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        double expResult = 0.9;
        double result = delivery.getPriority();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of setPriority method, of class Delivery.
     */
    @Test
    public void testSetPriority() {
        System.out.println("setPriority");
       ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        double priority_set = 0.3;

        delivery.setPriority(priority_set);
    }

    /**
     * Test of getSender method, of class Delivery.
     */
    @Test
    public void testGetSender() {
        System.out.println("getSender");
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
       
        ADSUser expResult = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser result = delivery.getSender();
        assertEquals(expResult, result);
    }

    /**
     * Test of setSender method, of class Delivery.
     */
    @Test
    public void testSetSender() {
        System.out.println("setSender");
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        ADSUser sender_set = new ADSUser("Marc","Gamell",
            new Office("605","606","604","x","4","y","3"), 
            "m@a.c", "mgamell","1111");
        
        delivery.setSender(sender_set);
    }

    /**
     * Test of getReceiver method, of class Delivery.
     */
    @Test
    public void testGetReceiver() {
        System.out.println("getReceiver");
        
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        ADSUser expResult = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        
        ADSUser result = delivery.getReceiver();
        assertEquals(expResult, result);
    }

    /**
     * Test of setReceiver method, of class Delivery.
     */
    @Test
    public void testSetReceiver() {
        System.out.println("setReceiver");
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        ADSUser receiver_set = new ADSUser("Marc","Gamell",
            new Office("605","606","604","x","4","y","3"), 
            "m@a.c", "mgamell","1111");;
        delivery.setReceiver(receiver_set);
    }

    /**
     * Test of getTimestampField method, of class Delivery.
     */
    @Test
    public void testGetTimestampField() {
        System.out.println("getTimestampField");
        
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        
        Timestamp expResult = Timestamp. valueOf("2012-11-10 05:52:56");
        Timestamp result = delivery.getTimestampField();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTimestampField method, of class Delivery.
     */
    @Test
    public void testSetTimestampField() {
        System.out.println("setTimestampField");
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        
        Timestamp timestampField_set = Timestamp.valueOf("2012-11-10 05:58:56");
        delivery.setTimestampField(timestampField_set);
    }

    /**
     * Test of hashCode method, of class Delivery.
     */
    @Test
    public void testHashCode_Delivery() {
        System.out.println("hashCode_Delivery");
        ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        int expResult = 249;
        int result = delivery.hashCode();
        assertEquals(expResult, result);
    }
        
 /**
     * Test of getId method, of class Office.
     */
    @Test
    public void testGetId_Office() {
        System.out.println("getId_Office");
        Office office = new Office("604","605","603","-x","3","y","4");
        int expResult = 0;
        int result = office.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Office.
     */
    @Test
    public void testSetId_Office() {
        System.out.println("setId_Office");
        int id = 3;
         Office office = new Office("604","605","603","-x","3","y","4");
        office.setId(id);
    }

    /**
     * Test of getOfficeAddress method, of class Office.
     */
    @Test
    public void testGetOfficeAddress() {
        System.out.println("getOfficeAddress");
         Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "604";
        String result = office.getOfficeAddress();
        assertEquals(expResult, result);
        //System.out.println("Office address is " + office.getOfficeAddress());    
    }

    /**
     * Test of getNextOfficeAddress method, of class Office.
     */
    @Test
    public void testGetNextOfficeAddress() {
        System.out.println("getNextOfficeAddress");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "605";
        String result = office.getNextOfficeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNextOfficeAddress method, of class Office.
     */
    @Test
    public void testSetNextOfficeAddress() {
        System.out.println("setNextOfficeAddress");
        String nextOfficeAddress = "607";
        Office office = new Office("604","605","603","-x","3","y","4");
        office.setNextOfficeAddress(nextOfficeAddress);
    }

    /**
     * Test of getPreOfficeAddress method, of class Office.
     */
    @Test
    public void testGetPreOfficeAddress() {
        System.out.println("getPreOfficeAddress");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "603";
        String result = office.getPreOfficeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPreOfficeAddress method, of class Office.
     */
    @Test
    public void testSetPreOfficeAddress() {
        System.out.println("setPreOfficeAddress");
        String preOfficeAddress = "602";
        Office office = new Office("604","605","603","-x","3","y","4");
        office.setPreOfficeAddress(preOfficeAddress);
    }

    /**
     * Test of hashCode method, of class Office.
     */
    @Test
    public void testHashCode_Office() {
        System.out.println("hashCode_Office");
        Office office = new Office("604","605","603","-x","3","y","4");
        int expResult = 53434;
        int result = office.hashCode();
        assertEquals(expResult, result);
        }

    /**
     * Test of toString method, of class Office.
     */
    @Test
    public void testToString_Office() {
        System.out.println("toString_Office");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "ads.data.Office[ officeAddress=604 ]";
        String result = office.toString();
        //assertEquals(expResult, result);
        System.out.println(office.toString());
    }
    
       /**
     * Test of toString method, of class PickedUpDelivery.
     */
    @Test
    public void testToString_PickedUpDelivery() {
        System.out.println("toString");
              ADSUser sender = new ADSUser("Tuo","Wang",
            new Office("604","605","603","-x","3","y","4"), 
            "t@a.c", "twang","1111");
        ADSUser receiver = new ADSUser("Qiannan","Li",
            new Office("603","604","602","x","4","y","3"), 
            "q@a.c", "qli","1111");
        Timestamp timestampField = Timestamp.valueOf("2012-11-10 05:52:56");
        double priority = 0.9;
        
        Delivery delivery = new Delivery(sender, receiver, timestampField, priority);
        PickedUpDelivery pkupdelivery = new PickedUpDelivery(timestampField, delivery);
        String expResult = "ads.data.PickedUpDelivery[ id=null ]";
        String result = pkupdelivery.toString();
        assertEquals(expResult, result);    
    }
     /**
     * Test of isIsMoving method, of class RobotPosition.
     */
    @Test
    public void testIsIsMoving() {
        System.out.println("isIsMoving");
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        boolean expResult = true;
        boolean result = robotposition.isIsMoving();
        assertEquals(expResult, result);
    }

    /**
     * Test of setIsMoving method, of class RobotPosition.
     */
    @Test
    public void testSetIsMoving() {
        System.out.println("setIsMoving");
        boolean isMoving = false;
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        robotposition.setIsMoving(isMoving);
    }

    /**
     * Test of hashCode method, of class RobotPosition.
     */
    @Test
    public void testHashCode_RobotPosition() {
        System.out.println("hashCode_RobotPosition");
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        int expResult = 53433;
        int result = robotposition.hashCode();
        assertEquals(expResult, result);
        //System.out.println("result =" + robotposition.hashCode());   
    }

    /**
     * Test of equals method, of class RobotPosition.
     */
    @Test
    public void testEquals_RobotPosition() {
        System.out.println("equals_RobotPosition");
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        Object object = new RobotPosition(office, true);
        boolean expResult = true;
        boolean result = robotposition.equals(object);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class RobotPosition.
     */
    @Test
    public void testToString_RobotPosition() {
        System.out.println("toString_RobotPosition");
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        String expResult = "ads.resources.data.RobotPosition[ lastKnownPosition=ads.data.Office[ officeAddress=603 ] ]";
        String result = robotposition.toString();
        assertEquals(expResult, result);
    }
     /**
     * Test of calculateNumOfHops method, of class FloorMap.
     */
    @Test (expected = Exception.class)
    public void testCalculateNumOfHops() throws Exception{
        System.out.println("calculateNumOfHops");
        Office origin = new Office("604","605","603","-x","3","y","4");
        Office destination = new Office("606","607","605","-x","3","y","4");
        int expResult = 0;
        int result = FloorMap.calculateNumOfHops(origin, destination);
        assertEquals(expResult, result);
    }
    /**
     * Test of isMoving method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testIsMoving() throws Exception {
        System.out.println("isMoving");
        Persistance.initPersistance();
        boolean expResult = false;
        boolean result = RobotPositionAccessor.isMoving();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMoving method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testSetMoving() throws Exception{
        System.out.println("setMoving");
        Persistance.initPersistance();
        boolean isMoving = false;
        RobotPositionAccessor.setMoving(isMoving);
    
    }


    /**
     * Test of getRobotPosition method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testGetRobotPosition_RobotPositionAccessor() throws Exception{
        System.out.println("getRobotPosition");
        Office expResult = null;
        Office result = RobotPositionAccessor.getRobotPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateRobotPositionToNext method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testUpdateRobotPositionToNext() throws Exception {
        System.out.println("updateRobotPositionToNext");
        RobotPositionAccessor.updateRobotPositionToNext();
     
    }
    
    /**
     * Test of allocateBox method, of class SectionedBox.
     */
    @Test (expected = Exception.class)
    public void testAllocateBox() throws Exception {
        System.out.println("allocateBox");
        Persistance.initPersistance();
        Delivery delivery = null;
        int expResult = 0;
        int result = SectionedBox.allocateBox(delivery);
        assertEquals(expResult, result);
    }

    /**
     * Test of deallocateBox method, of class SectionedBox.
     */
    @Test (expected = Exception.class)
    public void testDeallocateBox() throws Exception {
        System.out.println("deallocateBox");
        Delivery delivery = null;
        int expResult = 0;
        int result = SectionedBox.deallocateBox(delivery);
        assertEquals(expResult, result);
    }
    
    
}
 
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    


   
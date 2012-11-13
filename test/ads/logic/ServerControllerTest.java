/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import ads.resources.data.DeliveryStep;
import ads.resources.data.Office;
import ads.resources.datacontroller.DeliveryHistory;
import ads.resources.datacontroller.FloorMap;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.RobotPositionAccessor;
import ads.resources.datacontroller.UserController;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Qiannan
 */
public class ServerControllerTest {
    private static Office o2;
     private static final int STATE_SYSTEM_INITIALIZED = 1;
     private int state;
    private Office o5;
    private Office o4;
    
    public ServerControllerTest() {
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
     * Test of getInstance method, of class ServerController.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
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
            } catch (RemoteException | NotBoundException ex) {}
            // Binds a remote reference to the "ServerControllerInterface" in
           // this registry
            registry.bind("ServerControllerInterface", stub);
            System.out.println("Server ready");
        } catch (RemoteException | AlreadyBoundException e) {
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
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("m", "a", o2, "mfa@gm", "mfa", "1111");
        em.persist(u);
        em.getTransaction().commit();
        String username = "mfa";
        String password = "1111";
        int expResult = 2;
        int result = UserController.checkLogin(username, password);
        assertEquals(expResult, result);
      
    }
    
     @Test
    public void testCheckLogin_UserNotCorrect() throws Exception {
        System.out.println("checkLogin_UserNotCorrect");
        Persistance.initPersistance();
        String username = "m";
        String password = "1111";
        int expResult = 0;
        int result = UserController.checkLogin(username, password);
        assertEquals(expResult, result);
      
    }
     
      @Test
    public void testCheckLogin_UserCorrect_Admin() throws Exception {
        System.out.println("checkLogin_UserCorrect_Admin");
        Persistance.initPersistance();
        String username = "admin";
        String password = "admin";
        int expResult = 1;
        int result = UserController.checkLogin(username, password);
        assertEquals(expResult, result);
      
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
        ServerController instance = ServerController.getInstance();
        String expResult = "preCreatedMapDeleted";
        String result = instance.clearOffices();
        assertEquals(expResult, result);
       
    }

    /**
     * Test of createLinksBtwOffices method, of class ServerController.
     */
    @Test
    public void testCreateLinksBtwOffices() throws Exception {
        System.out.println("createLinksBtwOffices");
        ServerController instance = ServerController.getInstance();
        instance.createLinksBtwOffices();
   
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
    
    
    
    

}
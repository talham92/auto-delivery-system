/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.Office;
import ads.resources.datacontroller.Persistance;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
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
public class DeliveryCoordinatorTest {
    private Office o2;
    
    public DeliveryCoordinatorTest() {
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
        Persistance.initPersistance();
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        ADSUser u = new ADSUser("m", "a", o2, "mfa@gm", "mfa", "1111");
        em.persist(u);
        em.getTransaction().commit();
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
    
    
     @Test(expected = NonBookedDeliveryException.class)
    public void testBookDelivery_UserNotCorrect() throws NonBookedDeliveryException {
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
    public void testGetInstance() {
        System.out.println("getInstance");
        DeliveryCoordinator expResult = DeliveryCoordinator.getInstance();
        DeliveryCoordinator result = DeliveryCoordinator.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
}

   
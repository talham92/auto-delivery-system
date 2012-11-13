/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.resources.data.Delivery;
import ads.resources.datacontroller.Persistance;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tonywang
 */
public class SystemStatusViewTest {
    //create instance of ClientControllerInterface
    private ClientControllerInterface client;
    
    public SystemStatusViewTest() {
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
}

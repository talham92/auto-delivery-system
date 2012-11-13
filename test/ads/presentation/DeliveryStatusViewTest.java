/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.sql.Timestamp;
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
public class DeliveryStatusViewTest {
    //create instance of ClientControllerInterface
    private ClientControllerInterface client;
    
    
    public DeliveryStatusViewTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        // Create and display the UserMainView window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeliveryStatusView(client).setVisible(true);
            }
        });
    }
    
    @After
    public void tearDown() {
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
}

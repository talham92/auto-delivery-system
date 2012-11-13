/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.sql.Timestamp;
import java.util.List;
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
public class DeliveryTest {
    
    public DeliveryTest() {
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
     * Test of getId method, of class Delivery.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        
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
    public void testSetId() {
        System.out.println("setId");
        
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
    public void testHashCode() {
        System.out.println("hashCode");
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
}

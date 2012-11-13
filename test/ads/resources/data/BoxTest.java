/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

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
public class BoxTest {
    
    public BoxTest() {
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
    public void testHashCode() {
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
    public void testEquals() {
        System.out.println("equals");
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
    public void testToString() {
        System.out.println("toString");
        Box box = new Box();
        String expResult = "ads.data.Box[ id=0 ]";
        String result = box.toString();
        assertEquals(expResult, result);   
    }
}

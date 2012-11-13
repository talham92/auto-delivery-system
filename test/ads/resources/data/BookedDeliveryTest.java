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
public class BookedDeliveryTest {
    
    public BookedDeliveryTest() {
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
     * Test of toString method, of class BookedDelivery.
     */
    @Test
    public void testToString() {
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
}

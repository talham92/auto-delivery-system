/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.Delivery;
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
public class SectionedBoxTest {
    
    public SectionedBoxTest() {
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

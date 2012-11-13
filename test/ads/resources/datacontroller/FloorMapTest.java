/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.Office;
import java.util.ArrayList;
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
public class FloorMapTest {
    
    public FloorMapTest() {
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

}

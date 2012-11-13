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
public class RobotPositionTest {
    
    public RobotPositionTest() {
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
    public void testHashCode() {
        System.out.println("hashCode");
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
    public void testEquals() {
        System.out.println("equals");
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
    public void testToString() {
        System.out.println("toString");
        Office office = new Office("603","604","602","x","4","y","3");        
        RobotPosition robotposition = new RobotPosition(office, true);
        String expResult = "ads.resources.data.RobotPosition[ lastKnownPosition=ads.data.Office[ officeAddress=603 ] ]";
        String result = robotposition.toString();
        assertEquals(expResult, result);
    }
}

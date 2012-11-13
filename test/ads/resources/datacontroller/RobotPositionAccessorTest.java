/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.datacontroller;

import ads.resources.data.Office;
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
public class RobotPositionAccessorTest {
    
    public RobotPositionAccessorTest() {
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
     * Test of isMoving method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testIsMoving() throws Exception {
        System.out.println("isMoving");
        Persistance.initPersistance();
        boolean expResult = false;
        boolean result = RobotPositionAccessor.isMoving();
        assertEquals(expResult, result);
    }

    /**
     * Test of setMoving method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testSetMoving() throws Exception{
        System.out.println("setMoving");
        Persistance.initPersistance();
        boolean isMoving = false;
        RobotPositionAccessor.setMoving(isMoving);
    
    }

    /**
     * Test of init method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testInit() throws Exception{
        System.out.println("init");
        RobotPositionAccessor.init();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRobotPosition method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testGetRobotPosition() throws Exception{
        System.out.println("getRobotPosition");
        Office expResult = null;
        Office result = RobotPositionAccessor.getRobotPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of updateRobotPositionToNext method, of class RobotPositionAccessor.
     */
    @Test (expected = Exception.class)
    public void testUpdateRobotPositionToNext() throws Exception {
        System.out.println("updateRobotPositionToNext");
        RobotPositionAccessor.updateRobotPositionToNext();
     
    }
}

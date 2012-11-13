/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Qiannan
 */
public class SystemStatusTest {
    //String robotPosition, boolean robotIsMoving, boolean serverInitialized, String usersInPosition
    SystemStatus instance = new SystemStatus("Robot is in office 02", true, true,"User is in office 03");
    
    public SystemStatusTest() {
        
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
     * Test of getRobotPosition method, of class SystemStatus.
     */
    @Test
    public void testGetRobotPosition() {
        System.out.println("getRobotPosition");
        String expResult = "Robot is in office 02";
        String result = instance.getRobotPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRobotPosition method, of class SystemStatus.
     */
    @Test
    public void testSetRobotPosition() {
        System.out.println("setRobotPosition");
        String robotPosition = "Robot is in office 02";
        instance.setRobotPosition(robotPosition);
     
    }

    /**
     * Test of isRobotIsMoving method, of class SystemStatus.
     */
    @Test
    public void testIsRobotIsMoving() {
        System.out.println("isRobotIsMoving");
        boolean expResult = true;
        boolean result = instance.isRobotIsMoving();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRobotIsMoving method, of class SystemStatus.
     */
    @Test
    public void testSetRobotIsMoving() {
        System.out.println("setRobotIsMoving");
        boolean robotIsMoving = true;
        instance.setRobotIsMoving(robotIsMoving);
    }

    /**
     * Test of isServerInitialized method, of class SystemStatus.
     */
    @Test
    public void testIsServerInitialized() {
        System.out.println("isServerInitialized");
        boolean expResult = true;
        boolean result = instance.isServerInitialized();
        assertEquals(expResult, result);
 
    }

    /**
     * Test of setServerInitialized method, of class SystemStatus.
     */
    @Test
    public void testSetServerInitialized() {
        System.out.println("setServerInitialized");
        boolean serverInitialized = true;
        instance.setServerInitialized(serverInitialized);
     
    }

    /**
     * Test of getUsersInPosition method, of class SystemStatus.
     */
    @Test
    public void testGetUsersInPosition() {
        System.out.println("getUsersInPosition");
        String expResult = "User is in office 03";
        String result = instance.getUsersInPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsersInPosition method, of class SystemStatus.
     */
    @Test
    public void testSetUsersInPosition() {
        System.out.println("setUsersInPosition");
        String usersInPosition = "User is in office 03";
        instance.setUsersInPosition(usersInPosition);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

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
public class UserMainViewTest {
    //create instance of ClientControllerInterface
    private ClientControllerInterface client;
    
    public UserMainViewTest() {
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
     * Test the constructor UserMainView() of this class
     */
    @Test
    public void testUserMainViewConst() {
        System.out.println("* * Test testUserMainViewConst()");
        UserMainView main = new UserMainView(client);
    }
}

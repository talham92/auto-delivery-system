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
public class EmailCheckerTest {
    
    public EmailCheckerTest() {
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
     * Test of checkEmail method, of class EmailChecker.
     */
    @Test
    public void testCheckEmail_NotCorrect() {
        System.out.println("checkEmail_NotCorrect");
        String email = "123hotmail.com";
        boolean expResult = true;
        boolean result = EmailChecker.checkEmail(email);
        assertFalse(result);
        // TODO review the generated test code and remove the default call to fail.
       
    }
    
    
    
    @Test
public void testCheckEmail_Correct(){
      System.out.println("checkEmail_Correct");
        String email = "123@hotmail.com";
        boolean expResult = true;
        boolean result = EmailChecker.checkEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.   
}
}


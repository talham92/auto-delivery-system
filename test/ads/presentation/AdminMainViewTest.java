/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.ServerInitializedException;
import ads.logic.ServerNonInitializedException;
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
public class AdminMainViewTest {
    //create instance of ClientControllerInterface
    private ClientControllerInterface client;
    
    public AdminMainViewTest() {
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
     * Test of setNonInitializedAppearance method, of class AdminMainView.
     */
    @Test (expected = Exception.class)
    public void testSetNonInitializedAppearance() throws ServerNonInitializedException{
        System.out.println("setNonInitializedAppearance");
        
        //Create the AdminMainView window and show it to the administrator
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(client).setVisible(true);
            }
        });
        
        AdminMainView AdminMain = new AdminMainView(client);
        AdminMain.setNonInitializedAppearance();
    }

    /**
     * Test of setInitializedAppearance method, of class AdminMainView.
     */
    @Test (expected = Exception.class)
    public void testSetInitializedAppearance() throws ServerInitializedException{
        System.out.println("setInitializedAppearance");
        
        //Create the AdminMainView window and show it to the administrator
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(client).setVisible(true);
            }
        });
        
        AdminMainView AdminMain = new AdminMainView(client);
        AdminMain.setInitializedAppearance();
    }
}

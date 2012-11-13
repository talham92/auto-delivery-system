/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.ServerControllerInterface;
import ads.logic.SystemStatus;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
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
public class ClientControllerTest {
    //client controller interface
    private ClientControllerInterface client;
    
    //this variable stores current states of interaction between user and sytem
    private int state;
    
    //Create an object of class ClientController     
    private ServerControllerInterface server;
    
    
    public ClientControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("* Begin: ads.presentation.ClientControllerTest.java");

    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("* End: ads.presentation.ClientControllerTets.java");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class ClientController.
     */
    @Test
    public void testClientControllertMain() {
        System.out.println("* * testClientControllertMain() ");
        //state = STATE_NON_LOGGED_IN;       
        String host = "spring.rutgers.edu";
        
        try {
            /*try to create reference to the the remote object registry 
             * for the local host on the default registry port of 1099. */
            Registry registry = LocateRegistry.getRegistry();
            /* Look up ServerControllerInterface in the registry */
            server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
        } catch (Exception e) {
            //try to find remote server with host name spring.rutgers.edu
            try {
                Registry registry = LocateRegistry.getRegistry(host);
                server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
            //when neither local or remote server can be found, show messages and exit
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null,
                    "Unable to connect to server (nor local or remote). Exiting...",
                    "Register error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        } 
    }
    /**
     * Test of stateNonLoggedIn method, of class ClientController.
     */
    @Test
    public void testStateNonLoggedIn_0arg() {
        System.out.println("* * testStateNonLoggedIn_0arg()");
        //UserMainView viewToDispose = new UserMainView();
        ClientController instance = null;
        // create a thread and run it to show Login window to user
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView(client).setVisible(true);
            }
        });
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.NonBookedDeliveryException;
import ads.logic.ServerControllerInterface;
import ads.resources.data.ADSUser;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mgamell
 */
public class ClientController implements ClientControllerInterface {
    public static final int STATE_NON_LOGGED_IN = 1;
    public static final int STATE_REGISTERING = 2;
    public static final int STATE_BOOKING = 3;
    public static final int STATE_EDITING = 4;
    public static final int STATE_SHOWING_HELP = 5;
    public static final int STATE_VIEWING_DELIVERY_HISTORY = 6;
    public static final int STATE_ADMIN_MAIN = 7;
    public static final int STATE_ADMIN_CREATING_FLOOR_MAP = 8;
    
    private static final int userNotCorrect=0;
    private static final int userCorrect_Admin=1;
    private static final int userCorrect_NotAdmin=2;
    
    private int state;
    private final ClientController controller;
    private String username;
    private String password;
    
    private void initGUI() {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
 
    private ServerControllerInterface server;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        ClientController c = new ClientController(host);
    }

    private ClientController(String host) {
        state = STATE_NON_LOGGED_IN;
        controller = this;
        initServerConnection(host);
        initGUI();

        // Configure first state: allow Login or Register
        stateNonLoggedIn();

//        try {
//            server.stopServer("mgamell", "password");
//        } catch(Exception e) {}
    }

    private void initServerConnection(String host) {
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
        } catch (RemoteException | NotBoundException e) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "initServerConnection", e);
            System.exit(-1);
        }
    }

    private void stateNonLoggedIn() {
        // Change the state to non-logged in. We don't need to care about the
        // current state, because from every other state we can come to this 
        // state.
        state = STATE_NON_LOGGED_IN;

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView(controller).setVisible(true);
            }
        });
    }
    
    @Override
    public void register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1, RegisterView register) {
        // Check that we are in a correct state:
        if(state != STATE_REGISTERING) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "register error: state != STATE_NON_LOGGED_IN");
            System.exit(-1);
        }
        
        // Try to register to the server
        String error = "Error during server connection";
        try {
            error = server.register(firstName, lastName, roomNumber, email, username, password, password1);
            // If no error encountered, error will be null.
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (error == null) {
            // No error encountered, user inserted correctly to the system.
            // Show a message indicating that everything went fine
            JOptionPane.showMessageDialog(register,
                "User registered correctly",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            // The user and the password are correct, we can hide the register
            // view and continue to the next state (main page, that will be
            // the main usage case: bookDelivery). We also need to store
            // username and password for futute usage.
            // 0. store locally username and password
            this.username = username;
            this.password = password;
            // 1. set frame invisible
            register.setVisible(false);
            // 2. free frame resources
            register.dispose();
            // 3. turn to state booking
            stateBooking();
        } else {
            // Show an error indicating that the username or the password
            // are incorrect
            JOptionPane.showMessageDialog(register,
                error,
                "Register error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void wantsToRegister(LoginView login) {
        // Check that we are in a correct state:
        if(state != STATE_NON_LOGGED_IN) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "wantsToRegister error: state != STATE_NON_LOGGED_IN");
            System.exit(-1);
        }

        // The user wants to register. Let's show the register screen.
        // Change the state to non-logged in. We don't need to care about the
        // current state, because from every other state we can come to this 
        // state.
        state = STATE_REGISTERING;

        // Disappear 
        // Hide the screen
        // 1. set frame invisible
        login.setVisible(false);
        // 2. free frame resources
        login.dispose();

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterView(controller).setVisible(true);
            }
        });
    }

    @Override
    public void login(String username, String password, LoginView l) {
        // Check that we are in a correct state:
        if(state != STATE_NON_LOGGED_IN) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "login error: state != STATE_NON_LOGGED_IN");
            System.exit(-1);
        }
        
        // Access the server to know whether the login is correct or not
        int loginCorrect = userNotCorrect;
        try {
            loginCorrect = server.checkLogin(username, password);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

        // Check the server answer
        if(loginCorrect==userNotCorrect) {
            // Show an error indicating that the username or the password
            // are incorrect
            JOptionPane.showMessageDialog(l,
                "Username or password incorrect",
                "Login error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            // The user and the password are correct, we can hide the login
            // view and continue to the next state (main page, that will be
            // the main usage case: bookDelivery). We also need to store
            // username and password for futute usage.
            // 0. store locally username and password
            this.username = username;
            this.password = password;
            // 1. set frame invisible
            l.setVisible(false);
            // 2. free frame resources
            l.dispose();
            // 3. turn to BookDelivery state
            if(loginCorrect==userCorrect_Admin)
                stateAdminMain();
            else if(loginCorrect==userCorrect_NotAdmin)
                stateBooking();
        }
    }
    private void stateAdminMain(){
        state=STATE_ADMIN_MAIN;
        //Create the related form and display
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(controller).setVisible(true);
            }
        });
    }
    
    @Override
    public void wantsToCreateFloorMap(AdminMainView v){
        // set the admin main view invisible and dispose it
        v.setVisible(false);
        v.dispose();
        
        stateAdminCreateFloorMap();
    }
    private void stateAdminCreateFloorMap(){
        state = STATE_ADMIN_CREATING_FLOOR_MAP; // set the state of the controller
        //
        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminCreateFloorMapView(controller).setVisible(true);
            }
        });
    }        
    private void stateBooking() {
        // Change the state to Booking. Does not need to check the coming state,
        // as this state is accessible by every other.
        state = STATE_BOOKING;

        // Create and display the form
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookDeliveryView(controller).setVisible(true);
            }
        });
    }
    
    @Override
    public List<String[]> searchUser_NameOffice(String name, String office)
    {
        Set<ADSUser> result;
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.searchUser_NameOffice(this.username, this.password, name, office);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        List<String[]> resultString = new ArrayList<>(result.size());
        for(ADSUser u : result) {
            resultString.add(new String[] {u.getUsername(), u.getFirstName()+" "+u.getLastName(), u.getOffice().getOfficeAddress()});
        }
        
        return resultString;
    }
    
    @Override
    public void bookDelivery(double urgency, List<String> targetList, BookDeliveryView bookDeliveryView)
    {
        if(state != STATE_BOOKING) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "register error: state != STATE_BOOKING");
            System.exit(-1);
        }
        // Try to call the related function of the server
        try {
            server.bookDelivery(this.username, this.password, urgency, targetList);
            // If no error encountered, error will be null.
            JOptionPane.showMessageDialog(bookDeliveryView,
                "Deliveries are sucessfully booked",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (NonBookedDeliveryException ex) {
            JOptionPane.showMessageDialog(bookDeliveryView,
                "Error while booking deliveries: "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(bookDeliveryView,
                "Unknown error while booking deliveries",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

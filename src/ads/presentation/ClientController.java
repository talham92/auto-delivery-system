/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.NonBookedDeliveryException;
import ads.logic.ServerControllerInterface;
import ads.logic.ServerInitializedException;
import ads.logic.ServerNonInitializedException;
import ads.logic.SystemStatus;
import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import ads.resources.data.Office;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * This class contains methods to communicate with Server, change states
 * according to interaction with user. and implements functions of log-in, log-out
 * searching, booking delivery, registering, drawing floor map, show system status etc.
 * 
 * @author mgamell
 */
public class ClientController implements ClientControllerInterface {
    
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the user has not 
     * logged in.
     */
    private static final int STATE_NON_LOGGED_IN = 1;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the user is registering.
     */
    private static final int STATE_REGISTERING = 2;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the user is 
     * booking a delivery.
     */
    private static final int STATE_BOOKING = 3;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the user is editing
     * his/her user information.
     */
    private static final int STATE_EDITING = 4;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the system is displaying
     * help page to the user.
     */
    private static final int STATE_SHOWING_HELP = 5;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * The state in ClientController shows that the system is displaying
     * delivery history to the logged-in user.
     */
    private static final int STATE_VIEWING_DELIVERY_HISTORY = 6;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that administrator has
     * system is displaying main management page to the administrator.
     */
    private static final int STATE_ADMIN_MAIN = 7;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the administrator is
     * creating floor map.
     */
    private static final int STATE_ADMIN_CREATING_FLOOR_MAP = 8;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the regular user is viewing
     * detail delivery history information.
     */
    private static final int STATE_VIEWING_DELIVERY_HISTORY_DETAIL = 9;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the administrator is 
     * viewing delivery history of all users.
     */
    private static final int STATE_ADMIN_VIEWING_DELIVERY_HISTORY = 10;
    /**
     * 1 of the 11 states showing interaction status between user and system.
     * This state in ClientController shows that the administrator is viewing
     * one detailed delivery history.
     */
    private static final int STATE_ADMIN_VIEWING_DELIVERY_HISTORY_DETAIL = 11;

    /**
     * 1 of the 3 states showing user identification status.
     * This state shows that the user (no matter regular user or administrator)
     * has not input correct password.
     */
    private static final int userNotCorrect=0;
    
    /**
     * 1 of the 3 states showing user identification status.
     * This state shows that the administrator identification has completed. 
     */
    private static final int userCorrect_Admin=1;
    /**
     * 1 of the 3 states showing user identification status.
     * This state shows that the regular user's identification has completed. 
     */
    private static final int userCorrect_NotAdmin=2;
    
    //this variable stores current states of interaction between user and sytem
    private int state;
    
    //Create an object of class ClientController 
    private final ClientController controller;
    
    //input username from any user
    private String username;
    //input password from any user 
    private String password;
    
    /**
     * This method initiates the GUI and handles possible exceptions. 
     * <p>
     * This method Activate GUI to be Nimbus Look and Feel, which is drawn with
     * 2D vector graphics and can be rendered at any resolution.
     */    
    private void initGUI() { 
        try {
            //search for all possible LookAndFeel types and choose Nimbus as default
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        /*log information if there is no class named Nimbus, or there is 
          InstantiationException, IllegalAccessException, or 
          Unsupported Look and feel Exception */
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
    
    //Create an instance of ServerControllerInterface
    private ServerControllerInterface server;

    /**
     * main method in package ads.presentation.
     * <p>
     * this method will create an object of ClientController
     * and start the client window.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //String host = (args.length < 1) ? null : args[0];
        new ClientController("spring.rutgers.edu");
    }

    /**
     * Constructor of Class ClientController.
     * <p>
     * this constructor will first set the current state to STATE_NON_LOGGED_IN,
     * then pass the object to controller - private object in class ClientController.
     * Further more, it will initiate connection between the client and server.
     * It will then initiate Look and Feel to be Nimbus.and Finally, it will 
     * re-configure the state and open client window for user to input username
     * and password.
     * 
     * @param host the host name of Server,passing to method initServerConnection
     */
    private ClientController(String host) {
        //set the state as not logged in
        state = STATE_NON_LOGGED_IN;
        //set this object to private object controller for encapsulation
        controller = this;
        //initiate connection with server
        initServerConnection(host);
        //initiate GUI to be Nimbus
        initGUI();

        /* Configure first state: allow Login or Register and open client
         * window.                                                       */
        stateNonLoggedIn();

//        try {
//            server.stopServer("mgamell", "password");
//        } catch(Exception e) {}
    }

   /**
     * Initiate connection with server
     * <p>
     * this method will search for server and try to set up connection with the
     * server. Note that it will first try to search for remote server,ad then
     * try to search server locally. If there is also no server locally, the 
     * program will exit
     * 
     * @param host the host name of remote server.
     */
    private void initServerConnection(String host) {
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
     * Non-logged-in state handler
     * <p>
     * this method set the current state as STATE_NON_LOGGED_IN, and run the
     * thread of showing Login window
     */
    @Override
    public void stateNonLoggedIn() {
        // Change the state to non-logged in. We don't need to care about the
        // current state, because from every other state we can come to this 
        // state.
        state = STATE_NON_LOGGED_IN;

        // create a thread and run it to show Login window to user
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView(controller).setVisible(true);
            }
        });
    }
   
    /**
     * Non-logged-in handler that shows log-in window and close current 
     * User Main View window
     * <p>
     * this method is called when regular user in User Main View clicked the
     * "Log Out" Button. It opens the original Log-in window and close current
     * User Main View window.
     * 
     * @param viewToDispose The UserMainView Object needed to be closed
     */
    @Override
    public void stateNonLoggedIn(UserMainView viewToDispose) {
        //create and open "Log-in" window
        stateNonLoggedIn();
        //set current UserMainView window as invisible
        viewToDispose.setVisible(false);
        //free the JFrame resources
        viewToDispose.dispose();
    }

    /**
     * Non-logged-in handler that shows log-in window and close current 
     * Administrator Main View
     * <p>
     * This method is called when administrator in Admin Main View clicked 
     * the "Log out" button.It opens original Log-in window and close current
     * Administrator Main View window.
     * 
     * @param viewToDispose the parameter that indicates windows to be closed
     */
    @Override
    public void stateNonLoggedIn(AdminMainView viewToDispose) {
        //create and open "Log-in" window
        stateNonLoggedIn();
        //set current AdminMainView window as invisible
        viewToDispose.setVisible(false);
        //free the JFrame resources
        viewToDispose.dispose();
    }

    /**
     * Non-logged-in handler that shows log-in window and close current register
     * window
     * <p>
     * This method is called when "registering window" is shown and the user
     * clicked "Log out" button.It opens original Log-in window and close current
     * Registering window.
     * 
     * @param viewToDispose 
     */
    @Override
    public void stateNonLoggedIn(RegisterView viewToDispose) {
        //create and open "Log-in" window
        stateNonLoggedIn();
        //set current Registering view window as invisible
        viewToDispose.setVisible(false);
        //free the JFrame resources;
        viewToDispose.dispose();
    }

    /**
     * This method sends user registration information to the server, and 
     * handles with exception scenarios
     * <p>
     * It will send registration information to the server, and wait for server's
     * response. If resonse if null, which indicates no error happens when server
     * is receiving registration information, then the system will show 
     * booking delivery window to the user; if there is any error during registering,
     * window will show error message to the user.
     * 
     * @param firstName  string containng first name of the user
     * @param lastName   string containing last name of the user
     * @param roomNumber string containing room number of the user
     * @param email      string containing email address of the user
     * @param username   string containing username of the user
     * @param password   string containing password of the user 
     * @param password1  string containing verified password of the user
     * @param register   object of RegisterView
     */
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
        } catch (ServerNonInitializedException ex) {
            error = ex.getMessage();
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
            wantsToBookDelivery();
        } else {
            // Show an error indicating that the username or the password
            // are incorrect
            JOptionPane.showMessageDialog(register,
                error,
                "Register error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * this method handles with the case when user wants to register for a new
     * account
     * <p>
     * when user press the "register" button, the system will hide original
     * login window and show register window to the user
     * 
     * @param login object of LoginView 
     */
    @Override
    public void wantsToRegister(LoginView login) {
        // Check that we are in a correct state:
        // If we are not in STATE_NON_LOGGED_IN, system exits;
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

        // Create and display the registering window to the user;
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RegisterView(controller).setVisible(true);
            }
        });
    }

    /**
     * this method handles with username and password that the user
     * enters into the pane of client window.
     * <p>
     * ClientController sends username and password to the server side
     * and check whether they are correct. If they are checked to be incorrect,
     * show warning to the user; if they are checked to be correct and username
     * is not administrator, show the User main view window to the user; else 
     * if they are checked to be correct and username is administrator, show
     * Admin main view window to the user.
     * 
     * @param username the username string that user input into the window;
     * @param password the password string that user input into the window;
     * @param l        the object of LoginView
     */  
    @Override
    public void login(String username, String password, LoginView l) {
        /* Check that we are in a correct state: 
         * If we are not in STATE_NON_LOGGED_IN, system exit.
         */
        if(state != STATE_NON_LOGGED_IN) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "login error: state != STATE_NON_LOGGED_IN");
            System.exit(-1);
        }
        
        // Access the server to know whether the login is correct or not
        int loginCorrect = userNotCorrect;
        try {
            //server checks username and password and returns whether they are correct
            loginCorrect = server.checkLogin(username, password);
        //when server occurs severe exception, system exit.    
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        //when server is not initialized, show "Login error" to the user
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(l,
                ex.getMessage(),
                "Login error",
                JOptionPane.ERROR_MESSAGE);
        }

        // Check the answer from server
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
            // 3.a turn to admin main page, if user is administrator
            if(loginCorrect==userCorrect_Admin)
                stateAdminMain();
            // 3.b turn to user main page, if user is regular user.
            else if(loginCorrect==userCorrect_NotAdmin)
                wantsToBookDelivery();
        }
    }

    /**
     * STATE_BOOKING state handler.
     * <p>
     * This method handles the case when state changes to STATE_BOOKING. In this
     * state, the system will display UserMainView to the user.
     */
    @Override
    public void wantsToBookDelivery() {
        /*The former state can be either STATE_NON_LOGGED_IN or STATE_REGISTERING
          It means that either the user enters the current username and password,
          or finishes registering, he/she can log-in to the system and see the
          User Main View window.
         */
        if(state == STATE_NON_LOGGED_IN || state == STATE_REGISTERING) {
            // Create and display the UserMainView window
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
//                  new BookDeliveryView(controller).setVisible(true);
                    new UserMainView(controller).setVisible(true);
                }
            });
        }

        // Change the state to Booking.
        state = STATE_BOOKING;
    }
    
    /**
     * 
     * This method searches for possible users according to the characters that
     * the user inputs into the pane, no matter he is inputting into the
     * username pane or the password pane.
     * <p>
     * The ClientController will send search request to ServerController and return
     * result with possible usernames and offices. Then it will obtain full delivery
     * information of these usernames and offices into the resultString
     * 
     * @param name name characters that user inputs
     * @param office office character that user inputs
     * @return returns a list of possible searching results with full information
     */
    @Override
    public List<String[]> searchUser_NameOffice(String name, String office)
    {
        if(state != STATE_BOOKING) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "register error: state != STATE_BOOKING");
            System.exit(-1);
        }
        Set<ADSUser> result;
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.searchUser_NameOffice(this.username, this.password, name, office);
        //handle with exceptions
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        } catch (ServerNonInitializedException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        //obtain full result information into the resultString and return
        List<String[]> resultString = new ArrayList(result.size());
        for(ADSUser u : result) {
            resultString.add(new String[] {u.getUsername(), u.getFirstName()+" "+u.getLastName(), u.getOffice().getOfficeAddress()});
        }
        
        return resultString;
    }
    
    /**
     * Transfers booking delivery list to the server to finish booking.
     * 
     * @param urgency the priority of the delivery
     * @param targetList the list of booking deliveries
     * @param bookDeliveryView the object of BookDeliveryView
     */
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
        //handles with exceptions, including NonBookedDeliveryException
        //RemoteException and ServerNonInitializedException
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
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(bookDeliveryView,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method gets system status from the server and returns the status
     * 
     * @return system status
     */
    @Override
    public SystemStatus getSystemStatus() {
        try {
            return server.getSystemStatus(this.username, this.password);
        } catch (ServerNonInitializedException ex) {
            return null;
        } catch (RemoteException ex) {
            return null;
        }
    }

    /**
     * This method is a STATE_VIEWING_DELIVERY_HISTORY state handler, which 
     * shows delivery history window to the user.
     * <p> 
     * It obtains Delivery history list from server, handles with exception
     * during this period, and then draw delivey history one by one into the
     * window
     * 
     * @param view 
     * @param deliveryStatusView passing parameters to DeliveryStatusView
     */
    @Override
    public void wantsToTrackDeliveryStatus(UserMainView view, DeliveryStatusView deliveryStatusView) {
        state = STATE_VIEWING_DELIVERY_HISTORY;

        List<Delivery> result = null;
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.getUserDeliveryList(this.username, this.password);
        //handles with exceptions from server
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "Unknown error while retrieving delivery list "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(deliveryStatusView,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        //clear form drawn last time
        deliveryStatusView.reset();
        //draw delivery history information one by one
        if(result != null || !result.isEmpty()) {
            for(Delivery d : result) {
                deliveryStatusView.addDelivery(d.getId(), d.getTimestampField(), d.getSender().getUsername(), d.getReceiver().getUsername(), d.getPriority());
            }
        } else {
            //show warining that user has no delivery history
            JOptionPane.showMessageDialog(deliveryStatusView,
                "You haven't book any delivery yet.",
                "No deliveries",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This method shows details of delivery history into the window.
     * <p>
     * ClientController will get delivery detail history from server, and output
     * them into detailed delivery status window.
     * 
     * @param deliveryStatusView
     * @param deliveryId 
     */
    @Override
    public void wantsToSeeDeliveryDetails(DeliveryStatusView deliveryStatusView, int deliveryId) {
        if(state == STATE_VIEWING_DELIVERY_HISTORY || state == STATE_VIEWING_DELIVERY_HISTORY_DETAIL) {
            state = STATE_VIEWING_DELIVERY_HISTORY_DETAIL;
        } else if(state == STATE_ADMIN_VIEWING_DELIVERY_HISTORY || state == STATE_ADMIN_VIEWING_DELIVERY_HISTORY_DETAIL) {
            state = STATE_ADMIN_VIEWING_DELIVERY_HISTORY_DETAIL;
        } else {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "State incorrect!! wantsToSeeDeliveryDetails");
            System.exit(-1);
        }

        List<String[]> result = null;
        deliveryStatusView.resetDeliveryDetailsTable();
        deliveryStatusView.showDeliveryDetails(deliveryId);
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.getDeliveryDetails(this.username, this.password, deliveryId);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "Unknown error while retrieving delivery details "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (ServerNonInitializedException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(deliveryStatusView,
                "Unknown error while retrieving delivery details "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        //output delivery detail information into the deliverydetail window
        if(result != null) {
            for(String[] s : result) {
                deliveryStatusView.addDeliveryDetail(s);
            }
        //show message if none is available.
        } else {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "The delivery have no details!",
                "No details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * This method is the handler of state STATE_ADMIN_VIEWING_DELIVERY_HISTORY
     * <p>
     * This method first obtain delivery list from server, then show delivery
     * list into the Deliverylist window.
     * 
     * @param view                  N/A
     * @param deliveryStatusView    show message and output delivery info on this parameter
     */
    @Override
    public void wantsToTrackDeliveryStatusAdmin(SystemStatusView view, DeliveryStatusView deliveryStatusView) {
        state = STATE_ADMIN_VIEWING_DELIVERY_HISTORY;

        List<Delivery> result = null;
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.getDeliveryList(this.username, this.password);
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "Unknown error while retrieving delivery list "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(deliveryStatusView,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        //reset delivery status window
        deliveryStatusView.reset();
        //output delivery information to the delivery status window
        if(result != null || !result.isEmpty()) {
            for(Delivery d : result) {
                deliveryStatusView.addDelivery(d.getId(), d.getTimestampField(), d.getSender().getUsername(), d.getReceiver().getUsername(), d.getPriority());
            }
        } else {
            //show message when no delivery history is available
            JOptionPane.showMessageDialog(deliveryStatusView,
                "There are no deliveries yet.",
                "No deliveries",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * create floor map according to the text input by administrator
     * 
     * @param text the text input by admin
     * @param adminCreateFloorMap the object of AdminCreateFloorMapView
     */
    @Override
    public void createFloorMap(String text, AdminCreateFloorMapView adminCreateFloorMap) {
        //Clear offices before creating a new map to avoid the conflicts and inconsistencies
        clearOffices(adminCreateFloorMap);
        //
        String[] sTokens;
        String[] eTokens;
        String[] adjTokens;
        String nodeName, preNodeName, preNodeDir, preNodeDist,
                         nextNodeName, nextNodeDir, nextNodeDist,
                         endLine, line, adjNodePart;
        // Extracting the nodes and the relations and distances from the text
        StringTokenizer st = new StringTokenizer(text);
        String[] lineTokens=text.split("\\n");
        for(int i=0; i<lineTokens.length; i++) // we are tracing the lines
        {
            line=lineTokens[i];
            sTokens=line.split("/");
            nodeName=sTokens[0];
            endLine=sTokens[1];
            if(i==0) //if the related node is 'start'
            {
                //Check whether the entered start line format is correct
                if(!checkMapLineFormat("startLine", line))
                    throw new RuntimeException();
                preNodeName=null;
                preNodeDir=null;
                preNodeDist=null;
                
                adjTokens=endLine.split(",");
                nextNodeName=adjTokens[0];
                nextNodeDir=adjTokens[1];
                nextNodeDist=adjTokens[2];
            }
            else if(i==lineTokens.length-1)//if the related node is 'end'
            {
                //Check whether the entered end line format is correct
                if(!checkMapLineFormat("endLine", line))
                    throw new RuntimeException();
                adjTokens=endLine.split(",");
                preNodeName=adjTokens[0];
                preNodeDir=adjTokens[1];
                preNodeDist=adjTokens[2];
                
                nextNodeName=null;
                nextNodeDir=null;
                nextNodeDist=null;
            }
            else //if the line is a middle line
            {
                //Check whether the entered mid line format is correct
                if(!checkMapLineFormat("midLine", line))
                    throw new RuntimeException();
                
                eTokens=endLine.split("--");
                // for the previous node; first one in the end line
                adjNodePart=eTokens[0];
                adjTokens=adjNodePart.split(",");
                
                preNodeName=adjTokens[0];
                preNodeDir=adjTokens[1];
                preNodeDist=adjTokens[2];
                // for the next node; second one in the end line
                adjNodePart=eTokens[1];
                adjTokens=adjNodePart.split(",");
                nextNodeName=adjTokens[0];
                nextNodeDir=adjTokens[1];
                nextNodeDist=adjTokens[2];
            }
            //Create the corresponding office
            // We have everything to create the offices in the database
            //In this part only the addresses of the adjacent nodes will be assigned
            //Next time there is another to-be-implementd function called to make the links btw the offices
            try {
                //Call related server function to persist the office object
                server.createOffice(new Office(nodeName, nextNodeName, preNodeName,
                                    preNodeDir, preNodeDist, nextNodeDir, nextNodeDist));
            } catch (RemoteException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ServerInitializedException ex) {
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //After creating the map, make the links btw the offices
        createLinksBtwOffices();
    }

    /**
     * communicate with server to delete current generated map.
     * 
     * @param v object of AdminCreateFloorMapView
     */
    @Override
    public void clearOffices(AdminCreateFloorMapView v) {
        try {
            String r;
            r = server.clearOffices();
            if(r.equals("preCreatedMapDeleted"))
                v.getOutputText().append("There already created map and it is cleared\n");
            else if(r.equals("noPreCreatedMap"))
                v.getOutputText().append("There was no already created map and new map is successfully created\n");
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServerInitializedException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * check whether the format of characters entered by admin is correct.
     * 
     * @param whichLine the start line  of text entered by admin
     * @param line 
     * @return  whether the format is correct
     */
    public boolean checkMapLineFormat(String whichLine, String line)
    {   // Simple check it needs to be improced but may be later if we have enough time !!!
        //Check the dist,dir regions 
        //Check whether multiple offices with the same name is entered
        String[] lineTokens;
        String[] eTokens;
        lineTokens=line.split("/");
        eTokens=lineTokens[1].split("--");
        if(lineTokens.length!=2)
            return false;
        else if((whichLine.equals("startLine") || whichLine.equals("endLine")) && eTokens.length!=1)
            return false;
        else if(whichLine.equals("midLine") && eTokens.length!=2)
            return false;
        else
            return true;
    }

    /**
     * This method creates links between offices nodes generated by the system
     */
    @Override
    public void createLinksBtwOffices() {
        try {
                server.createLinksBtwOffices();
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServerInitializedException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This methods returns map drawing array to the AdminCreateFloorMapView
     * to draw nodes and lines into the window
     * 
     * @return 
     */
    @Override
    public ArrayList<String[]> getMapDrawingArray() {
        try {
            return server.getMapDrawingArray();
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
//
//    @Override
//    public void wantsToLookAtDynamicViewOfMap(AdminMainView aThis) {
//        // set the admin main view invisible and dispose it
//        aThis.setVisible(false);
//        aThis.dispose();
//        
//        stateAdminViewDynamicMap();
//    }

//    private void stateAdminViewDynamicMap() {
//        if(state != STATE_ADMIN_MAIN) {
//            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "view dynamic MAP error: state != STATE_ADMIN_MAIN");
//            System.exit(-1);
//        }
//        state = STATE_ADMIN_VIEW_DYNAMIC_MAP;
//        // Create and display the form
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new AdminViewDynamicMap(controller).setVisible(true);
//            }
//        });
//    }
    
    /**
     * state handler of STATE_ADMIN_CREATING_FLOOR_MAP
     * <p>
     * This method will change state into STATE_ADMIN_CREATING_FLOOR_MAP
     * and create and show window of floor map.
     * 
     */
    private void stateAdminCreateFloorMap(){
        if(state != STATE_ADMIN_MAIN) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, "create MAP error: state != STATE_ADMIN_MAIN");
            System.exit(-1);
        }
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

    /**
     * This method implements the function when admin wants to create floor map
     * 
     * @param v object of AdminMainView
     */
    @Override
    public void wantsToCreateFloorMap(AdminMainView v){
        // set the admin main view invisible and dispose it
        v.setVisible(false);
        v.dispose();
        
        stateAdminCreateFloorMap();
    }
    
    /**
     * STATE_ADMIN_MAIN state handler.
     * <p>
     * This method change current state into STATE_ADMIN_MAIN, and create and
     * open AdminMainView window.
     */
    @Override
    public void stateAdminMain(){
        //change current state into STATE_ADMIN_MAIN.
        state=STATE_ADMIN_MAIN;
        //Create the AdminMainView window and show it to the administrator
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminMainView(controller).setVisible(true);
            }
        });
    }

    /**
     * Administrator initializes the server with testing data.
     * 
     * @param view object of AdminMainView
     */
    @Override
    public void initializeWithTestingData(AdminMainView view) {
        try {
            server.initializeWithTestingData();
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (ServerInitializedException ex) {
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        //If server is initialized, set admin main view with appearance that
        //shows server has already initialized
        //If server is not initialized, set admin main view with the appearance
        //that server is not initialized
        if(getSystemStatus().isServerInitialized()) {
            view.setInitializedAppearance();
        } else {
            view.setNonInitializedAppearance();
        }

    }

    /**
     * The clientcontroller tries to communicate with server and ask the 
     * server to initialize the system.
     * 
     * @param view 
     */
    @Override
    public void initializeSystem(AdminMainView view) {
        try {
            server.initializeSystem();
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (ServerInitializedException ex) {
            JOptionPane.showMessageDialog(null,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        //keep che appearance uniform with actual status regarding
        //whether the system has been initiated
        if(getSystemStatus().isServerInitialized()) {
            view.setInitializedAppearance();
        } else {
            view.setNonInitializedAppearance();
        }
    }

}

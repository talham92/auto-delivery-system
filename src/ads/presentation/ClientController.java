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
 *
 * @author mgamell
 */
public class ClientController implements ClientControllerInterface {
    private static final int STATE_NON_LOGGED_IN = 1;
    private static final int STATE_REGISTERING = 2;
    private static final int STATE_BOOKING = 3;
    private static final int STATE_EDITING = 4;
    private static final int STATE_SHOWING_HELP = 5;
    private static final int STATE_VIEWING_DELIVERY_HISTORY = 6;
    private static final int STATE_ADMIN_MAIN = 7;
    private static final int STATE_ADMIN_CREATING_FLOOR_MAP = 8;
    private static final int STATE_VIEWING_DELIVERY_HISTORY_DETAIL = 9;
    private static final int STATE_ADMIN_VIEWING_DELIVERY_HISTORY = 10;
    private static final int STATE_ADMIN_VIEWING_DELIVERY_HISTORY_DETAIL = 11;

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
        //String host = (args.length < 1) ? null : args[0];
        new ClientController("spring.rutgers.edu");
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
            Registry registry = LocateRegistry.getRegistry();
            server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
        } catch (Exception e) {
            // try remote repository
            try {
                Registry registry = LocateRegistry.getRegistry(host);
                server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null,
                    "Unable to connect to server (nor local or remote). Exiting...",
                    "Register error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
    }

    @Override
    public void stateNonLoggedIn() {
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
    public void stateNonLoggedIn(UserMainView viewToDispose) {
        stateNonLoggedIn();
        viewToDispose.setVisible(false);
        viewToDispose.dispose();
    }

    @Override
    public void stateNonLoggedIn(AdminMainView viewToDispose) {
        stateNonLoggedIn();
        viewToDispose.setVisible(false);
        viewToDispose.dispose();
    }

    @Override
    public void stateNonLoggedIn(RegisterView viewToDispose) {
        stateNonLoggedIn();
        viewToDispose.setVisible(false);
        viewToDispose.dispose();
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
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(l,
                ex.getMessage(),
                "Login error",
                JOptionPane.ERROR_MESSAGE);
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
                wantsToBookDelivery();
        }
    }

    
    @Override
    public void wantsToBookDelivery() {
        if(state == STATE_NON_LOGGED_IN || state == STATE_REGISTERING) {
            // Create and display the form
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
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        } catch (ServerNonInitializedException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            result = null;
        }

        List<String[]> resultString = new ArrayList(result.size());
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
        } catch (ServerNonInitializedException ex) {
            JOptionPane.showMessageDialog(bookDeliveryView,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

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

    @Override
    public void wantsToTrackDeliveryStatus(UserMainView view, DeliveryStatusView deliveryStatusView) {
        state = STATE_VIEWING_DELIVERY_HISTORY;

        List<Delivery> result = null;
        try {
            //This method returns the receiver Name and Address by using name and office of the user
            result = server.getUserDeliveryList(this.username, this.password);
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
        deliveryStatusView.reset();
        if(result != null || !result.isEmpty()) {
            for(Delivery d : result) {
                deliveryStatusView.addDelivery(d.getId(), d.getTimestampField(), d.getSender().getUsername(), d.getReceiver().getUsername(), d.getPriority());
            }
        } else {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "You haven't book any delivery yet.",
                "No deliveries",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

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
        if(result != null) {
            for(String[] s : result) {
                deliveryStatusView.addDeliveryDetail(s);
            }
        } else {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "The delivery have no details!",
                "No details",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

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
        deliveryStatusView.reset();
        if(result != null || !result.isEmpty()) {
            for(Delivery d : result) {
                deliveryStatusView.addDelivery(d.getId(), d.getTimestampField(), d.getSender().getUsername(), d.getReceiver().getUsername(), d.getPriority());
            }
        } else {
            JOptionPane.showMessageDialog(deliveryStatusView,
                "There are no deliveries yet.",
                "No deliveries",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

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

    @Override
    public void wantsToCreateFloorMap(AdminMainView v){
        // set the admin main view invisible and dispose it
        v.setVisible(false);
        v.dispose();
        
        stateAdminCreateFloorMap();
    }
    
    @Override
    public void stateAdminMain(){
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
        
        if(getSystemStatus().isServerInitialized()) {
            view.setInitializedAppearance();
        } else {
            view.setNonInitializedAppearance();
        }

    }

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
        
        if(getSystemStatus().isServerInitialized()) {
            view.setInitializedAppearance();
        } else {
            view.setNonInitializedAppearance();
        }
    }

}

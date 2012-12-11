/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;
import ads.logic.SystemStatus;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface contains methods to communicate with Server, change states
 * according to interaction with user. and implements functions of log-in, log-out
 * searching, booking delivery, registering, drawing floor map, show system status etc.
 * 
 * @author mgamell
 */
interface ClientControllerInterface {
    /**
     * Non-logged-in state handler
     * <p>
     * this method set the current state as STATE_NON_LOGGED_IN, and run the
     * thread of showing Login window
     */
    public void stateNonLoggedIn();
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
    public void stateNonLoggedIn(UserMainView viewToDispose);
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
    public void stateNonLoggedIn(AdminMainView viewToDispose);
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
    public void stateNonLoggedIn(RegisterView viewToDispose);
    
    public void login(String username, String password, LoginView login);
    public void wantsToRegister(LoginView login);
    public void register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1, RegisterView register);
    
    public void wantsToBookDelivery();
    public List<String[]> searchUser_NameOffice(String name, String office);
    public void bookDelivery(double urgency, List<String> targetList, BookDeliveryView bookDeliveryView);
    public void wantsToCreateFloorMap(AdminMainView v);

    public SystemStatus getSystemStatus();
    public void wantsToTrackDeliveryStatus(UserMainView view, DeliveryStatusView deliveryStatusView);
    public void wantsToSeeDeliveryDetails(DeliveryStatusView deliveryStatusView, int deliveryId);
    public void wantsToTrackDeliveryStatusAdmin(SystemStatusView view, DeliveryStatusView deliveryStatusView);

    public void createFloorMap(String text, AdminCreateFloorMapView v);
    public void clearOffices(AdminCreateFloorMapView v);
    public void createLinksBtwOffices();
    public ArrayList<String[]> getMapDrawingArray();
    public void stateAdminMain();

//    public void wantsToLookAtDynamicViewOfMap(AdminMainView aThis);

    public void initializeWithTestingData(AdminMainView view);

    public void initializeSystem(AdminMainView view);
}

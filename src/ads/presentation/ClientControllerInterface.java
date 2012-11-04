/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mgamell
 */
interface ClientControllerInterface {
    public void login(String username, String password, LoginView login);
    public void wantsToRegister(LoginView login);
    public void register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1, RegisterView register);
    
    public List<String[]> searchUser_NameOffice(String name, String office);
    public void bookDelivery(String urgency, List<String> targetList, BookDeliveryView bookDeliveryView);
    public void wantsToCreateFloorMap(AdminMainView v);

    public void createFloorMap(String text, AdminCreateFloorMapView v);
    public void clearOffices(AdminCreateFloorMapView v);
    public void createLinksBtwOffices();
    public ArrayList<String[]> getMapDrawingArray();
    public void stateAdminMain();

    public void wantsToLookAtDynamicViewOfMap(AdminMainView aThis);
}

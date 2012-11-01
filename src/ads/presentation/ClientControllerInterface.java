/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.SystemStatus;
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
    
    public void wantsToBookDelivery();
    public List<String[]> searchUser_NameOffice(String name, String office);
    public void bookDelivery(double urgency, List<String> targetList, BookDeliveryView bookDeliveryView);
    public void wantsToCreateFloorMap(AdminMainView v);

    public SystemStatus getSystemStatus() throws Exception ;
    public void wantsToTrackDeliveryStatus(UserMainView view, DeliveryStatusView deliveryStatusView);
    public void wantsToSeeDeliveryDetails(DeliveryStatusView deliveryStatusView, int deliveryId);
}

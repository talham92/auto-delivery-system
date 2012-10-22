/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author mgamell
 */
interface ClientControllerInterface {
    public void login(String username, String password, LoginView login);
    public void wantsToRegister(LoginView login);
    public void register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1, RegisterView register);
    
    public String[] searchUser_NameOffice(String name, String office);
    public void bookDelivery(String urgency, ArrayList<String[]> targetList, BookDeliveryView bookDeliveryView);
}

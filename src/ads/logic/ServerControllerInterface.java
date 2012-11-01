/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mgamell
 */
public interface ServerControllerInterface extends Remote {
    public int checkLogin(String username, String password) throws RemoteException;
    public void stopServer(String username, String password) throws RemoteException;
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws RemoteException;
    
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws RemoteException;
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException;

    public SystemStatus getSystemStatus(String username, String password) throws RemoteException;
    public List<Delivery> getUserDeliveryList(String username, String password) throws RemoteException;
    public List<String[]> getUserDeliveryDetails(String username, String password, int deliveryId) throws RemoteException;
}

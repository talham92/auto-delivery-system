/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import ads.resources.data.Office;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mgamell
 */
public interface ServerControllerInterface extends Remote {
    public void createOffice(Office office)throws RemoteException, ServerInitializedException;
    public String clearOffices() throws RemoteException, ServerInitializedException;
    public void createLinksBtwOffices() throws RemoteException, ServerInitializedException;

    
    public void initializeWithTestingData() throws RemoteException, ServerInitializedException;
    public void initializeSystem() throws RemoteException, ServerInitializedException;
    
    
    public int checkLogin(String username, String password) throws RemoteException, ServerNonInitializedException;
    public void stopServer(String username, String password) throws RemoteException, ServerNonInitializedException;
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws RemoteException, ServerNonInitializedException;
    
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws RemoteException, ServerNonInitializedException;
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException, ServerNonInitializedException;

    public SystemStatus getSystemStatus(String username, String password) throws RemoteException, ServerNonInitializedException;
    public List<Delivery> getUserDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException;
    public List<String[]> getUserDeliveryDetails(String username, String password, int deliveryId) throws RemoteException, ServerNonInitializedException;

    public ArrayList<String[]> getMapDrawingArray()throws RemoteException, ServerNonInitializedException;
}

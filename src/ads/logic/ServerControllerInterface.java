/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mgamell
 */
public interface ServerControllerInterface extends Remote {
    public boolean checkLogin(String username, String password) throws RemoteException;
    public void stopServer(String username, String password) throws RemoteException;
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws RemoteException;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStubStatus;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mgamell
 */
public interface RobotStubInterface extends Remote {
    public RobotStubStatus getRobotStubStatus() throws RemoteException;
    public void answer(String password) throws RemoteException;
    public void answer() throws RemoteException;
}

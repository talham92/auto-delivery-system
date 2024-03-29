/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStubStatus;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**Define the function call of the RobotStub
 *
 * @author mgamell
 */
public interface RobotStubInterface extends Remote {
    /**
     *
     * @return
     * @throws RemoteException
     */
    public RobotStubStatus getRobotStubStatus() throws RemoteException;
    /**
     *
     * @param password
     * @throws RemoteException
     */
    public void answer(String password) throws RemoteException;
    /**
     *
     * @throws RemoteException
     */
    public void answer() throws RemoteException;
}

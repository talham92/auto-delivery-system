/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStub;
import adsrobotstub.RobotStubStatus;
import java.net.SocketTimeoutException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgamell
 */
public class ServerCommunicatorReal implements ServerCommunicator {
    //create a new object of ServerCommunicator
    private static ServerCommunicatorReal singleton = new ServerCommunicatorReal();
    //Define several private variables
    private static Logger logger = Logger.getLogger(ServerCommunicatorStub.class.getName());
    private static RobotSocketServer robotSocketServer;
    private boolean initiated = false;


    public void init() {
        robotSocketServer = new RobotSocketServer();
        initiated = true;
    }

    public void moveRobotToNextPoint() {
        while(!initiated) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
        
        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse(true, "move robot to next point");
            if(!response.equals("next point found")) {
                System.err.println("moveRobotToNextPoint RECEIVED MESSAGE DIFFERENT THAN \"next point found\": "+response);
                System.exit(1);
            }
            System.out.println("Robot arrived to the next point");
        } catch (SocketTimeoutException ex) {
            System.err.println("moveRobotToNextPoint EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public String waitForPassword(int timeout, String username) throws TimeoutException {
        return "a";
    }
    public void ringBuzzer() {
    }
    public void showPasswordIncorrectError() {
    }
    public void showPasswordIncorrectWarning() {
    }
    public void showPasswordCorrectMessage() {
    }
    public void openTray(int trayNum) {
    }
}

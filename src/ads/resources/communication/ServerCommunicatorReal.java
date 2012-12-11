/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStub;
import adsrobotstub.RobotStubStatus;
import java.io.IOException;
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
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}
        
        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("move robot to next point");
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

    public void ringBuzzer() {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("ringBuzzer");
            if(!response.equals("ringBuzzer done")) {
                System.err.println("ringBuzzer RECEIVED MESSAGE DIFFERENT THAN \"ringBuzzer done\": "+response);
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("ringBuzzer EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public void showPasswordIncorrectError() {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("showPasswordIncorrectError");
            if(!response.equals("showPasswordIncorrectError done")) {
                System.err.println("showPasswordIncorrectError RECEIVED MESSAGE DIFFERENT THAN \"showPasswordIncorrectError done\": "+response);
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("showPasswordIncorrectError EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public void showPasswordIncorrectWarning() {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("showPasswordIncorrectWarning");
            if(!response.equals("showPasswordIncorrectWarning done")) {
                System.err.println("showPasswordIncorrectWarning RECEIVED MESSAGE DIFFERENT THAN \"showPasswordIncorrectWarning done\": "+response);
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("showPasswordIncorrectWarning EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    public void showPasswordCorrectMessage() {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("showPasswordCorrectMessage");
            if(!response.equals("showPasswordCorrectMessage done")) {
                System.err.println("showPasswordCorrectMessage RECEIVED MESSAGE DIFFERENT THAN \"showPasswordCorrectMessage done\": "+response);
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("showPasswordCorrectMessage EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    
    
    public void openTray(int trayNum) {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("openTray "+trayNum);
            if(!response.startsWith("closedTray")) {
                System.err.println("openTray RECEIVED MESSAGE DIFFERENT THAN \"closedTray\": "+response);
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("openTray EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    
    public String waitForPassword(int timeout, String username) throws TimeoutException {
        while(!initiated) {try{Thread.sleep(100);}catch(InterruptedException ex){}}

        String password = "";
        try {
            // send a message to the robot
            String response = robotSocketServer.sendMessageAndWaitForResponse("waitForPassword");
            if(!response.startsWith("password is ")) {
                System.err.println("waitForPassword RECEIVED MESSAGE DIFFERENT THAN \"password is ...\": "+response);
                System.exit(1);
            }
            password = response.substring(12, response.length());
            System.out.println("Received: "+response+"   Password is "+password);
        } catch (SocketTimeoutException ex) {
            System.err.println("waitForPassword EXCEPTION!");
            ex.printStackTrace();
            System.exit(1);
        }
        return password;
    }
}

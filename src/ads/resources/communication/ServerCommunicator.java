/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStubStatus;
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
public class ServerCommunicator implements RobotStubInterface {
    private static ServerCommunicator singleton = new ServerCommunicator();
    private static RobotStubStatus status;
    private static String passwordReceived;
    private static boolean answered;
    private static Semaphore semaphore;

    public static void init() {
        status = new RobotStubStatus();
        semaphore = new Semaphore(1, true);
        passwordReceived = null;
        answered = false;
        initRMI();
    }
    
    private static void initRMI() {
        try {
            ServerCommunicator obj = singleton;
            RobotStubInterface stub = (RobotStubInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry("spring.rutgers.edu");
            try {
                registry.unbind("RobotStubInterface");
            } catch (Exception ex) {}

            try {
                registry.bind("RobotStubInterface", stub);
            } catch (Exception ex) {
                // check local registry
                registry = LocateRegistry.getRegistry();
                try {
                    registry.unbind("RobotStubInterface");
                } catch (NotBoundException ex1) {}
                registry.bind("RobotStubInterface", stub);
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    public static void moveRobotToNextPoint() {
        try {
            semaphore.acquire();
            status.isMoving = true;
            semaphore.release();
            Thread.sleep(1500);
            semaphore.acquire();
            status.isMoving = false;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String waitForPassword(int timeout, String username) throws TimeoutException {
        try {
            semaphore.acquire();
            status.isRequestingPassword = true;
            status.isPasswordCorrect = false;
            status.isPasswordWarning = false;
            status.isPasswordError = false;
            status.passwordUsername = username;
            passwordReceived = null;
            semaphore.release();

            while(true) {
                Thread.sleep(100);
                semaphore.acquire();
                System.out.println("passwordReceived "+passwordReceived);
                if(passwordReceived != null) {
                    String tmp = passwordReceived;
                    status.isRequestingPassword = false;
                    status.isPasswordCorrect = false;
                    status.isPasswordWarning = false;
                    status.isPasswordError = false;
                    status.passwordUsername = null;
                    passwordReceived = null;
                    semaphore.release();
                    return tmp;
                } else {
                    semaphore.release();
                    continue;
                    //TODO: IMPORTANT: IMPLEMENT TIMEOUT throw new TimeoutException("Timeout "+timeout+" passed.");
                }
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            throw new TimeoutException(ex.getMessage());
        }
    }

    public static void ringBuzzer() {
        try {
            System.out.println("ringBuzzer");
            semaphore.acquire();
            status.isBuzzerRinging = true;
            semaphore.release();
            Thread.sleep(1);
            semaphore.acquire();
            status.isBuzzerRinging = false;
            semaphore.release();
            System.out.println("ringBuzzer end");
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showPasswordIncorrectError() {
        try {
            System.out.println("showPasswordIncorrectError");
            semaphore.acquire();
            status.isPasswordError = true;
            answered = false;
            semaphore.release();

            while(true) {
                Thread.sleep(100);
                semaphore.acquire();
                if(answered) {
                    status.isPasswordError = false;
                    answered = false;
                    semaphore.release();
                    return;
                } else {
                    semaphore.release();
                    continue;
                }
            }
        
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showPasswordIncorrectWarning() {
        try {
            semaphore.acquire();
            status.isPasswordWarning = true;
            answered = false;
            semaphore.release();

            while(true) {
                Thread.sleep(100);
                semaphore.acquire();
                if(answered) {
                    status.isPasswordWarning = false;
                    answered = false;
                    semaphore.release();
                    return;
                } else {
                    semaphore.release();
                    continue;
                }
            }
        
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showPasswordCorrectMessage() {
        try {
            semaphore.acquire();
            status.isPasswordCorrect = true;
            answered = false;
            semaphore.release();

            while(true) {
                Thread.sleep(100);
                semaphore.acquire();
                if(answered) {
                    status.isPasswordCorrect = false;
                    answered = false;
                    semaphore.release();
                    return;
                } else {
                    semaphore.release();
                    continue;
                }
            }
        
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void openTray(int trayNum) {
        try {
            semaphore.acquire();
            status.trayOpen = trayNum;
            answered = false;
            semaphore.release();

            while(true) {
                Thread.sleep(100);
                semaphore.acquire();
                if(answered) {
                    status.trayOpen = -1;
                    answered = false;
                    semaphore.release();
                    return;
                } else {
                    semaphore.release();
                    continue;
                }
            }
        
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public RobotStubStatus getRobotStubStatus() throws RemoteException {
        try {
            semaphore.acquire();
            RobotStubStatus statustmp = new RobotStubStatus(status);
            semaphore.release();
            return statustmp;
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
        
    }

    @Override
    public void answer(String password) throws RemoteException {
        try {
            semaphore.acquire();
            passwordReceived = password;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
    }

    @Override
    public void answer() throws RemoteException {
        try {
            semaphore.acquire();
            answered = true;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicator.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
    }
    
}

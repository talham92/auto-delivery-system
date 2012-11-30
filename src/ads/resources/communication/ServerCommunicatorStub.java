/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import adsrobotstub.RobotStub;
import adsrobotstub.RobotStubStatus;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Define the robot status and association with system 
 *
 * @author mgamell
 */
public class ServerCommunicatorStub implements RobotStubInterface, ServerCommunicator {
    //create a new object of ServerCommunicator
    private static ServerCommunicatorStub singleton = new ServerCommunicatorStub();
    //Define several private variables
    private static RobotStubStatus status;
    private static String passwordReceived;
    private static boolean answered;
    private static Semaphore semaphore;
    private static Logger logger = Logger.getLogger(ServerCommunicatorStub.class.getName());
    private static RobotSocketServer robotSocketServer;
    /**
     * To initialize
     */
    public void init() {
        robotSocketServer = new RobotSocketServer();

        
        status = new RobotStubStatus();
        //Aquire a permit from the semaphore, blocking until one is available
        semaphore = new Semaphore(1, true);
        passwordReceived = null;
        answered = false;
        initRMI();
    }
    /**
     * To initialize the remote method interface
     * @exception Exception if the server is not connected
     */
    private static void initRMI() {
        try {
            ServerCommunicatorStub obj = singleton;
            RobotStubInterface stub = (RobotStubInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("RobotStubInterface");
            } catch (Exception ex) {}

            registry.bind("RobotStubInterface", stub);
        } catch (Exception e) {
            logger.severe("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }
    /**
     * When semaphore acquires and shows logically true, current execution thread
     * release and sleep, else the current execution release
     */
    public void moveRobotToNextPoint() {
        try {
            semaphore.acquire();
            status.isMoving = true;
            semaphore.release();
            Thread.sleep(1500);
            semaphore.acquire();
            status.isMoving = false;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Define a string waitForPassword
     * @param timeout
     * @param username
     * @return tmp
     * @throws TimeoutException if there is problem of time out 
     */
    public String waitForPassword(int timeout, String username) throws TimeoutException {
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
                logger.finest("passwordReceived "+passwordReceived);
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
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
            throw new TimeoutException(ex.getMessage());
        }
    }
    /**
     * Define the event of ringBuzzer
     */
    public void ringBuzzer() {
        try {
            logger.finest("ringBuzzer");
            semaphore.acquire();
            status.isBuzzerRinging = true;
            semaphore.release();
            Thread.sleep(1);
            semaphore.acquire();
            status.isBuzzerRinging = false;
            semaphore.release();
            logger.finest("ringBuzzer end");
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * To judge and if true, show the error of incorrect password
     */
    public void showPasswordIncorrectError() {
        try {
            logger.finest("showPasswordIncorrectError");
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
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * To judge and if true, show the error of incorrect password
     */
    public void showPasswordIncorrectWarning() {
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
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * To judge and if true, continue current status
     */
    public void showPasswordCorrectMessage() {
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
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * To judge and if true, open the tray
     * @param trayNum 
     */
    public void openTray(int trayNum) {
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
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public RobotStubStatus getRobotStubStatus() throws RemoteException {
        try {
            semaphore.acquire();
            RobotStubStatus statustmp = new RobotStubStatus(status);
            semaphore.release();
            return statustmp;
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
        
    }
    /**
     * To get the answer value with string password input
     * @param password
     * @throws RemoteException if there is no specified message
     */
    @Override
    public void answer(String password) throws RemoteException {
        try {
            semaphore.acquire();
            passwordReceived = password;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
    }
    /**
     * Acquire permit from semaphore and check the boolean of answer
     * @throws RemoteException if the Remote Server is not connected
     */
    @Override
    public void answer() throws RemoteException {
        try {
            semaphore.acquire();
            answered = true;
            semaphore.release();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerCommunicatorStub.class.getName()).log(Level.SEVERE, null, ex);
            throw new RemoteException(ex.toString());
        }
    }
    
}

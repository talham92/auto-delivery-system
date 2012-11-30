/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgamell
 */
public class RobotSocketServer {
    public static void main(String[] args) {
        new RobotSocketServer();
    }
    
    // Note: do not access to this 3 variables outside init, sendMessage and waitForResponseMessage!
    PrintWriter out;
    BufferedReader in;
    private static Semaphore semaphore;

    
    public RobotSocketServer() {
        init();
    }
    
    private void init() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10000.");
            System.exit(1);
        }

        System.out.println("Server listening!");

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        try {
            clientSocket.setSoTimeout(15000);
        } catch (SocketException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        System.out.println("Connection established!");

        //Aquire a permit from the semaphore, blocking until one is available
        semaphore = new Semaphore(1, true);

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(
                                    new InputStreamReader(
                                    clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Handshake
        System.out.println("Sending hello");
        System.out.println("Waiting for hello from client");
        String inputLine;
        try {
            inputLine = sendMessageAndWaitForResponse("Hello from server");
            if (!inputLine.startsWith("Hello from client")) {
                System.err.println("Message received different than hello from client!");
                System.exit(1);
            }
        } catch (SocketTimeoutException ex) {
            System.err.println("Did not receive any response from client!");
            System.exit(1);
        }
        System.out.println("Handshake finished correctly!");

        // Initiate alive check thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {}

                    try {
                        System.out.println("Checking if robot is alive");
                        String r = sendMessageAndWaitForResponse("alive?");
                        if(r.startsWith("I am alive")) {
                            System.out.println("Robot is alive!");
                        } else {
                            System.err.println("Robot is not alive (1)!");
                            System.exit(-1);
                        }
                    } catch (SocketTimeoutException ex) {
                        System.err.println("Robot is not alive (2)!");
                        System.exit(-1);
                    }
                }
            }
        }).start();
    }

    private String sendMessageAndWaitForResponse(String msg) throws SocketTimeoutException {
        String msgR = null;
        try {
            semaphore.acquire();
            out.println(msg);
            msgR = in.readLine();
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketTimeoutException ex) {
            throw new SocketTimeoutException("SocketTimeoutException");
        } catch (IOException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return msgR;
    }

    /*
    private void sendMessage(String msg) {
        try {
            semaphore.acquire();
            out.println(msg);
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
    }

    private String waitForResponseMessage() throws SocketTimeoutException {
        String msg = null;
        try {
            semaphore.acquire();
            msg = in.readLine();
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SocketTimeoutException ex) {
            throw new SocketTimeoutException("SocketTimeoutException");
        } catch (IOException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            semaphore.release();
        }
        return msg;
    }
    */
                
            
            
            
            
        /*
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received message: "+inputLine);
             outputLine = "\"" + inputLine + "\" received!!";
             out.println(outputLine);
             if (inputLine.startsWith("Bye."))
                break;
        }
        */
}

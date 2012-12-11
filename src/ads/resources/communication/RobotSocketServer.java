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
    private Semaphore semaphore;
    private ServerSocket serverSocket;
    
    public RobotSocketServer() {
        init();
    }
    
    private void init() {
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10000.");
            System.exit(1);
        }

        System.out.println("Server listening!");

        semaphore = new Semaphore(1, true);
        
/*        // Handshake
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
*/
        // Initiate alive check thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {}
/*
                    try {
                        sendMessage("alive?");
                    } catch (SocketTimeoutException ex) {
                        System.err.println("Robot is not alive (2)!");
                        System.exit(-1);
                    } catch (IOException ex) {
                        Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
*/
                }
            }
        }).start();
    }

    public String sendMessageAndWaitForResponse(String msg) throws SocketTimeoutException {
        String msgR = null;
        try {
            semaphore.acquire();
            sendMessage(msg);
            msgR = waitForResponseMessage();
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

    public void sendMessage(String msg) throws IOException {
        Socket socket = null;
        PrintWriter out = null;

        socket = new Socket("192.168.10.102", 10001);
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Sending message: "+msg);
        out.println(msg);
        BufferedReader in;
        in = new BufferedReader(
                                new InputStreamReader(
                                socket.getInputStream()));
        System.out.println("sendMessage waiting for OK");
        String r = in.readLine();
        if(!r.startsWith("OK from client")) {
            System.err.println("ERROR: sendMessage did not receive OK from client");
            System.exit(-1);
        } else {
            System.out.println("sendMessage received OK from client");
        }
        in.close();
	out.close();
	socket.close();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}
    }

    private String waitForResponseMessage() throws IOException, SocketTimeoutException {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        try {
            clientSocket.setSoTimeout(50000);
        } catch (SocketException ex) {
            Logger.getLogger(RobotSocketServer.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        System.out.println("Connection established!");
        
        BufferedReader in;
        in = new BufferedReader(
                                new InputStreamReader(
                                clientSocket.getInputStream()));
        String r = in.readLine();
        System.out.println("Received message: "+r);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        out.println("OK from server");
        System.out.println("Sending OK from server");
        in.close();
        out.close();
        clientSocket.close();
//        serverSocket.close();
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {}

        return r;
    }
                
            
            
            
            
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.ServerControllerInterface;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author mgamell
 */
public class ClientController {
    private ServerControllerInterface server;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        ClientController c = new ClientController(host);
    }

    private ClientController(String host) {
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
            
            Boolean response = true;
            try {
                server.stopServer("mgamell", "password");
            } catch(Exception e) {}
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
       
    }
        
}

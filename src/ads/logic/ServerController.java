/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.data.ADSUser;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.sessions.server.Server;

/**
 *
 * @author mgamell
 */
public class ServerController implements ServerControllerInterface {
    private EntityManagerFactory emf;
    private EntityManager em;

    public ServerController() {
        initPersistance();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initRMI();
    }

    private static void initRMI() {
        try {
            ServerController obj = new ServerController();
            ServerControllerInterface stub = (ServerControllerInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("ServerControllerInterface");
            } catch (NotBoundException ex) {}

            registry.bind("ServerControllerInterface", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }

    }

    private void initPersistance() {
        emf = Persistence.createEntityManagerFactory("adsPU");
        em = emf.createEntityManager();
//
//        em.getTransaction().begin();
//
//        ADSUser p1 = new ADSUser();
//        em.persist(p1);
//
//        ADSUser p2 = new ADSUser();
//        em.persist(p2);
//
//        em.getTransaction().commit();
//
//        em.close();
//        emf.close();
    }

    private void deinitPersistance() {
        em.close();
        emf.close();
    }

    private void deinitRMI() {
        try {
            // Unbind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.unbind("ServerControllerInterface");
        } catch (NotBoundException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccessException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean checkLogin(String username, String password) {
        return true;
    }
    
    @Override
    public void stopServer(String username, String password) {
        deinitRMI();
        deinitPersistance();
        System.exit(0);
    }

}

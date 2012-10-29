/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.resources.communication.ServerCommunicator;
import ads.resources.data.ADSUser;
import ads.resources.data.Office;
import ads.resources.datacontroller.Persistance;
import ads.resources.datacontroller.UserController;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

/**
 *
 * @author mgamell
 */
public class ServerController implements ServerControllerInterface {
    private static ServerController singleton = new ServerController();
    private DeliveryCoordinator delCoordinator;

    public static ServerController getInstance() {
        return singleton;
    }

    private ServerController() {
        Persistance.initPersistance();
        insertTestingDataSet();
        delCoordinator = DeliveryCoordinator.getInstance();
        ServerCommunicator.init();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initRMI();
    }

    private void insertTestingDataSet() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        Office o = new Office("601");
        em.persist(o);
        o = new Office("602");
        em.persist(o);
        o = new Office("603");
        em.persist(o);
        o = new Office("604");
        em.persist(o);
        o = new Office("605");
        em.persist(o);
        o = new Office("606");
        em.persist(o);
        o = new Office("admin");
        em.persist(o);
        ADSUser u = new ADSUser("Admin", "istrator", o, "a@a.c", "admin", "admin");
        u.setAdmin(true);
        em.persist(u);
        //Add some additional users for test purposes
        o = new Office("101");
        em.persist(o);
        u = new ADSUser("mehmet", "aktas", o, "mfa@gmail.com", "mfa", "1111");
        em.persist(u);
        //
        o = new Office("102");
        em.persist(o);
        u = new ADSUser("ali", "veli", o, "aliveli@hotmail.com", "aliveli", "2222");
        em.persist(u);
        
        em.getTransaction().commit();
    }

    private static void initRMI() {
        try {
            ServerController obj = ServerController.getInstance();
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
    public void stopServer(String username, String password) {
        deinitRMI();
        Persistance.deinitPersistance();
        System.exit(0);
    }

    @Override
    public int checkLogin(String username, String password) {
        return UserController.checkLogin(username, password);
    }
   
    @Override
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) {
        EntityManager em = Persistance.getEntityManager();
        Office office = new Office(roomNumber);
        // Check business rules
        if (!EmailChecker.checkEmail(email)) return "The e-mail address is not correct";
        // 1. A user with a repeated name.
        if (!em.createNamedQuery("User.searchByName")
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .getResultList()
                .isEmpty())
            return "A user with a name "+firstName+" "+lastName+" already exist";
        // 2. A user with a repeated username
        if (em.find(ADSUser.class, username)!=null)
            return "A user with a username "+username+" already exist";

        office = em.find(Office.class, roomNumber);

        // 3. The office does not exist
        if (office == null)
            return "The room number does not exist";

        // 4. The e-mail address syntax is not correct
        if (!EmailChecker.checkEmail(email))
            return "The e-mail address is not correct";
        
        // 5. The password does not match with the repeated password
        if (!password.equals(password1)) return "The password does not match with the repeated password";

        
        // Everything is correct, create and persist the user
        em.getTransaction().begin();
        try {
            ADSUser u = new ADSUser(firstName, lastName, office, email, username, password);
            em.persist(u);
            em.getTransaction().commit();
            // Everything went well, return null
            return null;
        }
        catch(Exception ex)
        {
            //em.getTransaction().rollback();
            System.out.println(ex.toString());
            ex.printStackTrace();
            em.getTransaction().rollback();
            // There was an error, retorn the appropiate error message
            return "Unexpected error occurred when storing user information";
        }
    }

    @Override
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) {
        EntityManager em = Persistance.getEntityManager();

        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return null;
        }
        
        Set<ADSUser> results = new HashSet<>(20);
        try {
            if(name != null && !name.equals("")) {
                results.addAll(em.createNamedQuery("User.searchByPartialNameES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameSSES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameSES")
                .setParameter("name", name)
                .getResultList());
                results.addAll(em.createNamedQuery("User.searchByPartialNameCFLSES")
                .setParameter("name", name)
                .getResultList());
            }
            if(office != null && !office.equals("")) {
                results.addAll(em.createNamedQuery("User.searchByOffice")
                .setParameter("office", office)
                .getResultList());
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    @Override
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException{
        if(this.checkLogin(username, password)==UserController.userNotCorrect) {
            return;
        }

        delCoordinator.bookDelivery(urgency, targetListUsernames, username);
    }

}

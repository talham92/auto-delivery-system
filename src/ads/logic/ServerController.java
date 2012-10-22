/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import ads.presentation.BookDeliveryView;
import ads.resources.data.ADSUser;
import ads.resources.data.FloorMap;
import ads.resources.data.Office;
import ads.resources.data.UserList;
import java.util.List;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.eclipse.persistence.sessions.server.Server;

/**
 *
 * @author mgamell
 */
public class ServerController implements ServerControllerInterface {
    private EntityManagerFactory emf;
    private EntityManager em;
    
    
    private int tempCount;
    private DeliveryCoordinator delCoordinator;
    public ServerController() {
        tempCount=0;
        delCoordinator=new DeliveryCoordinator();
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
        try{
            em.getTransaction().begin();
            Query query = em.createQuery("select u from ADSUser u");// where u.firstName='mehmet' and u.office='111'");
            List<ADSUser> results = (List<ADSUser>)query.getResultList();
            for(ADSUser u : results) {
                System.out.println("got a person: " + u.getFirstName() + " " + u.getPassword());
            }
            em.getTransaction().commit();
            if (results.size()==1)
                return true;
        }
        catch(Exception ex)
        {
            em.getTransaction().rollback();
        }
        return false;
    }
    
    @Override
    public void stopServer(String username, String password) {
        deinitRMI();
        deinitPersistance();
        System.exit(0);
    }

    @Override
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) {
        Office office = new Office(roomNumber);

        // Check business rules
/*TODO
        if (UserList.searchByName(firstName, lastName)!=null) return "A user with a name "+firstName+" "+lastName+" already exist";
        if (UserList.searchUsername(username)!=null) return "A user with a username "+username+" already exist";
        Office office = FloorMap.searchByRoomNumber(roomNumber);
        if (office == null) return "The room number does not exist";
        */
        if (!EmailChecker.checkEmail(email)) return "The e-mail address is not correct";
        if (!password.equals(password1)) return "The password does not match with the repeated password";

        // Everything is correct, create and persist the user
        em.getTransaction().begin();
        try{
            ADSUser u = new ADSUser(firstName, lastName, office, email, username, password);
            em.persist(u);
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            em.getTransaction().rollback();
        }
        // Everything went well, return null
        return null;
    }

    @Override
    public String[] searchUser_NameOffice(String name, String office) throws RemoteException{
        //throw new UnsupportedOperationException("Not supported yet.");
          String[] receiverInfo = new String[2];
//        if (tempCount==0) {receiverInfo[0]="Marc Antony"; receiverInfo[1]= "Colosseum 12";}
//        else              {receiverInfo[0]="Hasan Sabbah"; receiverInfo[1]= "Alamut"; tempCount=-1;}
//        tempCount++;
        try{
            em.getTransaction().begin();
            Query query = em.createQuery("select u from ADSUser u where u.firstName='name' and u.office='office'");
            List<ADSUser> results = (List<ADSUser>)query.getResultList();
            for(ADSUser u : results) {
                receiverInfo[0]=u.getFirstName();
                receiverInfo[1]=u.getOffice().getOfficeAddress();
                System.out.println("got a person: " + u.getFirstName() + " " + u.getOffice());
            }
            em.getTransaction().commit();
        }
        catch(Exception ex)
        {
            em.getTransaction().rollback();
        }
        return receiverInfo;
    }

    @Override
    public void bookDelivery(String urgency, ArrayList<String[]> targetList) throws RemoteException {
        delCoordinator.bookDelivery(urgency, targetList);
        
    }
    
}

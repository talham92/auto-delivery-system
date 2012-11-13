
package ads.logic;

import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import ads.resources.data.Office;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  ServerControllerInterface is called by ServerController and implemented by
 *  the following methods.
 * 
 */
public interface ServerControllerInterface extends Remote {
    /**
    * Create Office object to database
    * @param office The Office object
    * @throws RemoteException 
    * @throws ServerInitializedException 
    * @throw ServerInitializedException  If the server is initialized 
    * @see FloorMap#createOffice(Office) 
     */
    public void createOffice(Office office)throws RemoteException, ServerInitializedException;
      /**
     * Clear Offices
     * @return String showing office is cleared
     * @throws RemoteException 
     * @throws ServerInitializedException  If the server is initialized 
     * 
     * @see FloorMap#clearOffices() 
     */
    public String clearOffices() throws RemoteException, ServerInitializedException;
    
      /**
     * Create links between offices
     * @throws RemoteException  If execution of a remote method call
     * @throws ServerInitializedException  If the server is initialized 
     * 
     * @see FloorMap#createLinksBtwOffices() 
     */
    public void createLinksBtwOffices() throws RemoteException, ServerInitializedException;
    
    /**
    * Initialize with testing data.
    * First, check whether the system is initialized or not, then delete all 
    * persistance records, insert the testing data, and initialize the
    * RobotPositionAccessor, and get a DeliveryCoordinator object, and define the 
    * state is 1.
    * @throws ServerInitializedException  If the Server is not initialized successfully
    * @throws RemoteException If execution of a remote method call
    * 
    * @see ServerController#checkSystemInitialized() 
    * @see Persistance#deleteAllPersistanceRecords() 
    * @see ServerController#insertBigTestingDataSet() 
    */
    public void initializeWithTestingData() throws RemoteException, ServerInitializedException;
    
     /**
     * Initialize the system.
     * First check weather the system is initialized, initialize the
     * RobotPositionAccessor,get a DeliveryCoordinator object, and define the 
     * state is 1.
     * @throws ServerInitializedException 
     * @throws RemoteException If execution of a remote method call
     */
    public void initializeSystem() throws RemoteException, ServerInitializedException;
    
    /**
    * Check whether the Admin login.
    * 
    * @param username The String of username
    * @param password The String of password 
    * @return int login, which is user-defined.
    * @throws ServerNonInitializedException  If the Server is not initialized
    * @throws RemoteException If execution of a remote method call
    * 
    * @see UserController#checkLogin(String,String) 
    */
   
    public int checkLogin(String username, String password) throws RemoteException, ServerNonInitializedException;
    
    /**
     * Stop Server.
     * Deiniitilze the RMI and Persistance, exit the system.
     * 
     * @param username The String of username
     * @param password The String of password
     * @throws ServerNonInitializedException  If the Server is not initialized
     * @throws RemoteException If execution of a remote method call
     * 
     * @see ServerController#deinitRMI() 
     * @see Persistance#deinitPersistance() 
     * @see System#exit() 
     */
    public void stopServer(String username, String password) throws RemoteException, ServerNonInitializedException;
    
       /**
     * Helps with the user's register.
     * Check the validity of email format, username, office, password format
     * when user registers
     * 
     * @param firstName  The first name of user
     * @param lastName   The last name of user
     * @param roomNumber The room number
     * @param email      The email String
     * @param username   User's username
     * @param password   String of password
     * @param password1  The repeated password String
     * @return           String showing the scenario when user registers
     * @throws ServerNonInitializedException  If the Server is not initialized
     * @throws RemoteException If execution of a remote method call
     */
    public String register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1) throws RemoteException, ServerNonInitializedException;
    
     /**
     * Search user by name or office number.
     * @param username The String of username
     * @param password The String of password
     * @param name    The String of username
     * @param office  The String of office
     * @return   The searching results, which is an object of ADSUser
     * @throws ServerNonInitializedException  If the Server is not initialized
     * @throws RemoteException If execution of a remote method call
     * 
     * @see UserController#searchUser_NameOffice(String,String) 
     */
    public Set<ADSUser> searchUser_NameOffice(String username, String password, String name, String office) throws RemoteException, ServerNonInitializedException;
     
    /**
     * Book delivery.
     * @param username The String of username
     * @param password The String of password
     * @param urgency  The priority of delivery
     * @param targetListUsernames The list of receivers
     * @throws RemoteException  If execution of a remote method call
     * @throws NonBookedDeliveryException  If there is NonBookedDeliveryException
     * @throws ServerNonInitializedException If the server is non initialized
     * 
     * @see DeliveryCoordinator#bookDelivery(double,List,String) 
     */
    public void bookDelivery(String username, String password, double urgency, List<String> targetListUsernames) throws RemoteException, NonBookedDeliveryException, ServerNonInitializedException;
    
    /**
     * Get system status, including where is the robot and whether it is 
     * moving
     * @param username The String of username
     * @param password The String of password
     * @return The SystemStatus Object
     * @throws RemoteException If execution of a remote method call
     * @throws ServerNonInitializedException If the server is non initialized
     */
    public SystemStatus getSystemStatus(String username, String password) throws RemoteException, ServerNonInitializedException;
    
    /**
    * Get User Delivery list
    * @param username The String of username
    * @param password The String of password
    * @return List of Delivery
    * @throws RemoteException If execution of a remote method call
    * @throws ServerNonInitializedException If the server is non initialized
    * 
    * @see DeliveryHistory#getUserDeliveryList(ads.resources.data.ADSUser) 
    */
    public List<Delivery> getUserDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException;
    
      /**
     * Get Delivery List
     * @param username The String of username
     * @param password The String of password
     * @return List of Delivery
     * @throws RemoteException If execution of a remote method call
     * @throws ServerNonInitializedException If the server is non initialized
     * 
     * @see DeliveryHistory#getDeliveryList() 
     */
    public List<Delivery> getDeliveryList(String username, String password) throws RemoteException, ServerNonInitializedException;
    
    /**
    * Get the details of delivery
    * @param username The String of username
    * @param password The String of password
    * @param deliveryId The number of delivery ID
    * @return List of delivery details
    * @throws RemoteException If execution of a remote method call
    * @throws ServerNonInitializedException If Server is not initialized
    */
    public List<String[]> getDeliveryDetails(String username, String password, int deliveryId) throws RemoteException, ServerNonInitializedException;

     /**
     * Get the drawing map array, knowing the position point in the map
     * @return The Arraylist showing the map
     * @throws RemoteException If execution of a remote method call
     * 
     * @see FloorMap#getMapDrawingArray() 
     */
    public ArrayList<String[]> getMapDrawingArray() throws RemoteException;
}

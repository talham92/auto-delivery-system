
package ads.logic;

/**
 * custom exception class that descends from Java's Exception class.
 */
public class ServerNonInitializedException extends Exception {
     
    /**
     * Constructs an exception with the specified detail message. 
     * @param message 
     */
    public ServerNonInitializedException(String message) {
        super(message);
    }
    
}

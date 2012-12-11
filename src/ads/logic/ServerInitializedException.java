
package ads.logic;

/**
 * custom exception class that descends from Java's Exception class.
 */
public class ServerInitializedException extends Exception {
     
    /**
     * Constructs an exception with the specified detail message. 
     * @param message 
     */
    public ServerInitializedException(String message) {
        super(message);
    }
    
}

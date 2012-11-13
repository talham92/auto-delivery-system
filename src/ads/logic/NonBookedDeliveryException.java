
package ads.logic;

/**
 * custom exception class that descends from Java's Exception class.
 */
public class NonBookedDeliveryException extends Exception {
    /**
     * Constructs an exception with the specified detail message. 
     * @param m the detail message
     */
    public NonBookedDeliveryException(String m) {
        super(m);
    }
}

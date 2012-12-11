/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

/**Define the time out exception
 * 
 * @author mgamell
 */

public class TimeoutException extends Exception {

    /**
     *
     * @param message
     */
    public TimeoutException(String message) {
        super(message);
    }
    
}

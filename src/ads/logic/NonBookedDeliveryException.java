/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

/**
 *
 * @author mgamell
 */
public class NonBookedDeliveryException extends Exception {
    public NonBookedDeliveryException(String m) {
        super(m);
    }
}
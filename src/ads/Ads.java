/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads;

import ads.data.ADSUser;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mgamell
 */
public class Ads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello");
        ADSUser u = new ADSUser();
        System.out.println("Hello");
        try {
            Thread.sleep(10000);
        System.out.println("Hello");
        } catch (InterruptedException ex) {
            Logger.getLogger(Ads.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

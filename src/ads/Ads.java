/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads;

import ads.logic.ServerController;
import ads.presentation.ClientController;
import adsrobotstub.RobotStub;

/**
 *
 * @author mgamell
 */
public class Ads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length > 0) {
            if(args[0].equals("server")) {
                ServerController.main(args);
            } else if(args[0].equals("stub")) {
                RobotStub.main(args);
            } else {
                ClientController.main(args);
            }
        } else {
            ClientController.main(args);
        }
//        new Thread(new Runnable() {
//            public void run() {
//                ServerController.main(new String[]{});
//            }
//        }).start();
//        new Thread(new Runnable() {
//            public void run() {
//                ClientController.main(new String[]{});
//            }
//        }).start();
//        new Thread(new Runnable() {
//            public void run() {
//                ClientController.main(new String[]{});
//            }
//        }).start();
    }
}

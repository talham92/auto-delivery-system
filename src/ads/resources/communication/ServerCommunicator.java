/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

/**
 *
 * @author mgamell
 */
public class ServerCommunicator {
    private static RobotStub stub;

    public static void init() {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                stub = new RobotStub();
                stub.setVisible(true);
            }
        });
    }
    
    public static void moveRobotToNextPoint() {
        stub.moveRobotToNextPoint();
    }

    public static String waitForPassword(int timeout) throws TimeoutException {
        //TODO: IMPORTANT: IMPLEMENT TIMEOUT throw new TimeoutException("Timeout "+timeout+" passed.");
        return stub.waitForPassword(timeout);
    }

    public static void ringBuzzer() {
        stub.ringBuzzer();
    }

    public static void showPasswordIncorrectError() {
        stub.showPasswordIncorrectError();
    }

    public static void showPasswordIncorrectWarning() {
        stub.showPasswordIncorrectWarning();
    }

    public static void showPasswordCorrectMessage() {
        stub.showPasswordCorrectMessage();
    }

    public static void openTray(int trayNum) {
        stub.openTray(trayNum);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

/**
 *
 * @author mgamell
 */
public interface ServerCommunicator {
    public void init();
    public void moveRobotToNextPoint();
    public String waitForPassword(int timeout, String username) throws TimeoutException;
    public void ringBuzzer();
    public void showPasswordIncorrectError();
    public void showPasswordIncorrectWarning();
    public void showPasswordCorrectMessage();
    public void openTray(int trayNum);
}

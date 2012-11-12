/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.logic;

import java.io.Serializable;

/**
 *
 * @author mgamell
 */
public class SystemStatus implements Serializable {
    private static final long serialVersionUID = 752647222340717L;
    
    private String robotPosition;
    private boolean robotIsMoving;
    private boolean serverInitialized;
    private String usersInPosition;

    public SystemStatus(String robotPosition, boolean robotIsMoving, boolean serverInitialized, String usersInPosition) {
        this.robotPosition = robotPosition;
        this.robotIsMoving = robotIsMoving;
        this.serverInitialized = serverInitialized;
        this.usersInPosition = usersInPosition;
    }


    public String getRobotPosition() {
        return robotPosition;
    }

    public void setRobotPosition(String robotPosition) {
        this.robotPosition = robotPosition;
    }

    public boolean isRobotIsMoving() {
        return robotIsMoving;
    }

    public void setRobotIsMoving(boolean robotIsMoving) {
        this.robotIsMoving = robotIsMoving;
    }

    public boolean isServerInitialized() {
        return serverInitialized;
    }

    public void setServerInitialized(boolean serverInitialized) {
        this.serverInitialized = serverInitialized;
    }

    public String getUsersInPosition() {
        return usersInPosition;
    }

    public void setUsersInPosition(String usersInPosition) {
        this.usersInPosition = usersInPosition;
    }

}

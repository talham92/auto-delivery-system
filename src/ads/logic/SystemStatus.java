
package ads.logic;

import java.io.Serializable;

/**
 * Get the System Status, including where is the robot and whether the robot 
 * is moving.
 */
public class SystemStatus implements Serializable {
    private static final long serialVersionUID = 752647222340717L;
    
    private String robotPosition; // The String of robot position
    private boolean robotIsMoving; // Boolean parameter showing whether robot moving
    private boolean serverInitialized; // Boolean parameter showing whether the system is initialzied
    private String usersInPosition;// The String of user's  position
/**
 * SystemStatus constructor getting the robot position and whether the 
 * robot is moving.
 * @param robotPosition // The String of robot position
 * @param robotIsMoving // Boolean parameter showing whether robot moving
 * @param serverInitialized // Boolean parameter showing whether the system is initialzied
 * @param usersInPosition // The String of user's  position
 */
    public SystemStatus(String robotPosition, boolean robotIsMoving, boolean serverInitialized, String usersInPosition) {
        this.robotPosition = robotPosition;
        this.robotIsMoving = robotIsMoving;
        this.serverInitialized = serverInitialized;
        this.usersInPosition = usersInPosition;
    }

/**
 * Get the Robot Position
 * @return String of robot position
 */
    public String getRobotPosition() {
        return robotPosition;
    }
/**
 * Set the Robot Position
 * @param robotPosition // The String of robot position
 */
    public void setRobotPosition(String robotPosition) {
        this.robotPosition = robotPosition;
    }
/**
 * check whether the robot is moving
 * @return boolean parameter robotIsMoving
 */
    public boolean isRobotIsMoving() {
        return robotIsMoving;
    }
/**
 * Set Robot is moving
 * @param robotIsMoving Boolean parameter showing whether robot moving
 */
    public void setRobotIsMoving(boolean robotIsMoving) {
        this.robotIsMoving = robotIsMoving;
    }
/**
 * Check whether Server is initialized
 * @return  Boolean parameter showing Server status 
 */
    public boolean isServerInitialized() {
        return serverInitialized;
    }
/**
 * Set Server Initialized
 * @param serverInitialized boolean parameter showing Server status
 */
    public void setServerInitialized(boolean serverInitialized) {
        this.serverInitialized = serverInitialized;
    }
/**
 * Get User's position
 * @return String of user's position
 */
    public String getUsersInPosition() {
        return usersInPosition;
    }
/**
 * Set User's position
 * @param usersInPosition  String of user's position 
 */
    public void setUsersInPosition(String usersInPosition) {
        this.usersInPosition = usersInPosition;
    }

}

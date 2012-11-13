/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adsrobotstub;

import java.io.Serializable;

/**
 *
 * @author mgamell
 */

/** 
* @class RobotStubStatus 
* @a class of stubbing the robot status
*/ 

public class RobotStubStatus implements Serializable {
    private static final long serialVersionUID = 11081273897L;

    /**
     *
     */
    public boolean isMoving;
//    public String       movingTarget;
    /**
     *
     */
    public boolean isRequestingPassword;
    /**
     *
     */
    public boolean      isPasswordCorrect;
    /**
     *
     */
    public boolean      isPasswordWarning;
    /**
     *
     */
    public boolean      isPasswordError;
    /**
     *
     */
    public String       passwordUsername;
    /**
     *
     */
    public boolean isBuzzerRinging;
    /**
     *
     */
    public int trayOpen;

    /**
     *
     * @param status
     */
    public RobotStubStatus(RobotStubStatus status) {
        isMoving = status.isMoving;
        isRequestingPassword = status.isRequestingPassword;
        isPasswordCorrect = status.isPasswordCorrect;
        isPasswordWarning = status.isPasswordWarning;
        isPasswordError = status.isPasswordError;
        passwordUsername = status.passwordUsername;
        isBuzzerRinging = status.isBuzzerRinging;
        trayOpen = status.trayOpen;
    }

    /**
     *
     */
    public RobotStubStatus() {
        trayOpen = -1;
    }
    
}

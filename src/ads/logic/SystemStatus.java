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
    
    private String position;
    private boolean moving;

    public SystemStatus(String position, boolean moving) {
        this.position = position;
        this.moving = moving;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
   
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**Define the robot status and data parameters
 *
 * @author mgamell
 */
@Entity
public class RobotPosition implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    private Office lastKnownPosition;
    private boolean isMoving;
    /**
     * The constructor of RobotPosition
     */
    public RobotPosition() {
    }
    /**
     * The constructor of RobotPosition
     * @param lastKnownPosition
     * @param isMoving 
     */
    public RobotPosition(Office lastKnownPosition, boolean isMoving) {
        id = 1L;
        this.lastKnownPosition = lastKnownPosition;
        this.isMoving = isMoving;
    }
    /**
     * Get the string value of id
     * @return long id
     */
    public Long getId() {
        return id;
    }
    /**
     * Set the id as the input value
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }
       /**
        * Get the lastknownPosition as in Office class
        * @return 
        */
    public Office getLastKnownPosition() {
        return lastKnownPosition;
    }
    /**
     * Set the last known position as input last known position in office class 
     * @param lastKnownPosition 
     */
    public void setLastKnownPosition(Office lastKnownPosition) {
        this.lastKnownPosition = lastKnownPosition;
    }
    /**
     * To judge whether the robot is moving
     * @return boolean isMoving
     */
    public boolean isIsMoving() {
        return isMoving;
    }
    /**
     * To set the isMoving value as the input boolean value
     * @param isMoving 
     */
    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
    /**
     * To set the hashcode value for mapping and discrimination
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lastKnownPosition != null ? lastKnownPosition.hashCode() : 0);
        return hash;
    }
    /**
     * To judge if the status of robot position 
     * @param object
     * @return boolean true, false
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RobotPosition)) {
            return false;
        }
        RobotPosition other = (RobotPosition) object;
        if ((this.lastKnownPosition == null && other.lastKnownPosition != null) || (this.lastKnownPosition != null && !this.lastKnownPosition.equals(other.lastKnownPosition))) {
            return false;
        }
        return true;
    }
    /**
     * To get the value of last known position as string type
     * @return string lastKnownPosition
     */
    @Override
    public String toString() {
        return "ads.resources.data.RobotPosition[ lastKnownPosition=" + lastKnownPosition + " ]";
    }
    
}

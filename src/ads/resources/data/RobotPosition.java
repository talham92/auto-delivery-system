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

/**
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

    public RobotPosition() {
    }

    public RobotPosition(Office lastKnownPosition, boolean isMoving) {
        id = 1L;
        this.lastKnownPosition = lastKnownPosition;
        this.isMoving = isMoving;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Office getLastKnownPosition() {
        return lastKnownPosition;
    }

    public void setLastKnownPosition(Office lastKnownPosition) {
        this.lastKnownPosition = lastKnownPosition;
    }

    public boolean isIsMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lastKnownPosition != null ? lastKnownPosition.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "ads.resources.data.RobotPosition[ lastKnownPosition=" + lastKnownPosition + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author mgamell
 */
@Entity
public abstract class DeliveryStep implements Serializable {
    private static final long serialVersionUID = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp timeCreation;

    public DeliveryStep(Timestamp timeCreation) {
        this.timeCreation = timeCreation;
    }

    public DeliveryStep() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DeliveryStep)) {
            return false;
        }
        DeliveryStep other = (DeliveryStep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ads.data.DeliveryStep[ id=" + id + " ]";
    }
    
    public Timestamp getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Timestamp timeCreation) {
        this.timeCreation = timeCreation;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

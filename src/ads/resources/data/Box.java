/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *Define the status of mail box of the robot
 * @author mgamell
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="Box.searchEmpty",
        query="SELECT c FROM Box c WHERE c.delivery IS NULL"
    ),
    @NamedQuery(
        name="Box.searchDelivery",
//        query="SELECT c FROM Box c WHERE c.delivery.sender LIKE :sender AND c.delivery.receiver LIKE :receiver AND c.delivery.timestampfield like :timestampfield"
        query="SELECT c FROM Box c WHERE c.delivery = :delivery"
    )
})
public class Box implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    private Delivery delivery;
    /**
     * To get the integer value as id
     * @return int id
     */
    public int getId() {
        return id;
    }
    /**
     * To set id as the input int value
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * To get the delivery as in Delivery
     * @return delivery
     */
    public Delivery getDelivery() {
        return delivery;
    }
    /**
     * Set the delivery as input in delivery class
     * @param delivery 
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    /**
     * Define a hash code number for mapping and discrimination
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        return hash;
    }
    /**
     * To judge whether the object and id is correct 
     * @param object
     * @return boolean true, false
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Box)) {
            return false;
        }
        Box other = (Box) object;
        if (id != other.id) {
            return false;
        }
        return true;
    }
    /**
     * To change id value to string type
     * @return id
     */
    @Override
    public String toString() {
        return "ads.data.Box[ id=" + id + " ]";
    }
    
}

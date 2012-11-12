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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**Define the step status of delivery information
 *
 * @author mgamell
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="Delivery.searchPending",
//        query="SELECT s.delivery FROM DeliveryStep s WHERE TYPE(s) IN :class"
        query="SELECT s FROM Delivery s WHERE NOT EXISTS (SELECT t FROM DeliveryStep t WHERE t.delivery = s AND TYPE(t) IN :delivereddeliveryclass)"
        //SELECT e FROM Employee e JOIN e.projects p WHERE NOT EXISTS (SELECT t FROM Project t WHERE p = t AND t.STATUS <> 'In trouble')

    ),
    @NamedQuery(
        name="Delivery.searchStateListOrderedByDate",
        query="SELECT s FROM DeliveryStep s WHERE s.delivery = :delivery ORDER BY s.timeCreation DESC"
    ),
    @NamedQuery(
        name="Delivery.searchUserDeliveryList",
        query="SELECT s FROM Delivery s WHERE s.sender = :sender ORDER BY s.timestampField DESC"
    ),
    @NamedQuery(
        name="Delivery.searchAllDeliveryList",
        query="SELECT s FROM Delivery s"
    )
})

public abstract class DeliveryStep implements Serializable {
    private static final long serialVersionUID = 10L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Timestamp timeCreation;
    @ManyToOne
    private Delivery delivery;
    /**
     * The constructor of DeliveryStep
     * @param timeCreation
     * @param delivery 
     */
    public DeliveryStep(Timestamp timeCreation, Delivery delivery) {
        this.timeCreation = timeCreation;
        this.delivery = delivery;
    }
    /**
     * The constructor of DeliveryStep
     */
    public DeliveryStep() {
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    /**
     * To judge whether the object is the correct one and the id information
     * @param object
     * @return 
     */
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
    /**
     * To return the string type of id
     * @return string id
     */
    @Override
    public String toString() {
        return "ads.data.DeliveryStep[ id=" + id + " ]";
    }
    /**
     * To get the value of time creation as input 
     * @return timeCreation
     */
    public Timestamp getTimeCreation() {
        return timeCreation;
    }
    /**
     * To set the time creation information as in the Timestramp 
     * @param timeCreation 
     */
    public void setTimeCreation(Timestamp timeCreation) {
        this.timeCreation = timeCreation;
    }    
    /**
     * To get the Id as the input long
     * @return long id
     */
    public Long getId() {
        return id;
    }
    /**
     * To set Id as the long value
     * @param long id 
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * To get the delivery information as input 
     * @return delivery
     */
    public Delivery getDelivery() {
        return delivery;
    }
    /**
     * To set the delivery information as in delivery class
     * @param delivery 
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

}

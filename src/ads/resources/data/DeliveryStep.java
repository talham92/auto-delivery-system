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

/**
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

    public DeliveryStep(Timestamp timeCreation, Delivery delivery) {
        this.timeCreation = timeCreation;
        this.delivery = delivery;
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

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

}

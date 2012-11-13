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
import javax.persistence.OneToOne;

/**
 *Record the delivered deliveries
 * @author mgamell
 */
@Entity
public class DeliveredDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 13L;

    /**
     *
     */
    public DeliveredDelivery() {
    }
    /**
     * call super class DeliveryStep constructor 
     * @param timeCreation
     * @param delivery 
     */
    public DeliveredDelivery(Timestamp timeCreation, Delivery delivery) {
        super(timeCreation, delivery);
    }
    /**
     * Call the function getid() of DeliveryStep and return string
     * @return string Id
     */
    @Override
    public String toString() {
        return "ads.data.DeliveredDelivery[ id=" + super.getId() + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;

/**Records the picked up deliveries 
 *
 * @author mgamell
 */
@Entity
public class PickedUpDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 12L;

    /**
     *
     */
    public PickedUpDelivery() {
    }
    /**
     * call super class DeliveryStep constructor
     * @param timeCreation
     * @param delivery 
     */
    public PickedUpDelivery(Timestamp timeCreation, Delivery delivery) {
        super(timeCreation, delivery);
    }
    /**
     * Call the function getid() of DeliveryStep and return string
     * @return string Id
     */
    @Override
    public String toString() {
        return "ads.data.PickedUpDelivery[ id=" + super.getId() + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;

/**
 *
 * @author mgamell
 */
@Entity
public class PickedUpDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 12L;

    public PickedUpDelivery() {
    }

    public PickedUpDelivery(Timestamp timeCreation, Delivery delivery) {
        super(timeCreation, delivery);
    }

    @Override
    public String toString() {
        return "ads.data.PickedUpDelivery[ id=" + super.getId() + " ]";
    }
    
}

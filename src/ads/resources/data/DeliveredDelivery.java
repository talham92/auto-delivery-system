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
 *
 * @author mgamell
 */
@Entity
public class DeliveredDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 13L;

    public DeliveredDelivery() {
    }

    public DeliveredDelivery(Timestamp timeCreation, Delivery delivery) {
        super(timeCreation, delivery);
    }

    @Override
    public String toString() {
        return "ads.data.DeliveredDelivery[ id=" + super.getId() + " ]";
    }
    
}

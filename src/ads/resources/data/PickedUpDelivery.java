/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author mgamell
 */
@Entity
public class PickedUpDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 12L;
    @OneToOne
    private BookedDelivery previousState;

    public PickedUpDelivery() {
    }

    public PickedUpDelivery(Timestamp timeCreation, BookedDelivery previousState) {
        super(timeCreation);
        this.previousState = previousState;
    }

    public BookedDelivery getPreviousState() {
        return previousState;
    }

    public void setPreviousState(BookedDelivery previousState) {
        this.previousState = previousState;
    }

    @Override
    public String toString() {
        return "ads.data.PickedUpDelivery[ id=" + super.getId() + " ]";
    }
    
}

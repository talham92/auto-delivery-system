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
public class BookedDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 11L;

    public BookedDelivery() {
    }
    
    public BookedDelivery(Timestamp time) {
        super(time);
    }

    @Override
    public String toString() {
        return "ads.data.BookedDelivery[ id=" + super.getId() + " ]";
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;

/**Record the booked deliveries
 *
 * @author mgamell
 */
@Entity

public class BookedDelivery extends DeliveryStep implements Serializable {
    private static final long serialVersionUID = 11L;

    public BookedDelivery() {
    }
    /**
     * call super class constructor
     * @param time It records the time 
     * @param delivery It records the delivery
     */
    public BookedDelivery(Timestamp time, Delivery delivery) {
        super(time, delivery);
    }
    /**call function getid() of DeliveryStep and return string
     * 
     * @return string Id
     */
    @Override
    public String toString() {
        return "ads.data.BookedDelivery[ id=" + super.getId() + " ]";
    }
}

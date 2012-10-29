/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author mgamell
 */
@IdClass(DeliveryPK.class)
@Entity
@NamedQuery(
    name="Delivery.searchPending",
    query="SELECT c FROM Delivery c WHERE TYPE(c.state) IN :class"
)
public class Delivery implements Serializable {
    private static final long serialVersionUID = 6L;

    @Id
    @ManyToOne
    private ADSUser sender;
    @Id
    @ManyToOne
    private ADSUser receiver;
    @Id
    private Timestamp timestampField;
    @OneToOne
    private DeliveryStep state;
    private double priority;

    public Delivery() {
    }

    public Delivery(ADSUser sender, ADSUser receiver, Timestamp timestampField, DeliveryStep state, double urgency) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestampField = timestampField;
        this.state = state;
        this.priority = urgency;
    }

    public ADSUser getSender() {
        return sender;
    }

    public void setSender(ADSUser sender) {
        this.sender = sender;
    }

    public ADSUser getReceiver() {
        return receiver;
    }

    public void setReceiver(ADSUser receiver) {
        this.receiver = receiver;
    }

    public Timestamp getTimestampField() {
        return timestampField;
    }

    public void setTimestampField(Timestamp timestampField) {
        this.timestampField = timestampField;
    }

    public DeliveryStep getState() {
        return state;
    }

    public void setState(DeliveryStep state) {
        this.state = state;
    }

    public double getUrgency() {
        return priority;
    }

    public void setUrgency(double urgency) {
        this.priority = urgency;
    }

    public ADSUser getNextUser() {
        if(this.state instanceof DeliveredDelivery) {
            throw new RuntimeException("This delivery has been already delivered, it is closed!");
        } else if(this.state instanceof BookedDelivery) {
            return this.sender;
        } else if(this.state instanceof PickedUpDelivery) {
            return this.receiver;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public void updateState() {
        java.util.Date date = new java.util.Date();
        Timestamp timestampAux = new Timestamp(date.getTime());
        if(this.state instanceof DeliveredDelivery) {
            throw new RuntimeException("This delivery has been already delivered, it is closed!");
        } else if(this.state instanceof BookedDelivery) {
            // Create a new state and store the previous one.
            state = new PickedUpDelivery((Timestamp)timestampAux.clone(), (BookedDelivery) state);
        } else if(this.state instanceof PickedUpDelivery) {
            // Create a new state and store the previous one.
            state = new DeliveredDelivery((Timestamp)timestampAux.clone(), (PickedUpDelivery) state);
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public boolean isBookedNotYetPickedUp() {
        if(this.state instanceof DeliveredDelivery) {
            return false;
        } else if(this.state instanceof BookedDelivery) {
            return true;
        } else if(this.state instanceof PickedUpDelivery) {
            return false;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public boolean isPickedUpNotYetDelivered() {
        if(this.state instanceof DeliveredDelivery) {
            return false;
        } else if(this.state instanceof BookedDelivery) {
            return false;
        } else if(this.state instanceof PickedUpDelivery) {
            return true;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public boolean isDelivered() {
        if(this.state instanceof DeliveredDelivery) {
            return true;
        } else if(this.state instanceof BookedDelivery) {
            return false;
        } else if(this.state instanceof PickedUpDelivery) {
            return false;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }
}

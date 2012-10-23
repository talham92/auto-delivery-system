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

/**
 *
 * @author mgamell
 */
@IdClass(DeliveryPK.class)
@Entity
public class Delivery implements Serializable {
    private static final long serialVersionUID = 6L;

    @Id
    private ADSUser sender;
    @Id
    private ADSUser receiver;
    @Id
    private Timestamp timestampField;
    private DeliveryStep state;
    private String urgency;

    public Delivery() {
    }

    public Delivery(ADSUser sender, ADSUser receiver, Timestamp timestampField, DeliveryStep state, String urgency) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestampField = timestampField;
        this.state = state;
        this.urgency = urgency;
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

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

}

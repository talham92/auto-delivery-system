/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import ads.resources.datacontroller.Persistance;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author mgamell
 */
@Entity
public class Delivery implements Serializable {
    private static final long serialVersionUID = 6L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @ManyToOne
    private ADSUser sender;
    @ManyToOne
    private ADSUser receiver;
    private Timestamp timestampField;
    private double priority;

    public Delivery() {
    }

    public Delivery(ADSUser sender, ADSUser receiver, Timestamp timestampField, double priority) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestampField = timestampField;
        this.priority = priority;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
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

    public ADSUser getNextUser() {
        DeliveryStep state = getCurrentState();
        if(state instanceof DeliveredDelivery) {
            throw new RuntimeException("This delivery has been already delivered, it is closed!");
        } else if(state instanceof BookedDelivery) {
            return this.sender;
        } else if(state instanceof PickedUpDelivery) {
            return this.receiver;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public void updateState() {
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        java.util.Date date = new java.util.Date();
        Timestamp timestampAux = new Timestamp(date.getTime());
        DeliveryStep state = getCurrentState();
        DeliveryStep newState;
        if(state instanceof DeliveredDelivery) {
            em.getTransaction().rollback();
            throw new RuntimeException("This delivery has been already delivered, it is closed!");
        } else if(state instanceof BookedDelivery) {
            // Create a new state and store the previous one.
            newState = new PickedUpDelivery((Timestamp)timestampAux.clone(), this);
        } else if(state instanceof PickedUpDelivery) {
            // Create a new state and store the previous one.
            newState = new DeliveredDelivery((Timestamp)timestampAux.clone(), this);
        } else {
            em.getTransaction().rollback();
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
        em.persist(newState);
        em.getTransaction().commit();
    }

    public boolean isBookedNotYetPickedUp() {
        DeliveryStep state = getCurrentState();
        if(state instanceof DeliveredDelivery) {
            return false;
        } else if(state instanceof BookedDelivery) {
            return true;
        } else if(state instanceof PickedUpDelivery) {
            return false;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public boolean isPickedUpNotYetDelivered() {
        DeliveryStep state = getCurrentState();
        if(state instanceof DeliveredDelivery) {
            return false;
        } else if(state instanceof BookedDelivery) {
            return false;
        } else if(state instanceof PickedUpDelivery) {
            return true;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public boolean isDelivered() {
        DeliveryStep state = getCurrentState();
        if(state instanceof DeliveredDelivery) {
            return true;
        } else if(state instanceof BookedDelivery) {
            return false;
        } else if(state instanceof PickedUpDelivery) {
            return false;
        } else {
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
    }

    public DeliveryStep getCurrentState() {
        EntityManager em = Persistance.getEntityManager();
        DeliveryStep deliveryStep = (DeliveryStep) em.createNamedQuery("Delivery.searchStateListOrderedByDate")
                .setParameter("delivery", this)
                .setMaxResults(1)
                .getSingleResult();
        return deliveryStep;
    }

    public List<DeliveryStep> getStateList() {
        EntityManager em = Persistance.getEntityManager();
        List<DeliveryStep> deliverySteps = em.createNamedQuery("Delivery.searchStateListOrderedByDate")
                .setParameter("delivery", this)
                .getResultList();
        return deliverySteps;
    }
}

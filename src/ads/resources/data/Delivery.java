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

/**Depicting the process and participants of booking and picking a delivery.
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

    /**
     *
     */
    public Delivery() {
    }
    /**
     * constructor function of Delivery class
     * @param sender
     * @param receiver
     * @param timestampField
     * @param priority 
     */
    public Delivery(ADSUser sender, ADSUser receiver, Timestamp timestampField, double priority) {
        this.sender = sender;
        this.receiver = receiver;
        this.timestampField = timestampField;
        this.priority = priority;
    }

    /**
     * To get the integer value of id
     * @return int id
     */
    public int getId() {
        return id;
    }
    /**
     * set the id as the input integer
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * To get the double value of priority
     * @return double priority
     */
    public double getPriority() {
        return priority;
    }
    /**
     * Set the priority as the input double
     * @param priority 
     */
    public void setPriority(double priority) {
        this.priority = priority;
    }

    /**
     * To get the string value of sender
     * @return string sender
     */
    public ADSUser getSender() {
        return sender;
    }
    /**
     * Set the sender as the value of ADSUer sender
     * @param sender 
     */
    public void setSender(ADSUser sender) {
        this.sender = sender;
    }
    /**
     * Get the value of the receiver in ADSUer class
     * @return string receiver
     */
    public ADSUser getReceiver() {
        return receiver;
    }
    /**
     * Set the receiver as the value of ADSUer receiver
     * @param receiver 
     */
    public void setReceiver(ADSUser receiver) {
        this.receiver = receiver;
    }
    /**
     * Get the value of TimestampField in Timestamp class
     * @return timestampField
     */
    public Timestamp getTimestampField() {
        return timestampField;
    }
    /**
     * Set the timestampField as the value of Timestamp timestampField
     * @param timestampField 
     */
    public void setTimestampField(Timestamp timestampField) {
        this.timestampField = timestampField;
    }
    /**
     * Get the value of next user in ADSUer class
     * @return sender, receiver
     * @exception RuntimeException if the delivery is delivered
     */
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
    /**
     * 
     */
    public void updateState() {
        //create a user entity
        EntityManager em = Persistance.getEntityManager();
        em.getTransaction().begin();
        //create an new date object
        java.util.Date date = new java.util.Date();
        Timestamp timestampAux = new Timestamp(date.getTime());
        DeliveryStep state = getCurrentState();
        //To judge which state is robot in through "instance of ", which serve as judge whether the left is the instance of class right
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
            //To terminate an transcation and return to previous values
            em.getTransaction().rollback();
            throw new RuntimeException("This delivery is in an unidentified state!");
        }
        em.merge(newState);
        em.getTransaction().commit();
    }
    /**
     * To judge whether the mail is booked but not finished picking up
     * @return  boolean true, false
     */
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
    /**
     * To judge whether it's picked up but not delivered
     * @return boolean true, false
     */
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
    /**
     * To judge whether it's booked
     * @return boolean true, false
     * @exception RuntimeException if it's in a unidentifield state
     */
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
    /**
     * To get the current state of the robot
     * @return deliveryStep
     */
    public DeliveryStep getCurrentState() {
        EntityManager em = Persistance.getEntityManager();
        DeliveryStep deliveryStep = (DeliveryStep) em.createNamedQuery("Delivery.searchStateListOrderedByDate")
                .setParameter("delivery", this)
                .setMaxResults(1)
                .getSingleResult();
        return deliveryStep;
    }
   /**
    * to get the state list of the delivery
    * @return 
    */ 
    public List<DeliveryStep> getStateList() {
        EntityManager em = Persistance.getEntityManager();
        List<DeliveryStep> deliverySteps = em.createNamedQuery("Delivery.searchStateListOrderedByDate")
                .setParameter("delivery", this)
                .getResultList();
        return deliverySteps;
    }
    /**
     * Define an integer number to map object
     * @return hash
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.id;
        return hash;
    }
    /**
     * get the boolean value of whether it's obj or not
     * @param obj
     * @return boolean true, false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Delivery other = (Delivery) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}

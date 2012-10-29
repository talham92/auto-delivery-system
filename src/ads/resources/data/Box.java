/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author mgamell
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="Box.searchEmpty",
        query="SELECT c FROM Box c WHERE c.delivery IS NULL"
    ),
    @NamedQuery(
        name="Box.searchDelivery",
//        query="SELECT c FROM Box c WHERE c.delivery.sender LIKE :sender AND c.delivery.receiver LIKE :receiver AND c.delivery.timestampfield like :timestampfield"
        query="SELECT c FROM Box c WHERE c.delivery"
    )
})
public class Box implements Serializable {
    private static final long serialVersionUID = 3L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    private Delivery delivery;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Box)) {
            return false;
        }
        Box other = (Box) object;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ads.data.Box[ id=" + id + " ]";
    }
    
}

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

/**
 *
 * @author mgamell
 */
@Entity
public class Office implements Serializable {
    private static final long serialVersionUID = 8L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String officeAddress;
    
    public Long getId() {
        return id;
    }
    public Office(){}
    public Office(String officeAddress){
        this.officeAddress=officeAddress;
    }
    
    public String getOfficeAddress(){
        return officeAddress;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Office)) {
            return false;
        }
        Office other = (Office) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ads.data.Office[ id=" + id + " ]";
    }
    
}

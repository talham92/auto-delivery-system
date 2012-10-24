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
    private String officeAddress;
    

    public Office(){}
    public Office(String officeAddress){
        this.officeAddress=officeAddress;
    }

    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officeAddress != null ? officeAddress.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Office)) {
            return false;
        }
        Office other = (Office) object;
        if ((this.officeAddress == null && other.officeAddress != null) || (this.officeAddress != null && !this.officeAddress.equals(other.officeAddress))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ads.data.Office[ officeAddress=" + officeAddress + " ]";
    }
    
}

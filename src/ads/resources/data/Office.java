/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author mgamell
 */
@Entity
public class Office implements Serializable {
    private static final long serialVersionUID = 8L;
    @Id
    private String officeAddress;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office nextOffice;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office previousOffice;

    public Office(){}

    // todo: eliminate this constructor
    public Office(String officeAddress){
        this.officeAddress=officeAddress;
    }

    public Office(String officeAddress, Office nextOffice, Office previousOffice) {
        this.officeAddress = officeAddress;
        this.nextOffice = nextOffice;
        this.previousOffice = previousOffice;
    }

    
    
    
    public String getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public Office getNextOffice() {
        return nextOffice;
    }

    public void setNextOffice(Office nextOffice) {
        this.nextOffice = nextOffice;
    }

    public Office getPreviousOffice() {
        return previousOffice;
    }

    public void setPreviousOffice(Office previousOffice) {
        this.previousOffice = previousOffice;
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

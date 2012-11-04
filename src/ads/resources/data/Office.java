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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author mgamell
 * 
 * WHERE o.officeAddress LIKE :officeAddress
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name="Office.findAll",
        query="SELECT o FROM Office o"
    ),
    @NamedQuery(
        name="Office.findByOfficeAdress",
        query="SELECT o FROM Office o WHERE o.officeAddress LIKE :officeAddress"
    )
})
public class Office implements Serializable {
    private static final long serialVersionUID = 8L;
    @Id
    private String officeAddress;
    private String nextOfficeAddress;
    private String preOfficeAddress;
    private String nextOfficeDir;
    private String nextOfficeDist;
    private String preOfficeDir;
    private String preOfficeDist;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office nextOffice;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office preOffice;

    public Office(){}

    // todo: eliminate this constructor
    public Office(String officeAddress){
        this.officeAddress=officeAddress;
        this.nextOfficeAddress = null;
        this.preOfficeAddress = null;
        this.preOfficeDir=null;
        this.preOfficeDist=null;
        this.nextOfficeDir=null;
        this.nextOfficeDist=null;
        //for now
        this.nextOffice=null;
        this.preOffice=null;
    }

    public Office(String officeAddress, String nextOfficeAddress, String preOfficeAddress,
                  String preOfficeDir, String preOfficeDist, String nextOfficeDir, String nextOfficeDist) {
        this.officeAddress = officeAddress;
        this.nextOfficeAddress = nextOfficeAddress;
        this.preOfficeAddress = preOfficeAddress;
        this.preOfficeDir=preOfficeDir;
        this.preOfficeDist=preOfficeDist;
        this.nextOfficeDir=nextOfficeDir;
        this.nextOfficeDist=nextOfficeDist;
        //for now
        this.nextOffice=null;
        this.preOffice=null;
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
        return preOffice;
    }

    public void setPreviousOffice(Office previousOffice) {
        this.preOffice = previousOffice;
    }

    public String getNextOfficeAddress() {
        return nextOfficeAddress;
    }

    public void setNextOfficeAddress(String nextOfficeAddress) {
        this.nextOfficeAddress = nextOfficeAddress;
    }

    public String getNextOfficeDir() {
        return nextOfficeDir;
    }

    public void setNextOfficeDir(String nextOfficeDir) {
        this.nextOfficeDir = nextOfficeDir;
    }

    public String getNextOfficeDist() {
        return nextOfficeDist;
    }

    public void setNextOfficeDist(String nextOfficeDist) {
        this.nextOfficeDist = nextOfficeDist;
    }

    public String getPreOfficeAddress() {
        return preOfficeAddress;
    }

    public void setPreOfficeAddress(String preOfficeAddress) {
        this.preOfficeAddress = preOfficeAddress;
    }

    public String getPreOfficeDir() {
        return preOfficeDir;
    }

    public void setPreOfficeDir(String preOfficeDir) {
        this.preOfficeDir = preOfficeDir;
    }

    public String getPreOfficeDist() {
        return preOfficeDist;
    }

    public void setPreOfficeDist(String preOfficeDist) {
        this.preOfficeDist = preOfficeDist;
    }

    public Office getPreOffice() {
        return preOffice;
    }

    public void setPreOffice(Office preOffice) {
        this.preOffice = preOffice;
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

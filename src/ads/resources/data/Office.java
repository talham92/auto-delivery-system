/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

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
    // TODO: MARC: MEHMET! I KNOW I SHOULD TOUCH THIS CLASS, BUT WE SHOULD HAVE A NUMERIC ID, AND ID=0 MUST BE COMPULSORY REPRESENTING POINT 0.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String officeAddress;
    @Transient
    private String nextOfficeAddress;
    @Transient
    private String preOfficeAddress;
    private String nextOfficeDir;
    private String nextOfficeDist;
    @Transient
    private String preOfficeDir;
    @Transient
    private String preOfficeDist;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office nextOffice;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office preOffice;

    public Office(){}


    public Office(String officeAddress, String nextOfficeAddress, String preOfficeAddress,
                  String preOfficeDir, String preOfficeDist, String nextOfficeDir, String nextOfficeDist, Office nextOffice, Office preOffice) {
        this.officeAddress = officeAddress;
        this.nextOfficeAddress = nextOfficeAddress;
        this.preOfficeAddress = preOfficeAddress;
        this.preOfficeDir=preOfficeDir;
        this.preOfficeDist=preOfficeDist;
        this.nextOfficeDir=nextOfficeDir;
        this.nextOfficeDist=nextOfficeDist;
        //for now
        this.nextOffice=nextOffice;
        this.preOffice=preOffice;
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
    }

    public Office(String officeAddress,
                  String preOfficeDir, String preOfficeDist, String nextOfficeDir, String nextOfficeDist, Office nextOffice, Office preOffice) {
        this.officeAddress = officeAddress;
        this.preOfficeDir=preOfficeDir;
        this.preOfficeDist=preOfficeDist;
        this.nextOfficeDir=nextOfficeDir;
        this.nextOfficeDist=nextOfficeDist;
        //for now
        this.nextOffice=nextOffice;
        this.preOffice=preOffice;
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

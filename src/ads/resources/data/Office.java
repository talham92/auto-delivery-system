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
/**Define the data structure of office
 * 
 */
public class Office implements Serializable {
    // Define a static constant
    private static final long serialVersionUID = 8L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }
    /**
     * set the id as the input string
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    /*
     * define several private variables of offcie class
     */
    private String officeAddress;
    //todo: @Transient
    private String nextOfficeAddress;
    //todo: @Transient
    private String preOfficeAddress;
    private String nextOfficeDir;
    private String nextOfficeDist;
    //todo: @Transient
    private String preOfficeDir;
    //todo: @Transient
    private String preOfficeDist;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office nextOffice;
    @OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    private Office preOffice;

    /**
     *
     */
    public Office(){}

    /**
     * The constructor of office class with 9 variables
     * @param officeAddress
     * @param nextOfficeAddress
     * @param preOfficeAddress
     * @param preOfficeDir
     * @param preOfficeDist
     * @param nextOfficeDir
     * @param nextOfficeDist
     * @param nextOffice
     * @param preOffice 
     */
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
    /**
     * The constructor of office class with 7 variables
     * @param officeAddress
     * @param nextOfficeAddress
     * @param preOfficeAddress
     * @param preOfficeDir
     * @param preOfficeDist
     * @param nextOfficeDir
     * @param nextOfficeDist 
     */
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
    /**
     * The constructor of office class with 7 variables
     * @param officeAddress
     * @param preOfficeDir
     * @param preOfficeDist
     * @param nextOfficeDir
     * @param nextOfficeDist
     * @param nextOffice
     * @param preOffice 
     */
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
    /**
     * Get the string value of office address
     * @return string officeAddress
     */
    public String getOfficeAddress() {
        return officeAddress;
    }
    /**
     * Set the office address as the input string
     * @param officeAddress 
     */
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }
    /**
     * Get the string value of next office
     * @return string nextOffice
     */
    public Office getNextOffice() {
        return nextOffice;
    }
    /**
     * Set the next office as the input string
     * @param nextOffice 
     */
    public void setNextOffice(Office nextOffice) {
        this.nextOffice = nextOffice;
    }
    /**
     * Get the string value of previous office
     * @return string preoffice
     */
    public Office getPreviousOffice() {
        return preOffice;
    }
    /**
     * Set the previous office as the input string
     * @param previousOffice 
     */
    public void setPreviousOffice(Office previousOffice) {
        this.preOffice = previousOffice;
    }
    /**
     * Get the string value of next office address
     * @return string nextOfficeAddress
     */
    public String getNextOfficeAddress() {
        return nextOfficeAddress;
    }
    /**
     * Set the next office address as the input string
     * @param nextOfficeAddress 
     */
    public void setNextOfficeAddress(String nextOfficeAddress) {
        this.nextOfficeAddress = nextOfficeAddress;
    }
    /**
     * Get the string value of next office direction
     * @return string getNextOfficeDir
     */
    public String getNextOfficeDir() {
        return nextOfficeDir;
    }
    /**
     * Set the next office direction as the input string
     * @param nextOfficeDir 
     */
    public void setNextOfficeDir(String nextOfficeDir) {
        this.nextOfficeDir = nextOfficeDir;
    }
    /**
     * Get the string value of next office distance
     * @return string nextOfficeDist
     */
    public String getNextOfficeDist() {
        return nextOfficeDist;
    }
    /**
     * Set the next office distance as the input string
     * @param nextOfficeDist 
     */
    public void setNextOfficeDist(String nextOfficeDist) {
        this.nextOfficeDist = nextOfficeDist;
    }
    /**
     * Get the string value of previous office address
     * @return string preOfficeAddress
     */
    public String getPreOfficeAddress() {
        return preOfficeAddress;
    }
    /**
     * Set the previous office address as the input string
     * @param preOfficeAddress 
     */
    public void setPreOfficeAddress(String preOfficeAddress) {
        this.preOfficeAddress = preOfficeAddress;
    }
    /**
     * Get the string value of previoud office direction
     * @return string preOfferDir
     */
    public String getPreOfficeDir() {
        return preOfficeDir;
    }
    /**
     * Set the previous office direction as the input string
     * @param preOfficeDir 
     */
    public void setPreOfficeDir(String preOfficeDir) {
        this.preOfficeDir = preOfficeDir;
    }
    /**
     * Get the string value of previous office distance
     * @return string preOfficeDist
     */
    public String getPreOfficeDist() {
        return preOfficeDist;
    }
    /**
     * Set the previous office distance as the input string
     * @param preOfficeDist 
     */
    public void setPreOfficeDist(String preOfficeDist) {
        this.preOfficeDist = preOfficeDist;
    }
    /**
     * Get the string value of previous office
     * @return string preOffice
     */
    public Office getPreOffice() {
        return preOffice;
    }
    /**
     * Set the previous office as the input string
     * @param preOffice 
     */
    public void setPreOffice(Office preOffice) {
        this.preOffice = preOffice;
    }
    
    /**
     * Define an integer number to map object
     * @return int hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officeAddress != null ? officeAddress.hashCode() : 0);
        return hash;
    }
    /**To judge whether the input string is legal or not
     * 
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Office)) {
            return false;
        }
        Office other = (Office) object;
        if ((this.officeAddress == null && other.officeAddress != null) || (this.officeAddress != null && !this.officeAddress.equals(other.officeAddress))) {
            return false;
        }
        return true;
    }
    /**
     * Change the officeAddress into string type
     * @return string OfficeAddress
     */
    @Override
    public String toString() {
        return "ads.data.Office[ officeAddress=" + officeAddress + " ]";
    }
    
}

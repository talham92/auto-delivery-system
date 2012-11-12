package ads.resources.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 * Class representing a User.
 * @author mgamell
 */
//Declare this is Entity bean
@Entity
@NamedQueries({
    @NamedQuery(
        name="User.findAll",
        query="SELECT c FROM ADSUser c"
    ),
    @NamedQuery(
        name="User.searchByName",
        query="SELECT c FROM ADSUser c WHERE c.firstName LIKE :firstName AND c.lastName LIKE :lastName"
    ),
////        query="SELECT c FROM ADSUser c WHERE UPPER(c.firstName) LIKE UPPER(CONCAT(:nameUser,'%'))"
//        query="SELECT c FROM ADSUser c WHERE UPPER(c.firstName) LIKE UPPER(':name%') OR UPPER(c.lastName) LIKE UPPER(':name%')" +
//        " UNION " + "SELECT c FROM ADSUser c WHERE UPPER(c.firstName) LIKE UPPER('% :name%') OR UPPER(c.lastName) LIKE UPPER('% :name%')" +
//        " UNION " + "SELECT c FROM ADSUser c WHERE UPPER(c.firstName) LIKE UPPER('%:name%') OR UPPER(c.lastName) LIKE UPPER('%:name%')" +
//        " UNION " + "SELECT c FROM ADSUser c WHERE CONCAT(UPPER(c.firstName),UPPER(c.lastName)) LIKE UPPER('%:name%')"
    @NamedQuery(
        name="User.searchByPartialNameES",
        query="SELECT c FROM ADSUser c WHERE c.firstName LIKE CONCAT(:name,'%') OR c.lastName LIKE CONCAT(:name,'%')"
    ),
    @NamedQuery(
        name="User.searchByPartialNameSSES",
        query="SELECT c FROM ADSUser c WHERE c.firstName LIKE CONCAT('% ',CONCAT(:name,'%')) OR c.lastName LIKE CONCAT('% ',CONCAT(:name,'%'))"
    ),
    @NamedQuery(
        name="User.searchByPartialNameSES",
        query="SELECT c FROM ADSUser c WHERE c.firstName LIKE CONCAT('%',CONCAT(:name,'%')) OR c.lastName LIKE CONCAT('%',CONCAT(:name,'%'))"
    ),
    @NamedQuery(
        name="User.searchByPartialNameCFLSES",
        query="SELECT c FROM ADSUser c WHERE CONCAT(c.firstName,c.lastName) LIKE CONCAT('%',CONCAT(:name,'%'))"
    ),
    @NamedQuery(
        name="User.searchByOffice",
        query="SELECT c FROM ADSUser c JOIN c.office o WHERE o.officeAddress LIKE :office"
    )
})

public class ADSUser implements Serializable {
    /** Define the properties and constant of user
     * 
     */
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    @ManyToOne(fetch=FetchType.LAZY)
//    @JoinColumn(nullable=false)
    private Office office;
    private String email;
    @Id
    private String username;
    private String password;
    private boolean administrator;
    
    public ADSUser() {
    }
    /**
     * define the user information and type
     * @param firstName this records the first name of user
     * @param lastName  this records the last name of user
     * @param office    this records the office address of user
     * @param email     this records the email of user
     * @param username  this records the user name of user
     * @param password  this records the password of user
     */
    public ADSUser(String firstName, String lastName, Office office, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.office = office;
        this.email = email;
        this.username = username;
        this.password = password;
        this.administrator = false;
    }
    /** An integer number to map and recognize object
     * 
     * @return integer hash
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }
    /**To judge whether the input string is legal or not
     * 
     * @param object
     * @return boolean
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ADSUser)) {
            return false;
        }
        ADSUser other = (ADSUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }
    /**Input string with user name
     * 
     * @return string "ads.data.User[ username=" + username + " ]"
     */
    @Override
    public String toString() {
        return "ads.data.User[ username=" + username + " ]";
    }
    /**To let the administrator of ADSUser class to get the boolean value of firstname
     * 
     * @return string firstname
     */
    public String getFirstName() {
        return firstName;
    }
    /**To set the string value of first name to the administrator of ADSuser class
     * 
     * @param string firstName 
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**To let the administrator of ADSUser class to get the boolean value of lastname
     * 
     * @return string lastname
     */
    public String getLastName() {
        return lastName;
    }
    /**To set the string value of last name to the administrator of ADSuser class
     * 
     * @param string lastName 
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**To let the administrator of ADSUser class to get the boolean value of mail
     * 
     * @return string mail
     */
    public String getEmail() {
        return email;
    }
    /**To set the string value of email to the administrator of ADSuser class
     * 
     * @param string email 
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**To let the administrator of ADSUser class to get the boolean value of user name
     * 
     * @return string username
     */
    public String getUsername() {
        return username;
    }
    /**
     * set the user name as the input string
     * @param string username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
    * get the string value of password
    * @return string password
    */
    public String getPassword() {
        return password;
    }
    /**To set the string value of password to the administrator of ADSuser class
     * 
     * @param password 
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**To get the office information through call of function getoffice() 
     * and then to set the value of office of ADSuser class
     * @return string office
     */
    public Office getOffice() {
        return office;
    }
    /** To set the string value of office to the administrator of ADSuser class
     * 
     * @param String office 
     */
    public void setOffice(Office office) {
        this.office = office;
    }
    /** To let the administrator of ADSUser class to get the boolean value of admin
     * 
     * @return it records whether the user is administrator or not
     */
    public boolean isAdmin() {
        return administrator;
    }
    /** To set the boolean value of admin to the administrator of ADSuser class 
     * 
     * @param boolean admin it judges whether it's administrator or not 
     */
    public void setAdmin(boolean admin) {
        this.administrator = admin;
    }
}

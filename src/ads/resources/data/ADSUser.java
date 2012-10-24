package ads.resources.data;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Class representing a User.
 * @author mgamell
 */
@Entity
@NamedQueries({
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
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
//    @JoinColumn(nullable=false)
    private Office office;
    private String email;
    @Id
    private String username;
    private String password;
    private boolean admin;
    
    public ADSUser() {
    }

    public ADSUser(String firstName, String lastName, Office office, String email, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.office = office;
        this.email = email;
        this.username = username;
        this.password = password;
        this.admin = false;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ADSUser)) {
            return false;
        }
        ADSUser other = (ADSUser) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ads.data.User[ username=" + username + " ]";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}

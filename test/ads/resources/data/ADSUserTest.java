/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tonywang
 */
public class ADSUserTest {
    
    public ADSUserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hashCode method, of class ADSUser.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        int expResult = 110770295;
        int result = ads.hashCode();
        //System.out.println("hash code is" + ads.hashCode());
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class ADSUser.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        
        Object object = new ADSUser("Qiannan","Li",
                new Office("603","604","602","-x","5","y","6"), 
                "t@a.c", "twang","1111");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        boolean expResult = true;
        boolean result = ads.equals(object);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ADSUser.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "ads.data.User[ username=twang ]";
        String result = ads.toString();
        assertEquals(expResult, result);     
    }

    /**
     * Test of getFirstName method, of class ADSUser.
     */
    @Test
    public void testGetFirstName() {
        System.out.println("getFirstName");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "Tuo";
        String result = ads.getFirstName();
        assertEquals(expResult, result);  
    }

    /**
     * Test of setFirstName method, of class ADSUser.
     */
    @Test
    public void testSetFirstName() {
        System.out.println("setFirstName");
        String firstName = "Tony";
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        ads.setFirstName(firstName);
    }

    /**
     * Test of getLastName method, of class ADSUser.
     */
    @Test
    public void testGetLastName() {
        System.out.println("getLastName");

        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "Wang";
        String result = ads.getLastName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLastName method, of class ADSUser.
     */
    @Test
    public void testSetLastName() {
        System.out.println("setLastName");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String lastName = "WANG";
        ads.setLastName(lastName);
    }

    /**
     * Test of getEmail method, of class ADSUser.
     */
    @Test
    public void testGetEmail() {
        System.out.println("getEmail");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "t@a.c";
        String result = ads.getEmail();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEmail method, of class ADSUser.
     */
    @Test
    public void testSetEmail() {
        System.out.println("setEmail");
       
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String email = "tuowang@a.c";
        ads.setEmail(email);
    }

    /**
     * Test of getUsername method, of class ADSUser.
     */
    @Test
    public void testGetUsername() {
        System.out.println("getUsername");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");
        
        String expResult = "twang";
        String result = ads.getUsername();
        assertEquals(expResult, result);
    }

    /**
     * Test of setUsername method, of class ADSUser.
     */
    @Test
    public void testSetUsername() {
        System.out.println("setUsername");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  

        String username = "tonyw";
        ads.setUsername(username);
    }

    /**
     * Test of getPassword method, of class ADSUser.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        String expResult = "1111";
        String result = ads.getPassword();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPassword method, of class ADSUser.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String password = "1234";
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        ads.setPassword(password);
    }

    /**
     * Test of getOffice method, of class ADSUser.
     */
    @Test
    public void testGetOffice() {
        System.out.println("getOffice");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        Office expResult = new Office("604","605","603","-x","3","y","4");
        Office result = ads.getOffice();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOffice method, of class ADSUser.
     */
    @Test
    public void testSetOffice() {
        System.out.println("setOffice");      
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        Office office = new Office("602","603","601","-y","9","x","5");
        ads.setOffice(office);
    }

    /**
     * Test of isAdmin method, of class ADSUser.
     */
    @Test
    public void testIsAdmin() {
        System.out.println("isAdmin");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        
        boolean expResult = false;
        boolean result = ads.isAdmin();
        assertEquals(expResult, result);
    }

    /**
     * Test of setAdmin method, of class ADSUser.
     */
    @Test
    public void testSetAdmin() {
        System.out.println("setAdmin");
        ADSUser ads = new ADSUser("Tuo","Wang",
                new Office("604","605","603","-x","3","y","4"), 
                "t@a.c", "twang","1111");  
        boolean admin = true;

        ads.setAdmin(admin);
    }
}

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
public class OfficeTest {
    
    public OfficeTest() {
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
     * Test of getId method, of class Office.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Office office = new Office("604","605","603","-x","3","y","4");
        int expResult = 0;
        int result = office.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Office.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 3;
         Office office = new Office("604","605","603","-x","3","y","4");
        office.setId(id);
    }

    /**
     * Test of getOfficeAddress method, of class Office.
     */
    @Test
    public void testGetOfficeAddress() {
        System.out.println("getOfficeAddress");
         Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "604";
        String result = office.getOfficeAddress();
        assertEquals(expResult, result);
        //System.out.println("Office address is " + office.getOfficeAddress());    
    }

    /**
     * Test of getNextOfficeAddress method, of class Office.
     */
    @Test
    public void testGetNextOfficeAddress() {
        System.out.println("getNextOfficeAddress");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "605";
        String result = office.getNextOfficeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setNextOfficeAddress method, of class Office.
     */
    @Test
    public void testSetNextOfficeAddress() {
        System.out.println("setNextOfficeAddress");
        String nextOfficeAddress = "607";
        Office office = new Office("604","605","603","-x","3","y","4");
        office.setNextOfficeAddress(nextOfficeAddress);
    }

    /**
     * Test of getPreOfficeAddress method, of class Office.
     */
    @Test
    public void testGetPreOfficeAddress() {
        System.out.println("getPreOfficeAddress");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "603";
        String result = office.getPreOfficeAddress();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPreOfficeAddress method, of class Office.
     */
    @Test
    public void testSetPreOfficeAddress() {
        System.out.println("setPreOfficeAddress");
        String preOfficeAddress = "602";
        Office office = new Office("604","605","603","-x","3","y","4");
        office.setPreOfficeAddress(preOfficeAddress);
    }

    /**
     * Test of hashCode method, of class Office.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Office office = new Office("604","605","603","-x","3","y","4");
        int expResult = 53434;
        int result = office.hashCode();
        assertEquals(expResult, result);
        }

    /**
     * Test of toString method, of class Office.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Office office = new Office("604","605","603","-x","3","y","4");
        String expResult = "ads.data.Office[ officeAddress=604 ]";
        String result = office.toString();
        //assertEquals(expResult, result);
        System.out.println(office.toString());
    }
}

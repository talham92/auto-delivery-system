/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import javax.swing.JTextArea;
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
public class AdminCreateFloorMapViewTest {
    
    public AdminCreateFloorMapViewTest() {
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
     * Test of drawAction method, of class AdminCreateFloorMapView.
     */
    @Test (expected = Exception.class)
    public void testDrawAction() throws Exception{
        System.out.println("drawAction");
        AdminCreateFloorMapView floormap = new AdminCreateFloorMapView();
        floormap.drawAction();
    }

    /**
     * Test of getOutputText method, of class AdminCreateFloorMapView.
     */
    @Test
    public void testGetOutputText() {
        System.out.println("getOutputText");
        
        AdminCreateFloorMapView floormap = new AdminCreateFloorMapView();
        JTextArea result = floormap.getOutputText();
    }

    /**
     * Test of main method, of class AdminCreateFloorMapView.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] arasdas = {"s","t","a","r","t"};
        AdminCreateFloorMapView.main(arasdas);
    }
}

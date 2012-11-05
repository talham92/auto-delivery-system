/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.NonBookedDeliveryException;
import ads.logic.ServerControllerInterface;
import ads.logic.SystemStatus;
import ads.resources.data.ADSUser;
import ads.resources.data.Delivery;
import ads.resources.data.Office;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.swing.JOptionPane;

/**
 *
 * @author mgamell
 */
public class ClientController implements ClientControllerInterface {

    public static void main(String[] args) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void stateNonLoggedIn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateNonLoggedIn(UserMainView viewToDispose) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateNonLoggedIn(AdminMainView viewToDispose) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void login(String username, String password, LoginView login) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToRegister(LoginView login) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void register(String firstName, String lastName, String roomNumber, String email, String username, String password, String password1, RegisterView register) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToBookDelivery() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String[]> searchUser_NameOffice(String name, String office) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void bookDelivery(double urgency, List<String> targetList, BookDeliveryView bookDeliveryView) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToCreateFloorMap(AdminMainView v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SystemStatus getSystemStatus() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToTrackDeliveryStatus(UserMainView view, DeliveryStatusView deliveryStatusView) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToSeeDeliveryDetails(DeliveryStatusView deliveryStatusView, int deliveryId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createFloorMap(String text, AdminCreateFloorMapView v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clearOffices(AdminCreateFloorMapView v) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createLinksBtwOffices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<String[]> getMapDrawingArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stateAdminMain() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wantsToLookAtDynamicViewOfMap(AdminMainView aThis) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

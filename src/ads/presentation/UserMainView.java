/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import adsrobotstub.RobotStub;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * This class contains all components and logic when user has successfully
 * logged in to the system
 * <p>
 * The user main view has two tabs, one is "Book Delivery", the other is 
 * "Delivery Status",by clicking different tabs, user can view either 
 * book delivery condition or delivery status.
 * 
 * @author mgamell
 */
public class UserMainView extends javax.swing.JFrame {
    //log the current class information
    private static Logger logger = Logger.getLogger(UserMainView.class.getName());
    //object of ClientControllerInterface, which is used to change state
    //by calling methods in ClientControllerInterface
    private ClientControllerInterface controller;
    //Object of UserMainView, which is used as parameter to change current state.
    private UserMainView thisView;
    //object of DeliveryStatusView, which is used as parameter to change current state
    private DeliveryStatusView deliveryStatusView;
    //object of BookDeliveryView, which is used as parameter to change to book 
    //delivery state
    private BookDeliveryView bookDeliveryView;
    /**
     * Creates new form UserMainView
     * 
     * @param c used to passing object from controller to current class
     */
    public UserMainView(ClientControllerInterface c) {
        thisView = this;
        controller = c;
        initComponents();
        bookDeliveryView = new BookDeliveryView(controller);
        jTabbedPane1.addTab("Book Delivery", bookDeliveryView);
        deliveryStatusView = new DeliveryStatusView(controller);
        jTabbedPane1.addTab("Track Status", deliveryStatusView);
        //change view of window when title of tab has changed
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String title = jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
                
                //if title equals "Book Delivery", the window will show info about "book delivery"
                //if title equals "Track Status", the window will show info about "Delivery Status"
                if(title.equals("Book Delivery")) {
                    controller.wantsToBookDelivery();
                } else if(title.equals("Track Status")) {
                    controller.wantsToTrackDeliveryStatus(thisView, deliveryStatusView);
                } else {
                    logger.finest("Error! choose tab title: "+jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()));
                    JOptionPane.showMessageDialog(thisView,
                        "Error! choose tab title: "+jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()),
                        "Fatal Error",
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(-1);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        logoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 707, Short.MAX_VALUE)
                        .addComponent(logoutButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logoutButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * This method responses to the situation when user clicked the logout button
     * 
     * @param evt the event that user clicked the Logout button
     */
    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        //change state to STATE_NON_LOGGED_IN and show log-in window
        controller.stateNonLoggedIn(this);
    }//GEN-LAST:event_logoutButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton logoutButton;
    // End of variables declaration//GEN-END:variables
}

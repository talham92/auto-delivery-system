/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mgamell
 */
public final class DeliveryStatusView extends javax.swing.JPanel {
    private ClientControllerInterface controller;
    DeliveryStatusView thisDeliveryStatusView;
    /**
     * Creates new form DeliveryStatusView
     */
    public DeliveryStatusView(ClientControllerInterface c) {
        thisDeliveryStatusView = this;
        controller = c;
        initComponents();
        reset();
        deliveriesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 || e.getClickCount() == 2) {
                    int rowIndex = deliveriesTable.getSelectedRow();
                    int selectedId = Integer.parseInt((String)deliveriesTable.getValueAt(rowIndex, 0));
                    controller.wantsToSeeDeliveryDetails(thisDeliveryStatusView, selectedId);
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

        deliveriesLabel = new javax.swing.JLabel();
        deliveriesScrollPane = new javax.swing.JScrollPane();
        deliveriesTable = new javax.swing.JTable();
        deliveryDetailsScrollPane = new javax.swing.JScrollPane();
        deliveryDetailsTable = new javax.swing.JTable();
        deliveryDetailsLabel = new javax.swing.JLabel();
        deliveryDetailsIDLabel = new javax.swing.JLabel();

        deliveriesLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        deliveriesLabel.setText("Deliveries");

        deliveriesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Time booking", "Sender", "Receiver", "Priority"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        deliveriesTable.getTableHeader().setReorderingAllowed(false);
        deliveriesScrollPane.setViewportView(deliveriesTable);
        deliveriesTable.getColumnModel().getColumn(0).setPreferredWidth(10);
        deliveriesTable.getColumnModel().getColumn(1).setPreferredWidth(150);

        deliveryDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Time", "Event"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        deliveryDetailsTable.getTableHeader().setReorderingAllowed(false);
        deliveryDetailsScrollPane.setViewportView(deliveryDetailsTable);
        deliveryDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(150);

        deliveryDetailsLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        deliveryDetailsLabel.setText("Delivery details");

        deliveryDetailsIDLabel.setText("id=0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deliveriesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(deliveryDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deliveriesLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(deliveryDetailsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deliveryDetailsIDLabel)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deliveriesLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deliveriesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deliveryDetailsLabel)
                    .addComponent(deliveryDetailsIDLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deliveryDetailsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel deliveriesLabel;
    private javax.swing.JScrollPane deliveriesScrollPane;
    private javax.swing.JTable deliveriesTable;
    private javax.swing.JLabel deliveryDetailsIDLabel;
    private javax.swing.JLabel deliveryDetailsLabel;
    private javax.swing.JScrollPane deliveryDetailsScrollPane;
    private javax.swing.JTable deliveryDetailsTable;
    // End of variables declaration//GEN-END:variables

    public void reset() {
        resetDeliveriesTable();
        resetDeliveryDetailsTable();
        hideDeliveryDetails();
    }

    public void resetDeliveriesTable() {
        // Obtain the model for the table
        DefaultTableModel deliveriesTableModel = (DefaultTableModel) deliveriesTable.getModel();

        // Remove all rows
        int rows = deliveriesTableModel.getRowCount(); 
        for(int i = rows - 1; i >=0; i--) {
            deliveriesTableModel.removeRow(i); 
        }
    }

    public void resetDeliveryDetailsTable() {
        // Obtain the model for the table
        DefaultTableModel deliveryDetailsTableModel = (DefaultTableModel) deliveryDetailsTable.getModel();

        // Remove all rows
        int rows = deliveryDetailsTableModel.getRowCount(); 
        for(int i = rows - 1; i >=0; i--) {
            deliveryDetailsTableModel.removeRow(i); 
        }
    }

    public void addDelivery(int id, Timestamp timestampField, String usernameSender, String usernameReceiver, double priority) {
        // Obtain the model for the table
        DefaultTableModel deliveriesTableModel = (DefaultTableModel) deliveriesTable.getModel();
        deliveriesTableModel.addRow(new String[]{Integer.toString(id), timestampField.toString(), usernameSender, usernameReceiver, Double.toString(priority)});
    }

    public void addDeliveryDetail(String[] s) {
        // Obtain the model for the table
        DefaultTableModel deliveryDetailsTableModel = (DefaultTableModel) deliveryDetailsTable.getModel();
        deliveryDetailsTableModel.addRow(s);
    }

    public void showDeliveryDetails(int deliveryId) {
        deliveryDetailsIDLabel.setText("id = "+deliveryId);
        deliveryDetailsLabel.setVisible(true);
        deliveryDetailsIDLabel.setVisible(true);
        deliveryDetailsTable.setVisible(true);
        deliveryDetailsScrollPane.setVisible(true);
    }

    public void hideDeliveryDetails() {
        deliveryDetailsLabel.setVisible(false);
        deliveryDetailsIDLabel.setVisible(false);
        deliveryDetailsTable.setVisible(false);
        deliveryDetailsScrollPane.setVisible(false);
    }

    
}
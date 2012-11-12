/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MFA, mgamell
 */
public class BookDeliveryView extends javax.swing.JPanel {
    private Set<String[]> targetList;
    private ClientControllerInterface controller;
    /**
     * Creates new form BookDeliveryView
     */
    public BookDeliveryView(ClientControllerInterface c) {
        initComponents();
        targetList=new HashSet();
        resultTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String[] selectedReceiver;
                    selectedReceiver = new String[3];
                    JTable target = (JTable)e.getSource();
                    int rowIndex = target.getSelectedRow();
                    int columns = resultTable.getColumnCount();
                    for(int col = 0; col < columns; col++)  
                    {
                        Object o = resultTable.getValueAt(rowIndex, col);
                        selectedReceiver[col]=o.toString();
                    }

                    // Check if the targetList contains selected receiver
                    boolean selectedReceiverContained = false;
                    for(String[] i : targetList) {
                        if(i[0].equals(selectedReceiver[0])) {
                            selectedReceiverContained = true;
                            break;
                        }
                    }
    
                    // If the targetList already contains the receiver, do not add it.
                    if(!selectedReceiverContained) {
                        DefaultTableModel model_ = (DefaultTableModel) targetTable.getModel();
                        model_.addRow(new Object[]{selectedReceiver[0], selectedReceiver[1]});
                        targetList.add(selectedReceiver);
                    } else {
                        outputText.append("The receiver with username: "+selectedReceiver[0]+" is already in the target list\n");
                    }
                }
            }
        });
        controller = c;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        resultTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        confirmAllButton = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        targetTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        clearAllButton = new javax.swing.JButton();
        nameField = new javax.swing.JTextField();
        officeField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputText = new javax.swing.JTextArea();

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel1.setText("name");

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Search");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Target List:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel2.setText("office");

        resultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Name", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        resultTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(resultTable);

        confirmAllButton.setText("Confirm all");
        confirmAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAllButtonActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
        jComboBox1.setSelectedIndex(1);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jLabel3.setText("Priority (higher is more prioritary)");

        targetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Username", "Name", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        targetTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(targetTable);

        clearAllButton.setLabel("Clear Target List");
        clearAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAllButtonActionPerformed(evt);
            }
        });

        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        officeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        outputText.setColumns(20);
        outputText.setRows(5);
        outputText.setText("Work log\n");
        jScrollPane1.setViewportView(outputText);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(confirmAllButton, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(488, 488, 488))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(searchButton)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addComponent(jLabel2))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(nameField)
                                            .addComponent(officeField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
                    .addComponent(jScrollPane8))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(officeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton)
                        .addGap(0, 77, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmAllButton)
                    .addComponent(clearAllButton)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // Obtain the result for the search
        List<String[]> data;
        data = controller.searchUser_NameOffice(nameField.getText(), officeField.getText());

        // Obtain the model for the table
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();

        // Remove all rows
        int rows = model.getRowCount();
        for(int i = rows - 1; i >=0; i--) {
            model.removeRow(i);
        }

        // Add all the rows from the result
        for(String[] row : data) {
            model.addRow(new Object[]{row[0], row[1], row[2]});
        }

        //jTable2.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //jTable2.getSelectionModel().addListSelectionListener(new TableSelectionHandler());
    }//GEN-LAST:event_searchButtonActionPerformed

    private void confirmAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAllButtonActionPerformed
        List<String> targetUsernames = new ArrayList(targetList.size());

        for(String[] target : targetList) {
            targetUsernames.add(target[0]);
        }

        if(!targetList.isEmpty()){
            controller.bookDelivery(
                ((double)Integer.parseInt(jComboBox1.getSelectedItem().toString()))/((double)jComboBox1.getItemCount()),
                targetUsernames,
                this);
            DefaultTableModel model_3 = (DefaultTableModel) targetTable.getModel();
            while (model_3.getRowCount()>0){
                model_3.removeRow(0);
            }
            targetList.clear();
        }
        else{
            JOptionPane.showMessageDialog(this,
                "There are no deliveries chosen submit",
                "Fail",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_confirmAllButtonActionPerformed

    private void clearAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAllButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) targetTable.getModel();
        while (model.getRowCount()>0){
            model.removeRow(0);
        }
        targetList.clear();
    }//GEN-LAST:event_clearAllButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearAllButton;
    private javax.swing.JButton confirmAllButton;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField officeField;
    private javax.swing.JTextArea outputText;
    private javax.swing.JTable resultTable;
    private javax.swing.JButton searchButton;
    private javax.swing.JTable targetTable;
    // End of variables declaration//GEN-END:variables
}

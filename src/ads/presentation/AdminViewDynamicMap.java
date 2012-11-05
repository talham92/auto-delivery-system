/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 *
 * @author MFA
 */
public class AdminViewDynamicMap extends javax.swing.JFrame {
    private final ClientControllerInterface controller;
    private ArrayList<String[]> officeDrawingItems;
    /**
     * Creates new form AdminViewDynamicMap
     */
    public AdminViewDynamicMap() {
        initComponents();
        controller=null;
    }
    public AdminViewDynamicMap(ClientControllerInterface c) {
        initComponents();
        controller=c;
        
        officeDrawingItems=new ArrayList<>();
        dynamicFloorMap.setBackground(Color.WHITE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dynamicFloorMap = new javax.swing.JPanel(){
            @Override
            public void paintComponent(Graphics g)   {
                // Paint background
                super.paintComponent(g);

                // Get the drawing area
                int xt=10;
                int yt=10;
                int x=10;
                int y=10;
                int ovalL=10;
                // Set current drawing color
                g.setColor (Color.BLACK);
                String[] ts;
                // Draw a circle around the mid-point
                for(int i=0;i<officeDrawingItems.size();i++)
                {
                    ts=officeDrawingItems.get(i);
                    //unitSign for adapting drawing to the negative dists
                    //g.fillOval(x, y, 2, 2);
                    if(ts[0].equals("end"))
                    {
                        g.drawOval(x-(ovalL/2), y-(ovalL/2), ovalL, ovalL);
                        g.fillOval(x-(ovalL/2), y-(ovalL/2), ovalL, ovalL);
                    }
                    else
                    {
                        int unitSign=Integer.parseInt(ts[2])/Math.abs(Integer.parseInt(ts[2]));
                        if(ts[0].equals("start"))
                        {
                            g.drawOval(x-(ovalL/2), y-(ovalL/2), ovalL, ovalL);
                            g.fillOval(x-(ovalL/4), y-(ovalL/4), ovalL/2, ovalL/2);
                        }
                        else
                        {
                            g.drawOval(x-(ovalL/2), y-(ovalL/2), ovalL, ovalL);
                        }
                        if(ts[1].equals("x"))
                        {
                            xt=x + 10*Integer.parseInt(ts[2]);
                            yt=y;
                            g.drawLine(x+unitSign*ovalL/2, y, xt-unitSign*ovalL/2, yt);
                        }
                        else if(ts[1].equals("y"))
                        {
                            xt=x;
                            yt=y + 10*Integer.parseInt(ts[2]);
                            g.drawLine(x, y+ovalL/2, xt, yt-ovalL/2);
                        }
                        x=xt;
                        y=yt;
                    }
                }
            }
        };
        jScrollPane8 = new javax.swing.JScrollPane();
        targetTable = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;   //Disallow the editing of any cell
            }
        };
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout dynamicFloorMapLayout = new javax.swing.GroupLayout(dynamicFloorMap);
        dynamicFloorMap.setLayout(dynamicFloorMapLayout);
        dynamicFloorMapLayout.setHorizontalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        dynamicFloorMapLayout.setVerticalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );

        targetTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sender", "Receiver", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(targetTable);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Pending Delivery List:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(dynamicFloorMap, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addGap(24, 24, 24)
                .addComponent(dynamicFloorMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public void drawAction()
    {
        officeDrawingItems=controller.getMapDrawingArray();
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            String[] ts=officeDrawingItems.get(i);
            System.out.println(ts[0]+" "+ts[1]+" "+ts[2]+" "+ts[3]+" "+ts[4]);
        }
        //Redraw the panel
        dynamicFloorMap.repaint();
        //Untill now only the offices are drawn also robot should also be drawn
        
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminViewDynamicMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminViewDynamicMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminViewDynamicMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminViewDynamicMap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminViewDynamicMap().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dynamicFloorMap;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable targetTable;
    // End of variables declaration//GEN-END:variables
}
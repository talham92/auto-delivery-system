/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.presentation;

import ads.logic.SystemStatus;
import adsrobotstub.RobotStub;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//TODO: remove all System.out.println and put logger.debug, instead!
/**
 * This class will get system status and show the status in the window
 *
 * @author mgamell
 */
public class SystemStatusView extends javax.swing.JPanel {
    private static Logger logger = Logger.getLogger(SystemStatusView.class.getName());
    private ClientControllerInterface controller;
    private SystemStatusView thisView;
    private DeliveryStatusView deliveryStatusView;
    private ArrayList<String[]> officeDrawingItems;
    private String robotPosition;

    /**
     * Creates new form StatusPanel
     * @param c 
     */
    public SystemStatusView(ClientControllerInterface c) {
        thisView = this;
        controller = c;
        officeDrawingItems=new ArrayList();
        initComponents();
        jTabbedPane1.add("Deliveries", deliveryStatusView = new DeliveryStatusView(c));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        //get System Status from ClientCotroller
//                        System.out.println("updating");
                        SystemStatus status = controller.getSystemStatus();
//                        System.out.println(" received "+status.getPosition()+" "+status.isMoving());
                        if(status != null && status.isServerInitialized()) {
                            position.setText(status.getRobotPosition());
                            isMoving.setSelected(status.isRobotIsMoving());
                            usersInPosition.setText(status.getUsersInPosition());
                            drawAction(status.getRobotPosition());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(thisView,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SystemStatusView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();
        //Listens to the tab select changing between "Robot Status" and "Deliveries"
        jTabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String title = jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
                if(title.equals("Robot Status")) {
                } else if(title.equals("Deliveries")) {
                    controller.wantsToTrackDeliveryStatusAdmin(thisView, deliveryStatusView);
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
     * draw map into the system status view window
     * 
     * @param g object of Graphics
     */
    private void paintMap(Graphics g)
    {
        // Get the drawing area
        int xt=10;
        int yt=10;
        int ovalL=10;
        double reducerX = calculateReducerX();
        double reducerY = calculateReducerY();
        int x=10+(int) (calculateStartX()*reducerX);
        int y=10+(int) (calculateStartY()*reducerY);
        
        
        // Set current drawing color
        g.setColor (Color.BLACK);
        String[] ts;
        // Draw a circle around the mid-point
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            ts=officeDrawingItems.get(i);
            if(ts[0].equals(robotPosition)) {
                g.setColor (Color.RED);
            } else {
                g.setColor (Color.BLACK);
            }

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
                g.setColor (Color.BLACK);
                if(ts[1].equals("x"))
                {
                    xt=x + (int) (reducerX*Integer.parseInt(ts[2]));
                    yt=y;
                    g.drawLine(x+unitSign*ovalL/2, y, xt-unitSign*ovalL/2, yt);
                }
                else if(ts[1].equals("y"))
                {
                    xt=x;
                    yt=y + (int) (reducerY*Integer.parseInt(ts[2]));
                    g.drawLine(x, y+ovalL/2, xt, yt-ovalL/2);
                }
                x=xt;
                y=yt;
            }
        }
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
        jPanel1 = new javax.swing.JPanel();
        position = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        isMoving = new javax.swing.JCheckBox();
        dynamicFloorMap = new javax.swing.JPanel(){
            @Override
            public void paintComponent(Graphics g)   {
                // Paint background
                super.paintComponent(g);
                paintMap(g);
            }
        };
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        usersInPosition = new javax.swing.JTextField();

        position.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Is Moving?");

        isMoving.setEnabled(false);

        javax.swing.GroupLayout dynamicFloorMapLayout = new javax.swing.GroupLayout(dynamicFloorMap);
        dynamicFloorMap.setLayout(dynamicFloorMapLayout);
        dynamicFloorMapLayout.setHorizontalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        dynamicFloorMapLayout.setVerticalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 218, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Position in map");

        jLabel5.setText("Users in position");

        usersInPosition.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dynamicFloorMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(position))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(isMoving)
                                .addGap(0, 356, Short.MAX_VALUE))
                            .addComponent(usersInPosition))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(position, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dynamicFloorMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(isMoving, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usersInPosition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Robot Status", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This method get drawing array from ClientController and draw maps
     * according to the data of it.
     * 
     * @param position 
     */
    public void drawAction(String position)
    {
        officeDrawingItems=controller.getMapDrawingArray();
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            String[] ts=officeDrawingItems.get(i);
            logger.finest(ts[0]+" "+ts[1]+" "+ts[2]+" "+ts[3]+" "+ts[4]);
        }
        //Redraw the panel
        robotPosition = position;
        dynamicFloorMap.repaint();
        //Untill now only the offices are drawn also robot should also be drawn
        
    }

    /**
     * this method returns the DeliveryStatus
     * @return 
     */
    public DeliveryStatusView getDeliveryStatusView() {
        return deliveryStatusView;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dynamicFloorMap;
    private javax.swing.JCheckBox isMoving;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField position;
    private javax.swing.JTextField usersInPosition;
    // End of variables declaration//GEN-END:variables

    /**
     * This method calculate the reduce amount in x coordinate 
     * 
     * @return reducer amount
     */
    private double calculateReducerX() {
        int maxX = 0;
        int minX = 0;
        int x=0;
        String[] ts;
        // Draw a circle around the mid-point
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            ts=officeDrawingItems.get(i);

            if(ts[0].equals("end"))
            {
            }
            else
            {
                if(ts[1].equals("x"))
                {
                    x = x + Integer.parseInt(ts[2]);
                    if(x > maxX) {
                        maxX = x;
                    }
                    if(x < minX) {
                        minX = x;
                    }
                }
            }
        }
        if(maxX-minX != 0) {
            return (dynamicFloorMap.getWidth()-20)/(maxX-minX);
        } else {
            return 10;
        }
    }

    /**
     * This method calculate the reduce amount in y coordinates
     * 
     * @return reducer amount
     */
    private double calculateReducerY() {
        int maxY = 0;
        int minY = 0;
        int y=0;
        String[] ts;
        // Draw a circle around the mid-point
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            ts=officeDrawingItems.get(i);

            if(ts[0].equals("end"))
            {
            }
            else
            {
                if(ts[1].equals("y"))
                {
                    y = y + Integer.parseInt(ts[2]);
                    if(y > maxY) {
                        maxY = y;
                    }
                    if(y < minY) {
                        minY = y;
                    }
                }
            }
        }
        if(maxY-minY != 0) {
            return (dynamicFloorMap.getHeight()-20)/(maxY-minY);
        } else {
            return 10;
        }
    }


    /**
     * This method calculates the starting point of X
     * 
     * @return starting point x
     */
    private int calculateStartX() {
        int maxX = 0;
        int minX = 0;
        int x=0;
        String[] ts;
        // Draw a circle around the mid-point
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            ts=officeDrawingItems.get(i);

            if(ts[0].equals("end"))
            {
            }
            else
            {
                if(ts[1].equals("x"))
                {
                    x = x + Integer.parseInt(ts[2]);
                    if(x > maxX) {
                        maxX = x;
                    }
                    if(x < minX) {
                        minX = x;
                    }
                }
            }
        }
//        System.err.println(-minX);
        return -minX;
    }

    /**
     * This method calculates the starting point in y coordinate
     * 
     * @return 
     */
    private int calculateStartY() {
        int maxY = 0;
        int minY = 0;
        int y=0;
        String[] ts;
        // Draw a circle around the mid-point
        for(int i=0;i<officeDrawingItems.size();i++)
        {
            ts=officeDrawingItems.get(i);

            if(ts[0].equals("end"))
            {
            }
            else
            {
                if(ts[1].equals("y"))
                {
                    y = y + Integer.parseInt(ts[2]);
                    if(y > maxY) {
                        maxY = y;
                    }
                    if(y < minY) {
                        minY = y;
                    }
                }
            }
        }
//        System.err.println(-minY);
        return -minY;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adsrobotstub;

import ads.logic.ServerControllerInterface;
import ads.logic.ServerNonInitializedException;
import ads.logic.SystemStatus;
import ads.presentation.ClientController;
import ads.presentation.SystemStatusView;
import ads.resources.communication.RobotStubInterface;
import java.awt.Color;
import java.awt.Graphics;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mgamell
 */
public class RobotStub extends javax.swing.JFrame {
    private ArrayList<String[]> officeDrawingItems;
    private String robotPosition;
    private ServerControllerInterface server;
    private RobotStubInterface robotStubServer;
    private RobotStub thisView;
    private static Logger logger = Logger.getLogger(RobotStub.class.getName());

    /**
     * Creates new form RobotStub
     */
    public RobotStub() {
        initServerConnection("spring.rutgers.edu");
        thisView = this;
        officeDrawingItems=new ArrayList();
        initComponents();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        logger.finest("updating");
                        SystemStatus status = getSystemStatus();
                        if(status != null && status.isServerInitialized()) {
                            position.setText(status.getRobotPosition());
                            isMoving.setSelected(status.isRobotIsMoving());
                            drawAction(status.getRobotPosition());
                        }
                        RobotStubStatus robotStatus = getRobotStubStatus();
                        buzzer.setSelected(robotStatus.isBuzzerRinging);
                        if(robotStatus.isRequestingPassword) {
                            String password = JOptionPane.showInputDialog(thisView,
                                "Insert password for user "+robotStatus.passwordUsername,
                                "Password",
                                JOptionPane.INFORMATION_MESSAGE);
                            robotStubServer.answer(password);
                        }
                        if(robotStatus.isPasswordCorrect) {
                            JOptionPane.showMessageDialog(thisView,
                                "The password is correct",
                                "Password correct",
                                JOptionPane.INFORMATION_MESSAGE);
                            robotStubServer.answer();
                        }
                        if(robotStatus.isPasswordWarning) {
                            JOptionPane.showMessageDialog(thisView,
                                "The password is incorrect... Try again!",
                                "Password incorrect",
                                JOptionPane.WARNING_MESSAGE);
                            robotStubServer.answer();
                        }
                        if(robotStatus.isPasswordError) {
                            JOptionPane.showMessageDialog(thisView,
                                "The password is incorrect... You can't try again!",
                                "Password incorrect",
                                JOptionPane.WARNING_MESSAGE);
                            robotStubServer.answer();
                        }
                        if(robotStatus.trayOpen != -1) {
                            JOptionPane.showMessageDialog(thisView,
                                "Tray "+robotStatus.trayOpen+" is open now! Click OK to close it.",
                                "Tray open",
                                JOptionPane.INFORMATION_MESSAGE);
                            robotStubServer.answer();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(thisView,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SystemStatusView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }).start();
    }

    private RobotStubStatus getRobotStubStatus() {
        try {
            return robotStubServer.getRobotStubStatus();
        } catch (RemoteException ex) {
            Logger.getLogger(RobotStub.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(thisView,
                "Unexpected error: "+ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
            return null;
        }
    }
    
    public SystemStatus getSystemStatus() {
        try {
            return server.getSystemStatus("admin", "admin");
        } catch (ServerNonInitializedException ex) {
            return null;
        } catch (RemoteException ex) {
            return null;
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

        jPanel1 = new javax.swing.JPanel();
        position = new javax.swing.JTextField();
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
        buzzer = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        position.setEnabled(false);

        isMoving.setText("is Moving?");
        isMoving.setEnabled(false);

        javax.swing.GroupLayout dynamicFloorMapLayout = new javax.swing.GroupLayout(dynamicFloorMap);
        dynamicFloorMap.setLayout(dynamicFloorMapLayout);
        dynamicFloorMapLayout.setHorizontalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        dynamicFloorMapLayout.setVerticalGroup(
            dynamicFloorMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 303, Short.MAX_VALUE)
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Position in map");

        buzzer.setText("Buzzer enabled");
        buzzer.setEnabled(false);

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
                        .addComponent(position, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(isMoving)
                            .addComponent(buzzer))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buzzer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(isMoving)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(RobotStub.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RobotStub.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RobotStub.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RobotStub.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RobotStub().setVisible(true);
            }
        });
    }
    
    
    private void initServerConnection(String host) {
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
            robotStubServer = (RobotStubInterface) registry.lookup("RobotStubInterface");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Unable to connect to remote server. Press OK to try to with local server",
                "Register error",
                JOptionPane.ERROR_MESSAGE);
            // try local repository
            try {
                Registry registry = LocateRegistry.getRegistry();
                server = (ServerControllerInterface) registry.lookup("ServerControllerInterface");
                robotStubServer = (RobotStubInterface) registry.lookup("RobotStubInterface");
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null,
                    "Unable to connect to local server. Exiting...",
                    "Register error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox buzzer;
    private javax.swing.JPanel dynamicFloorMap;
    private javax.swing.JCheckBox isMoving;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField position;
    // End of variables declaration//GEN-END:variables


    public ArrayList<String[]> getMapDrawingArray() {
        try {
            return server.getMapDrawingArray();
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void drawAction(String position)
    {
        officeDrawingItems=getMapDrawingArray();
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

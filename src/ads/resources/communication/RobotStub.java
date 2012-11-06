/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ads.resources.communication;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;

/**
 *
 * @author mgamell
 */
public class RobotStub extends javax.swing.JFrame {

    /**
     * Creates new form ServerStub
     */
    public RobotStub() {
        initComponents();
    }

    public void moveRobotToNextPoint() {
        logText("request: move robot to next point\n");
        logText("action: sleep 1;\n");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RobotStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        logText("answer: Done\n\n");
        
    }

    public String waitForPassword(int timeout) {
        logText("request: wait for password\n");
        
        String password = JOptionPane.showInputDialog(this,
            "Insert password",
            "Password",
            JOptionPane.INFORMATION_MESSAGE);
        logText("action: ask for password\n");
        logText("answer: "+password+"\n\n");
        
        return password;
    }

    public void ringBuzzer() {
        logText("request: ring buzzer\n");
        logText("answer\n\n");
        
    }

    public void showPasswordIncorrectError() {
        logText("request: show password incorrect error\n");
        logText("answer\n\n");
        
    }

    public void showPasswordIncorrectWarning() {
        logText("request: show password incorrect warning\n");
        logText("answer\n\n");
        
    }

    public void showPasswordCorrectMessage() {
        logText("request: show password correct\n");
        logText("answer\n\n");
        
    }

    public void openTray(int trayNum) {
        logText("request: open tray "+trayNum+"\n");
        logText("answer\n\n");
        
    }

    // From http://binfalse.de/2012/04/conditionally-autoscroll-a-jscrollpane/
    private void logText (String text)
    {
        final JScrollBar vbar = jScrollPane1.getVerticalScrollBar ();
        // is the scroll bar at the bottom?
        boolean end = vbar.getMaximum () == vbar.getValue () + vbar.getVisibleAmount ();

        // append some new text to the text area
        // (or do something else that increases the contents of the JScrollPane)
        this.outputLog.append (text);

        // if scroll bar already was at the bottom we schedule
        // a new scroll event to again scroll to the bottom
        // As you can see, here a new event is put in the EventQueue, and this event is told to put another event in the queue that will do the scroll event. Correct, that’s a bit strange, but the swing stuff is very lazy and it might take a while until the new maximum position of the scroll bar is calculated after the whole GUI stuff is re-validated. So let’s be sure that our event definitely happens when all dependent swing events are processed.
        if (end)
        {
            EventQueue.invokeLater (new Runnable ()
            {
                public void run ()
                {
                    EventQueue.invokeLater (new Runnable ()
                    {
                        public void run ()
                        {
                            vbar.setValue (vbar.getMaximum ());
                        }
                    });
                }
            });

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

        jScrollPane1 = new javax.swing.JScrollPane();
        outputLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        outputLog.setColumns(20);
        outputLog.setRows(5);
        jScrollPane1.setViewportView(outputLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputLog;
    // End of variables declaration//GEN-END:variables

}

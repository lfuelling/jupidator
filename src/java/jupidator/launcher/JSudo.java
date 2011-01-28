/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JSudo.java
 *
 * Created on Nov 30, 2009, 6:10:46 PM
 */
package jupidator.launcher;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import javax.swing.JDialog;

/**
 *
 * @author teras
 */
public class JSudo extends JDialog {

    private String pass = null;
    private final String[] command;

    public static void main(String[] args) {
        JSudo sudo = new JSudo(args);
        sudo.setVisible(true);
    }

    /** Creates new form JSudo */
    public JSudo(String[] command) {
        initComponents();
        this.command = command;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Viewport = new javax.swing.JPanel();
        LowerPanel = new javax.swing.JPanel();
        ButtonPanel = new javax.swing.JPanel();
        AllowB = new javax.swing.JButton();
        DenyB = new javax.swing.JButton();
        CentralPanel = new javax.swing.JPanel();
        InfoLabel = new javax.swing.JLabel();
        MsgLabel = new javax.swing.JLabel();
        PassPanel = new javax.swing.JPanel();
        LeftPassPanel = new javax.swing.JPanel();
        PLabel = new javax.swing.JLabel();
        RightPassPanel = new javax.swing.JPanel();
        Password = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CaféPorts authorization");
        setLocationByPlatform(true);
        setModal(true);
        setResizable(false);

        Viewport.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 24, 12, 24));
        Viewport.setLayout(new java.awt.BorderLayout());

        LowerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 0, 0, 0));
        LowerPanel.setLayout(new java.awt.BorderLayout());

        ButtonPanel.setLayout(new java.awt.GridLayout(1, 0));

        AllowB.setText("Allow");
        AllowB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AllowBActionPerformed(evt);
            }
        });
        ButtonPanel.add(AllowB);

        DenyB.setText("Deny");
        DenyB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DenyBActionPerformed(evt);
            }
        });
        ButtonPanel.add(DenyB);

        LowerPanel.add(ButtonPanel, java.awt.BorderLayout.EAST);

        Viewport.add(LowerPanel, java.awt.BorderLayout.PAGE_END);

        CentralPanel.setLayout(new java.awt.BorderLayout());

        InfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jupidator/launcher/lock.png"))); // NOI18N
        InfoLabel.setText("Application requires your password.");
        InfoLabel.setIconTextGap(20);
        CentralPanel.add(InfoLabel, java.awt.BorderLayout.NORTH);

        MsgLabel.setText(" ");
        CentralPanel.add(MsgLabel, java.awt.BorderLayout.PAGE_END);

        PassPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 4, 0));
        PassPanel.setLayout(new java.awt.BorderLayout(12, 0));

        LeftPassPanel.setLayout(new java.awt.GridLayout(1, 1, 0, 4));

        PLabel.setText("Password");
        LeftPassPanel.add(PLabel);

        PassPanel.add(LeftPassPanel, java.awt.BorderLayout.WEST);

        RightPassPanel.setLayout(new java.awt.GridLayout(1, 1, 0, 4));

        Password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PasswordActionPerformed(evt);
            }
        });
        RightPassPanel.add(Password);

        PassPanel.add(RightPassPanel, java.awt.BorderLayout.CENTER);

        CentralPanel.add(PassPanel, java.awt.BorderLayout.CENTER);

        Viewport.add(CentralPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(Viewport, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AllowBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AllowBActionPerformed
        setEnabled(false);
        MsgLabel.setForeground(Color.BLACK);
        MsgLabel.setText("Validating...");
        final String newp = new String(Password.getPassword()) + "\n";
        testSudo(newp, new Closure<String>() {

            public void exec(String data) {
                if (data == null) {
                    pass = newp;
                    MsgLabel.setForeground(Color.BLACK);
                    MsgLabel.setText(" ");
                    setVisible(false);
                    launchApp();
                } else {
                    Password.requestFocus();
                    Password.selectAll();
                    MsgLabel.setText("Wrong password!");
                    MsgLabel.setForeground(Color.RED);
                    setEnabled(true);
                }
            }
        });
}//GEN-LAST:event_AllowBActionPerformed

    private void DenyBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DenyBActionPerformed
        pass = null;
        MsgLabel.setForeground(Color.BLACK);
        MsgLabel.setText(" ");
        setVisible(false);
        System.exit(0);
}//GEN-LAST:event_DenyBActionPerformed

    private void PasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PasswordActionPerformed
        AllowBActionPerformed(evt);
    }//GEN-LAST:event_PasswordActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AllowB;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JPanel CentralPanel;
    private javax.swing.JButton DenyB;
    private javax.swing.JLabel InfoLabel;
    private javax.swing.JPanel LeftPassPanel;
    private javax.swing.JPanel LowerPanel;
    private javax.swing.JLabel MsgLabel;
    private javax.swing.JLabel PLabel;
    private javax.swing.JPanel PassPanel;
    private javax.swing.JPasswordField Password;
    private javax.swing.JPanel RightPassPanel;
    private javax.swing.JPanel Viewport;
    // End of variables declaration//GEN-END:variables

    private void launchApp() {
        String[] launcher = new String[command.length + 4];
        launcher[0] = "sudo";
        launcher[1] = "-S";
        launcher[2] = "-p";
        launcher[3] = "";
        System.arraycopy(command, 0, launcher, 4, command.length);
        thinLauncher(launcher, pass, null);
        System.exit(0);
    }

    @Override
    public void setEnabled(boolean status) {
        super.setEnabled(status);
        PLabel.setEnabled(status);
        Password.setEnabled(status);
        AllowB.setEnabled(status);
        DenyB.setEnabled(status);
    }

    public String getUserPass() {
        return pass;
    }

    @Override
    public void setVisible(boolean status) {
        super.setVisible(status);
        Password.requestFocus();
        Password.selectAll();
    }

    private void testSudo(String pass, final Closure<String> waiting) {
        final String SIGNATURE = "_ALL_OK_";
        pass += "\n";
        thinLauncher(new String[]{"sudo", "-k"}, null, null);   // Clear sudo
        thinLauncher(new String[]{"sudo", "-S", "echo", SIGNATURE}, pass, new Closure<String>() {

            private boolean messageSent = false;

            public void exec(String line) {
                System.out.println(line);
                if (messageSent)
                    return;
                if (line == null) {
                    messageSent = true;
                    waiting.exec("Password is not correct");
                    return;
                }
                if (line.equals(SIGNATURE)) {
                    messageSent = true;
                    waiting.exec(null);
                }
            }
        });
    }

    private static void thinLauncher(final String[] command, final String input, final Closure<String> output) {
        try {
            final Process proc = new ProcessBuilder(command).redirectErrorStream(true).start();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream(), "UTF-8"));   // We need to do it anyways to consume it
            if (input != null) {
                out.write(input);
                out.flush();
            }
            new Thread() {

                @Override
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));      // We need to do it anyways to consume it
                        String line;
                        while ((line = in.readLine()) != null)
                            if (output != null)
                                output.exec(line);
                        // Finalize output
                        if (output != null)
                            output.exec(null);
                    } catch (IOException ex) {
                    }
                }
            }.start();
        } catch (IOException ex) {
        }
    }
}

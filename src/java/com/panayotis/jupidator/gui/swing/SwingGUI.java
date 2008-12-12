/*
 * SwingGUI.java
 *
 * Created on September 25, 2008, 3:54 AM
 */
package com.panayotis.jupidator.gui.swing;

import com.panayotis.jupidator.gui.*;
import static com.panayotis.jupidator.i18n.I18N._;

import com.panayotis.jupidator.ApplicationInfo;
import com.panayotis.jupidator.Updater;
import com.panayotis.jupidator.UpdaterException;
import com.panayotis.jupidator.list.UpdaterAppElements;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Formatter;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author  teras
 */
public class SwingGUI extends JDialog implements JupidatorGUI {

    private Updater callback;

    /** Creates new form SwingGUI */
    public SwingGUI(Updater callback) {
        super((Frame) null, false);
        initComponents();
        this.callback = callback;
        LaterB.requestFocus();
    }

    public void setInformation(UpdaterAppElements el, ApplicationInfo info) throws UpdaterException {
        NewVerL.setText(_("A new version of {0} is available!", el.getAppName()));
        VersInfoL.setText(_("{0} version {1} is now available - you have {2}.", el.getAppName(), el.getNewVersion(), info.getVersion()));
        setTitle(_("New version of {0} found!", el.getAppName()));

        InfoPane.setContentType("text/html");
        InfoPane.setText(el.getHTML());

        try {
            URL icon = new URL(el.getIconpath());
            if (icon != null)
                IconL.setIcon(new ImageIcon(icon));
        } catch (MalformedURLException ex) {
            throw new UpdaterException("Unable to load  icon " + ex.getMessage());
        }
    }

    public void startDialog() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void endDialog() {
        setVisible(false);
        dispose();
    }

    public void setIndetermined() {
        ActionB.setEnabled(false);
        PBar.setIndeterminate(true);
        InfoL.setText(_("Deploying files..."));
    }

    public void errorOnCommit(String message) {
        setInfoArea(message);
        InfoL.setForeground(Color.RED);
        ProgressP.revalidate();
    }

    public void successOnCommit() {
        setInfoArea(_("Successfully downloaded updates"));
        ActionB.setText(_("Restart application"));
        ActionB.setActionCommand("restart");
        ProgressP.revalidate();
    }

    private void setInfoArea(String message) {
        ActionB.setEnabled(true);
        BarPanel.remove(PBar);
        ProgressP.remove(InfoL);
        BarPanel.add(InfoL);
        InfoL.setText(message);
    }

    public void setDownloadRatio(long bytes, float percent) {
        PBar.setValue(Math.round(percent * 100));

        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        if (bytes < 1e3) {
            formatter.format("%db/sec", bytes);
        } else if (bytes < 1e6) {
            formatter.format("%2.1fKb/sec", bytes / 1e3);
        } else if (bytes < 1e9) {
            formatter.format("%2.1fMb/sec", bytes / 1e6);
        } else if (bytes < 1e12) {
            formatter.format("%2.1fGb/sec", bytes / 1e9);
        }
        String ratio = sb.toString().trim();
        PBar.setToolTipText("Download speed: " + ratio);
        PBar.setString(ratio);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ProgressP = new javax.swing.JPanel();
        BarPanel = new javax.swing.JPanel();
        PBar = new javax.swing.JProgressBar();
        ButtonPanel = new javax.swing.JPanel();
        ActionB = new javax.swing.JButton();
        InfoL = new javax.swing.JLabel();
        MainPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        InfoPane = new javax.swing.JEditorPane();
        jPanel5 = new javax.swing.JPanel();
        VersInfoL = new javax.swing.JLabel();
        NotesL = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        InfoB = new javax.swing.JButton();
        NewVerL = new javax.swing.JLabel();
        IconL = new javax.swing.JLabel();
        CommandP = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        SkipB = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        LaterB = new javax.swing.JButton();
        UpdateB = new javax.swing.JButton();

        ProgressP.setLayout(new java.awt.BorderLayout());

        BarPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 0));
        BarPanel.setLayout(new java.awt.BorderLayout());

        PBar.setStringPainted(true);
        BarPanel.add(PBar, java.awt.BorderLayout.CENTER);

        ProgressP.add(BarPanel, java.awt.BorderLayout.CENTER);

        ButtonPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 24, 8, 8));
        ButtonPanel.setLayout(new java.awt.BorderLayout());

        ActionB.setText(_("Cancel"));
        ActionB.setActionCommand("cancel");
        ActionB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActionBActionPerformed(evt);
            }
        });
        ButtonPanel.add(ActionB, java.awt.BorderLayout.CENTER);

        ProgressP.add(ButtonPanel, java.awt.BorderLayout.EAST);

        InfoL.setText(_("Downloading..."));
        ProgressP.add(InfoL, java.awt.BorderLayout.LINE_START);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        MainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        MainPanel.setMinimumSize(new java.awt.Dimension(550, 400));
        MainPanel.setPreferredSize(new java.awt.Dimension(550, 400));
        MainPanel.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.BorderLayout());

        InfoPane.setEditable(false);
        jScrollPane1.setViewportView(InfoPane);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel5.setLayout(new java.awt.BorderLayout());
        jPanel5.add(VersInfoL, java.awt.BorderLayout.CENTER);

        NotesL.setFont(NotesL.getFont().deriveFont(NotesL.getFont().getStyle() | java.awt.Font.BOLD));
        NotesL.setText(_("Release Notes"));
        NotesL.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 4, 0));
        jPanel5.add(NotesL, java.awt.BorderLayout.SOUTH);

        jPanel1.setLayout(new java.awt.BorderLayout());

        InfoB.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/i.png"))); // NOI18N
        InfoB.setBorderPainted(false);
        InfoB.setMaximumSize(new java.awt.Dimension(16, 16));
        InfoB.setMinimumSize(new java.awt.Dimension(16, 16));
        InfoB.setPreferredSize(new java.awt.Dimension(16, 16));
        InfoB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InfoBActionPerformed(evt);
            }
        });
        jPanel1.add(InfoB, java.awt.BorderLayout.EAST);

        NewVerL.setFont(NewVerL.getFont().deriveFont(NewVerL.getFont().getStyle() | java.awt.Font.BOLD, NewVerL.getFont().getSize()+1));
        NewVerL.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 4, 0));
        jPanel1.add(NewVerL, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel6.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        MainPanel.add(jPanel6, java.awt.BorderLayout.CENTER);

        IconL.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        IconL.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 0, 0, 0));
        MainPanel.add(IconL, java.awt.BorderLayout.LINE_START);

        CommandP.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 8));
        jPanel3.setLayout(new java.awt.BorderLayout());

        SkipB.setText(_("Skip this version"));
        SkipB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkipBActionPerformed(evt);
            }
        });
        jPanel3.add(SkipB, java.awt.BorderLayout.CENTER);

        CommandP.add(jPanel3, java.awt.BorderLayout.WEST);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 12));
        jPanel4.setLayout(new java.awt.GridLayout(1, 2, 4, 0));

        LaterB.setText(_("Remind me later"));
        LaterB.setSelected(true);
        LaterB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LaterBActionPerformed(evt);
            }
        });
        jPanel4.add(LaterB);

        UpdateB.setText(_("Install Update"));
        UpdateB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UpdateBActionPerformed(evt);
            }
        });
        jPanel4.add(UpdateB);

        CommandP.add(jPanel4, java.awt.BorderLayout.EAST);

        MainPanel.add(CommandP, java.awt.BorderLayout.SOUTH);

        getContentPane().add(MainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void UpdateBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateBActionPerformed
    CommandP.setVisible(false);
    ProgressP.setVisible(true);
    MainPanel.add(ProgressP, BorderLayout.SOUTH);
    callback.actionCommit();
}//GEN-LAST:event_UpdateBActionPerformed

private void LaterBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LaterBActionPerformed
    callback.actionDefer();
}//GEN-LAST:event_LaterBActionPerformed

private void SkipBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkipBActionPerformed
    callback.actionIgnore();
}//GEN-LAST:event_SkipBActionPerformed

private void ActionBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActionBActionPerformed
    ActionB.setEnabled(false);
    if (ActionB.getActionCommand().startsWith("c"))
        callback.actionCancel();
    else
        callback.actionRestart();
}//GEN-LAST:event_ActionBActionPerformed

private void InfoBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InfoBActionPerformed
    String msg = "Jupidator is open source, under the LGPL\nMore info: http://www.jupidator.org";
    JOptionPane.showMessageDialog(this, msg, _("About Jupidator"), JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_InfoBActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActionB;
    private javax.swing.JPanel BarPanel;
    private javax.swing.JPanel ButtonPanel;
    private javax.swing.JPanel CommandP;
    private javax.swing.JLabel IconL;
    private javax.swing.JButton InfoB;
    private javax.swing.JLabel InfoL;
    private javax.swing.JEditorPane InfoPane;
    private javax.swing.JButton LaterB;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JLabel NewVerL;
    private javax.swing.JLabel NotesL;
    private javax.swing.JProgressBar PBar;
    private javax.swing.JPanel ProgressP;
    private javax.swing.JButton SkipB;
    private javax.swing.JButton UpdateB;
    private javax.swing.JLabel VersInfoL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
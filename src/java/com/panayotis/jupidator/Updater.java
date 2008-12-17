/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panayotis.jupidator;

import static com.panayotis.jupidator.i18n.I18N._;

import com.panayotis.jupidator.elements.FileUtils;
import com.panayotis.jupidator.gui.JupidatorGUI;
import com.panayotis.jupidator.gui.UpdateWatcher;
import com.panayotis.jupidator.data.Arch;
import com.panayotis.jupidator.data.SimpleApplication;
import com.panayotis.jupidator.gui.console.ConsoleGUI;
import com.panayotis.jupidator.gui.swing.SwingGUI;
import com.panayotis.jupidator.loglist.creators.HTMLCreator;
import com.panayotis.jupidator.data.Version;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author teras
 */
public class Updater {

    private Version vers;
    private JupidatorGUI gui;
    private UpdateWatcher watcher;
    private UpdatedApplication application;
    private ApplicationInfo appinfo;
    private Thread download;

    public Updater(String xmlurl, ApplicationInfo appinfo, UpdatedApplication application) throws UpdaterException {
        this.appinfo = appinfo;
        vers = Version.loadVersion(xmlurl, appinfo);
        if (application == null)
            application = new SimpleApplication();
        this.application = application;
        watcher = new UpdateWatcher();
    }

    /** Return JupidatorGUI, and create it if it does not exist.
     *  This is the official method to create the default GUI
     *  GUI is created lazily, when needed
     */
    public JupidatorGUI getGUI() {
        if (gui == null) {
            if (GraphicsEnvironment.isHeadless())
                gui = new ConsoleGUI();
            else
                gui = new SwingGUI();
        }
        return gui;
    }

    public void setGUI(JupidatorGUI gui) {
        if (gui != null)
            this.gui = gui;
    }

    public void actionDisplay() throws UpdaterException {
        if (vers.size() > 0) {
            getGUI();  /* GUI is created lazily, when needed */
            watcher.setCallBack(gui);
            gui.setInformation(this, vers.getAppElements(), appinfo);
            gui.startDialog();
        }
    }

    public void actionCommit() {
        long size = 0;
        for (String key : vers.keySet()) {
            size += vers.get(key).getSize();
        }
        watcher.setAllBytes(size);
        download = new Thread() {

            public void run() {
                /* Download */
                for (String key : vers.keySet()) {
                    String result = vers.get(key).fetch(application, watcher);
                    if (result != null) {
                        watcher.stopWatcher();
                        gui.errorOnCommit(result);
                        return;
                    }
                }
                /* Deploy */
                gui.setIndetermined();
                for (String key : vers.keySet()) {
                    String result = vers.get(key).deploy(application);
                    if (result != null) {
                        watcher.stopWatcher();
                        gui.errorOnCommit(result);
                        return;
                    }
                }
                watcher.stopWatcher();
                gui.successOnCommit();
            }
        };
        download.start();
        watcher.startWatcher();
    }

    public void actionCancel() {
        watcher.stopWatcher();
        download.interrupt();
        gui.endDialog();
        try {
            download.join();
        } catch (InterruptedException ex) {
        }
        for (String key : vers.keySet()) {
            vers.get(key).cancel(application);
        }
    }

    /* Do nothing - wait for next cycle */
    public void actionDefer() {
        watcher.stopWatcher();
        gui.endDialog();
        vers.getUpdaterProperties().defer();
    }

    public void actionIgnore() {
        watcher.stopWatcher();
        gui.endDialog();
        vers.getUpdaterProperties().ignore(vers.getAppElements().getNewRelease());
    }

    public void actionRestart() {
        watcher.stopWatcher();
        gui.endDialog();
        if (application.requestRestart()) {
            String classname = "com.panayotis.jupidator.deployer.JupidatorDeployer";
            String temppath = System.getProperty("java.io.tmpdir");
            Arch arch = vers.getArch();

            String message = FileUtils.copyClass(classname, temppath, application);
            if (message != null) {
                application.receiveMessage(message);
                JOptionPane.showMessageDialog(null, message, message, JOptionPane.ERROR_MESSAGE);
                return;
            }

            int header = 6;
            String args[] = new String[header + vers.size() + arch.countArguments()];
            args[0] = FileUtils.JAVABIN;
            args[1] = "-cp";
            args[2] = temppath;
            args[3] = classname;
            args[4] = vers.isGraphicalDeployer() ? "g" : "t";
            args[5] = String.valueOf(vers.size());

            for (String key : vers.keySet()) {
                args[header++] = vers.get(key).getArgument();
            }

            for (int i = 0; i < arch.countArguments(); i++) {
                args[header++] = arch.getArgument(i, appinfo);
            }

            try {
                StringBuffer buf = new StringBuffer();
                for (int i = 0; i < args.length; i++)
                    buf.append(args[i]).append(' ');
                application.receiveMessage(_("Executing {0}", buf.toString()));
                Runtime.getRuntime().exec(args);
            } catch (IOException ex) {
                application.receiveMessage(ex.getMessage());
            }
            System.exit(0);
        }
    }

    public String getChangeLog() {
        return HTMLCreator.getList(vers.getAppElements().getLogList());
    }
}

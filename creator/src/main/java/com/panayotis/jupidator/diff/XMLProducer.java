/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.panayotis.jupidator.diff;

import com.panayotis.jupidator.xml.XMLWalker;
import java.io.File;
import java.util.Collection;

/**
 *
 * @author teras
 */
public class XMLProducer {

    public static void produce(File xmlfile, String arch, String version, Collection<DiffCommand> commands) {
        XMLWalker w = XMLWalker.load(xmlfile);
        if (w == null)
            w = new XMLWalker();
        w.execIf(q -> !q.nodeExists("updatelist"), q -> q.add("updatelist"));
        w.node("updatelist");

        w.filterNodes("version", q -> q.tag("version"), q -> version.equals(q.attribute("version")));
        w.execIf(q -> !q.hasTag("version"), q -> q.add("version").tag("version").setAttribute("version", version));
        w.toTag("version");
        w.execIf(q -> !q.nodeExists("description"), q -> q.add("description").setText("Description of version " + version));
        w.filterNodes("arch", q -> q.remove(), q -> arch.equals(q.attribute("name")));
        w.add("arch").setAttribute("name", arch);
        for (DiffCommand cmd : commands)
            cmd.add(w);
        w.store(xmlfile, true);
    }
}

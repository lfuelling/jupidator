/*
 *
 * This file is part of Jupidator.
 *
 * Jupidator is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 *
 * Jupidator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jupidator; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package com.panayotis.jupidator;

import com.panayotis.arjs.StringArg;
import com.panayotis.arjs.Args;
import com.panayotis.arjs.BoolArg;
import com.panayotis.jupidator.diff.DiffCommand;
import com.panayotis.jupidator.diff.DiffCreator;
import com.panayotis.jupidator.diff.XMLProducer;
import com.panayotis.jupidator.diff.XMLSqueezer;
import com.panayotis.jupidator.parsables.ParseFolder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author teras
 */
public class Creator {

    private static final File NULL = new File("");

    /**
     * @param arguments the command line arguments
     */
    public static void main(String... arguments) {
        BoolArg parse = new BoolArg();
        BoolArg create = new BoolArg();
        BoolArg squeeze = new BoolArg();
        StringArg output = new StringArg("");
        StringArg arch = new StringArg(System.getProperty("os.arch"));
        StringArg prev = new StringArg("");
        StringArg packfile = new StringArg("files");
        StringArg version = new StringArg("1.0-SNAPSHOT");
        StringArg jupfile = new StringArg("jupidator.xml");
        BoolArg nomd5 = new BoolArg();
        BoolArg nosha1 = new BoolArg();
        BoolArg nosha256 = new BoolArg();
        BoolArg skipfiles = new BoolArg();
        Args args = new Args();
        args
                .def("parse", parse)
                .def("create", create)
                .def("squeeze", squeeze)
                .def("-o", output)
                .def("-a", arch)
                .def("-p", prev)
                .def("-f", packfile)
                .def("-v", version)
                .def("-j", jupfile)
                .def("--no-md5", nomd5)
                .def("--no-sha1", nosha1)
                .def("--no-sha256", nosha256)
                .def("--skip-files", skipfiles)
                .defhelp("-h", "--help")
                .alias("-o", "--output")
                .alias("-a", "--arch")
                .alias("-p", "--prev")
                .alias("-f", "--files")
                .alias("-v", "--version")
                .alias("-j", "--jupfile")
                .dep("-p", "create")
                .dep("-f", "create", "squeeze")
                .dep("--skip-files", "create")
                .dep("-v", "create", "squeeze")
                .dep("-j", "create", "squeeze")
                .dep("--no-md5", "create")
                .dep("--no-sha1", "create")
                .dep("--no-sha256", "create")
                .dep("-o", "parse", "create")
                .dep("-a", "create", "parse")
                .req("parse", "create", "squeeze")
                .req("-p")
                .uniq("parse", "create", "squeeze")
                .usage("jupidator_creator", "parse", "-o", "-a", "INSTALL_DIR")
                .usage("jupidator_creator", "create", "-p", "-j", "-f", "-o", "-a", "INSTALL_DIR")
                .usage("jupidator_creator", "squeeze", "-j", "-v", "-f")
                .group("Parse existing installation", "parse")
                .group("Create jupidator files", "create", "-p", "-f", "--skip-files", "-j", "-v", "--no-md5", "--no-sha1", "--no-sha256")
                .group("Squeeze Jupidator file", "squeeze", "-j", "-v")
                .info("parse", "parse INSTALL_DIR and create fingerprints of the directory structure and files.")
                .info("-o", "output resulting hashing information to a file for future use or comparison, instead of standard output.", "FILE")
                .info("-a", "the name of the architecture. If missing the default architecture is used.")
                .info("create", "create an installation bundle, based on a previous installation (given by --prev) and the current installation (given by INSTALL_DIR).")
                .info("-p", "the file with the hashing information of the previous installation.")
                .info("-f", "where the compressed package files will be stored; defaults to \"files\".")
                .info("--skip-files", "skip creation of files, if specific files already exist.")
                .info("-v", "the version of the produced application. Will be used to organize downloaded files.")
                .info("-j", "use this jupidator update file to append the update information. Defaults to jupidator.xml.")
                .info("--no-md5", "disable the usade of md5 hashing algorithm.")
                .info("--no-sha1", "disable the usade of sha1 hashing algorithm.")
                .info("--no-sha256", "disable the usade of sha256 hashing algorithm.")
                .info("squeeze", "squeeze architects of jupidator update file and support the 'all' argument. Note that this is an irreversible procedure.")
                .setCondensed('-')
                .setJoined('=')
                .error(err -> {
                    System.err.println("Error while executing Jupidator Creator: " + err);
                    System.err.println();
                    System.err.print(args.getUsage());
                    System.exit(-1);
                });
        List<String> freeArgs = args.parse(arguments);
        if (squeeze.get()) {
            if (!freeArgs.isEmpty())
                throw new JupidatorCreatorException("No free parameters are expected, " + freeArgs.size() + " found");
            squeeze(new File(jupfile.get()), new File(packfile.get()), version.get());
        } else {
            if (freeArgs.size() != 1)
                throw new JupidatorCreatorException("Exactly one free parameter is required, " + freeArgs.size() + " found");
            File in = new File(freeArgs.get(0));
            File out = output.get().isEmpty() ? null : new File(output.get());

            if (parse.get())
                parse(in, out, arch.get());
            else if (create.get())
                create(new File(prev.get()), in, out, new File(packfile.get()), arch.get(), version.get(), new File(jupfile.get()), nomd5.get(), nosha1.get(), nosha256.get(), skipfiles.get());
        }
    }

    private static ParseFolder parse(File input, File output, String arch) {
        ParseFolder result = new ParseFolder(input);
        JSONObject obj;
        if (output != null && output.isFile())
            try {
                obj = new JSONObject(new String(Files.readAllBytes(output.toPath()), "UTF-8"));
            } catch (IOException ex) {
                throw new JupidatorCreatorException("Unable to parse output file " + output);
            }
        else
            obj = new JSONObject();

        obj.put(arch, result.toJSON());
        if (output == NULL) {
        } else if (output != null)
            try {
                Files.write(output.toPath(), obj.toString().getBytes());
            } catch (IOException ex) {
                throw new JupidatorCreatorException("Unable to to write to output file " + output);
            }
        else
            System.out.println(obj);
        return result;
    }

    private static void create(File previous, File input, File output, File packages, String arch, String version, File jupfile, boolean nomd5, boolean nosha1, boolean nosha256, boolean skipfiles) {
        ParseFolder older;
        try {
            JSONObject obj = new JSONObject(new String(Files.readAllBytes(previous.toPath()), "UTF-8"));
            obj = obj.optJSONObject(arch);
            if ((obj == null))
                throw new JupidatorCreatorException("Unable to find architecture " + arch + " in previous configurations at " + previous.getPath());
            older = new ParseFolder(obj);
        } catch (IOException ex) {
            throw new JupidatorCreatorException("Unable to read previous installation file '" + previous + "'");
        }
        ParseFolder current = parse(input, output == null ? NULL : output, arch);
        Collection<DiffCommand> diffs = DiffCreator.create(older, current, input, packages, version, arch, nomd5, nosha1, nosha256, skipfiles);
        XMLProducer.produce(jupfile, arch, version, diffs);
    }

    private static void squeeze(File jupidator, File files, String version) {
        XMLSqueezer.squeeze(jupidator, files, version);
    }
}

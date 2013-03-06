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

package com.panayotis.jupidator.constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CFile extends CPath {

    private final long size;
    private final String md5;
    private final String sha256;

    public CFile(File file) throws IOException {
        super(file);
        size = file.length();
        md5 = getDigest(file, "MD5");
        sha256 = getDigest(file, "SHA-256");
    }

    @Override
    protected void dump(Writer out, int depth) throws IOException {
        dumpTabs(out, depth);
        out.append("<file name=\"").append(getName())
                .append("\" size=\"").append(Long.toString(size))
                .append("\" md5=\"").append(md5)
                .append("\" sha256=\"").append(sha256)
                .append("\"/>\n");
    }

    private static String getDigest(File in, String algorithm) throws IOException {
        MessageDigest digest;
        FileInputStream fis = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
            byte[] buffer = new byte[1024];
            digest.reset();
            int read;
            fis = new FileInputStream(in);
            while ((read = fis.read(buffer)) >= 0)
                digest.update(buffer, 0, read);

            byte[] outb = digest.digest();
            StringBuilder sbout = new StringBuilder();
            for (byte b : outb)
                sbout.append(String.format("%02x", b));
            return sbout.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException("Algorithm error while digesting");
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException ex) {
                }
        }
    }
}

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2013, 2014, 2016 Synacor, Inc.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation,
 * version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.common.mime;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataSource;

import com.zimbra.common.mime.MimePart.PartSource;

public class MimeParserInputStream extends FilterInputStream {
    private MimeParser parser;
    private MimePart.PartSource psource;
    private MimeHeaderBlock headers;

    public MimeParserInputStream(InputStream in) {
        super(in);
        parser = new MimeParser();
    }

    public MimeParserInputStream(InputStream in, MimeHeaderBlock hblock) {
        super(in);
        parser = new MimeParser(hblock);
        headers = hblock;
    }

    @Override public int read() throws IOException {
        int b = super.read();
        if (b != -1) {
            parser.handleByte((byte) b);
        }
        return b;
    }

    @Override public int read(byte[] b, int off, int len) throws IOException {
        int amt = super.read(b, off, len);
        if (amt != -1) {
            parser.handleBytes(b, off, amt);
        }
        return amt;
    }

    @Override public long skip(long n) throws IOException {
        long remaining = n;
        int max = (int) Math.min(n, 32768), read = 0;
        final byte buffer[] = new byte[max];

        while (remaining > 0 && (read = read(buffer, 0, (int) Math.min(remaining, max))) != -1) {
            remaining -= read;
        }
        return n - remaining;
    }

    @Override public void close() throws IOException {
        super.close();
        parser.endParse();
    }


    public MimeParserInputStream setSource(byte[] content) {
        psource = content == null ? null : new PartSource(content);
        return this;
    }

    public MimeParserInputStream setSource(File file) {
        psource = file == null || !file.exists() ? null : new PartSource(file);
        return this;
    }

    public MimeParserInputStream setSource(DataSource ds) {
        psource = ds == null ? null : new PartSource(ds);
        return this;
    }

    public MimeParserInputStream setSource(MimePart.InputStreamSource iss) {
        psource = iss == null ? null : new PartSource(iss);
        return this;
    }

    public MimePart getPart() {
        MimePart mp = parser.getPart().attachSource(psource);
        if (mp instanceof MimeBodyPart && headers != null && !headers.containsHeader("Content-Transfer-Encoding")) {
            MimeBodyPart mbp = (MimeBodyPart) mp;
            try {
                mbp.setTransferEncoding(mbp.pickEncoding());
            } catch (IOException ioe) {
                mbp.setTransferEncoding(ContentTransferEncoding.BASE64);
            }
        }
        return mp;
    }

    <T extends MimeMessage> T insertBodyPart(T mm) {
        mm.setBodyPart(getPart());
        mm.recordEndpoint(parser.getPosition(), parser.getLineNumber());
        mm.attachSource(psource);
        return mm;
    }

    public MimeMessage getMessage(Properties props) {
        MimeMessage mm = new MimeMessage(getPart(), props);
        mm.recordEndpoint(parser.getPosition(), parser.getLineNumber());
        mm.attachSource(psource);
        return mm;
    }
}

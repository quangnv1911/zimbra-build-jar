/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculates the size and digest of the wrapped <tt>InputStream</tt>.
 * Does not support mark/reset, since we need to read the data sequentially
 * when calculating the digest.
 */
public class CalculatorStream extends InputStream {

    private long mSize = 0;
    private InputStream mIn;
    private MessageDigest mDigestCalculator;
    
    public CalculatorStream(InputStream in) {
        try {
            mDigestCalculator = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to initialize " + CalculatorStream.class.getSimpleName(), e);
        }
        
        mIn  = new DigestInputStream(in, mDigestCalculator);
    }

    /**
     * Returns the SHA-256 digest of the bytes read, encoded as base64.
     */
    public String getDigest() {
        return ByteUtil.encodeFSSafeBase64(mDigestCalculator.digest());
    }
    
    /**
     * Returns the number of bytes read.
     */
    public long getSize() {
        return mSize;
    }
    
    @Override
    public int read() throws IOException {
        int result = mIn.read();
        if (result >= 0) {
            mSize++;
        }
        return result;
    }

    @Override
    public boolean markSupported() {
        // Not supported because we need to read sequentially to calculate the digest.
        return false;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int numRead = mIn.read(b, off, len);
        if (numRead > 0) {
            mSize += numRead;
        }
        return numRead;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int numRead = mIn.read(b);
        if (numRead > 0) {
            mSize += numRead;
        }
        return numRead;
    }

    @Override
    public int available() throws IOException {
        return mIn.available();
    }

    @Override
    public void close() throws IOException {
        mIn.close();
    }

    @Override
    public long skip(long n) throws IOException {
        return mIn.skip(n);
    }
}

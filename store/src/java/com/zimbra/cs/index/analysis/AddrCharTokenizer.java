/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;

import com.zimbra.cs.index.LuceneIndex;

/**
 * Tokenizer for email addresses.
 *
 * @author tim
 * @author ysasaki
 */
public final class AddrCharTokenizer extends CharTokenizer {

    public AddrCharTokenizer(Reader reader) {
        super(LuceneIndex.VERSION, reader);
    }

    @Override
    protected boolean isTokenChar(int ch) {
        if (Character.isWhitespace(ch)) {
            return false;
        }
        switch (ch) {
            case '\u3000': // fullwidth space
            case '<':
            case '>':
            case '\"':
            case ',':
            case '\'':
            case '(':
            case ')':
            case '[':
            case ']':
                return false;
        }
        return true;
    }

    @Override
    protected int normalize(int c) {
        return (char) NormalizeTokenFilter.normalize(c);
    }

}

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2008, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.mailbox.calendar;

import java.util.Comparator;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class ListJavaTimeZones {

    public static void main(String[] args) throws Exception {
        String[] tzids = TimeZone.getAvailableIDs();
        Set<TimeZone> tzset = new TreeSet<TimeZone>(new TZComparatorByOffset());  // TreeSet for sorting.
        for (String tzid : tzids) {
            tzset.add(TimeZone.getTimeZone(tzid));
        }
        for (TimeZone tz : tzset) {
            printTZ(tz);
        }
    }

    private static class TZComparatorByOffset implements Comparator<TimeZone> {

        public int compare(TimeZone tz1, TimeZone tz2) {
            if (tz1 != null && tz2 != null) {
                // Sort on raw offset.  If equal, sort of TZID string.
                int off1 = tz1.getRawOffset();
                int off2 = tz2.getRawOffset();
                if (off1 != off2) {
                    return off1 - off2;
                } else {
                    return tz1.getID().compareTo(tz2.getID());
                }
            } else if (tz1 != null) {
                // tz2 == null
                return -1;
            } else if (tz2 != null) {
                // tz1 == null
                return 1;
            } else {
                // both null
                return 0;
            }
        }
    }

    private static String toGMTOffsetString(int offsetMillis) {
        int minutes = offsetMillis / 1000 / 60;
        String sign;
        if (minutes < 0) {
            minutes *= -1;
            sign = "-";
        } else {
            sign = "+";
        }
        int hours = minutes / 60;
        minutes %= 60;
        return String.format("GMT%s%02d:%02d", sign, hours, minutes);
    }

    private static void printTZ(TimeZone tz) {
        String tzid = tz.getID();
        String gmtOffset = toGMTOffsetString(tz.getRawOffset());
        boolean isDst = tz.useDaylightTime();
        String clazz = tz.getClass().getSimpleName();
        String text = String.format("%-20s (%s) - hasDST=%s (%s:%08x)", tzid, gmtOffset, (isDst ? "Y" : "N"), clazz, tz.hashCode());
        System.out.println(text);
    }
}

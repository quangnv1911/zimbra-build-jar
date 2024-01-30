/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.store.file;

import java.io.IOException;

import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.store.StagedBlob;

public class VolumeStagedBlob extends StagedBlob {
    private VolumeBlob mLocalBlob;
    private boolean mWasStagedDirectly;

    VolumeStagedBlob(Mailbox mbox, VolumeBlob blob) throws IOException {
        super(mbox, blob.getDigest(), blob.getRawSize());
        mLocalBlob = blob;
    }

    public VolumeBlob getLocalBlob() {
        return mLocalBlob;
    }

    @Override public String getLocator() {
        return Short.toString(mLocalBlob.getVolumeId());
    }

    VolumeStagedBlob markStagedDirectly() {
        mWasStagedDirectly = true;
        return this;
    }

    boolean wasStagedDirectly() {
        return mWasStagedDirectly;
    }
}

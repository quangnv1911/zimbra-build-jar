/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2004, 2005, 2006, 2007, 2009, 2010, 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.redolog.op;

import java.io.IOException;
import java.util.Arrays;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.mailbox.MailItem;
import com.zimbra.cs.mailbox.MailboxManager;
import com.zimbra.cs.mailbox.MailboxOperation;
import com.zimbra.cs.mailbox.MailItem.TargetConstraint;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.redolog.RedoLogInput;
import com.zimbra.cs.redolog.RedoLogOutput;

/**
 * @since 2004. 7. 21.
 */
public class MoveItem extends RedoableOp {

    private int[] mIds;
    private MailItem.Type type;
    private int mDestId;
    private String mConstraint;
    private int mUIDNEXT = Mailbox.ID_AUTO_INCREMENT;

    public MoveItem() {
        super(MailboxOperation.MoveItem);
        type = MailItem.Type.UNKNOWN;
        mDestId = 0;
        mConstraint = null;
    }

    public MoveItem(int mailboxId, int[] ids, MailItem.Type type, int destId, TargetConstraint tcon) {
        this();
        setMailboxId(mailboxId);
        mIds = ids;
        this.type = type;
        mDestId = destId;
        mConstraint = (tcon == null ? null : tcon.toString());
    }

    public void setUIDNEXT(int uidnext) {
        mUIDNEXT = (uidnext > 0 ? uidnext : Mailbox.ID_AUTO_INCREMENT);
    }

    public int getUIDNEXT() {
        return mUIDNEXT;
    }

    @Override
    protected String getPrintableData() {
        StringBuilder sb = new StringBuilder("id=");
        sb.append(Arrays.toString(mIds)).append(", type=").append(type);
        sb.append(", dest=").append(mDestId);
        if (mConstraint != null) {
            sb.append(", constraint=").append(mConstraint);
        }
        if (mUIDNEXT != Mailbox.ID_AUTO_INCREMENT) {
            sb.append(", uidnext=").append(mUIDNEXT);
        }
        return sb.toString();
    }

    @Override
    protected void serializeData(RedoLogOutput out) throws IOException {
        out.writeInt(-1);
        out.writeByte(type.toByte());
        out.writeInt(mDestId);
        boolean hasConstraint = mConstraint != null;
        out.writeBoolean(hasConstraint);
        if (hasConstraint) {
            out.writeUTF(mConstraint);
        }
        out.writeInt(mIds == null ? 0 : mIds.length);
        if (mIds != null) {
            for (int i = 0; i < mIds.length; i++) {
                out.writeInt(mIds[i]);
            }
        }
        if (getVersion().atLeast(1, 16)) {
            out.writeInt(mUIDNEXT);
        }
    }

    @Override
    protected void deserializeData(RedoLogInput in) throws IOException {
        int id = in.readInt();
        if (id > 0) {
            mIds = new int[] { id };
        }
        type = MailItem.Type.of(in.readByte());
        mDestId = in.readInt();
        if (in.readBoolean()) {
            mConstraint = in.readUTF();
        }
        if (id <= 0) {
            mIds = new int[in.readInt()];
            for (int i = 0; i < mIds.length; i++) {
                mIds[i] = in.readInt();
            }
        }
        if (getVersion().atLeast(1, 16)) {
            mUIDNEXT = in.readInt();
        }
    }

    @Override
    public void redo() throws Exception {
        Mailbox mbox = MailboxManager.getInstance().getMailboxById(getMailboxId());

        TargetConstraint tcon = null;
        if (mConstraint != null)
            try {
                tcon = TargetConstraint.parseConstraint(mbox, mConstraint);
            } catch (ServiceException e) {
                mLog.warn(e);
            }

            // No extra checking needed because Mailbox.move() is already idempotent.
            mbox.move(getOperationContext(), mIds, type, mDestId, tcon);
    }

    @Override
    public boolean isDeleteOp() {
        return mDestId == Mailbox.ID_FOLDER_TRASH;
    }
}

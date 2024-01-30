/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2015, 2016 Synacor, Inc.
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
package com.zimbra.cs.filter;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zimbra.cs.account.Account;
import com.zimbra.cs.account.MockProvisioning;
import com.zimbra.cs.account.Provisioning;
import com.zimbra.cs.filter.jsieve.ActionFlag;
import com.zimbra.cs.mailbox.DeliveryContext;
import com.zimbra.cs.mailbox.DeliveryOptions;
import com.zimbra.cs.mailbox.Flag;
import com.zimbra.cs.mailbox.Folder;
import com.zimbra.cs.mailbox.MailItem;
import com.zimbra.cs.mailbox.Mailbox;
import com.zimbra.cs.mailbox.MailboxManager;
import com.zimbra.cs.mailbox.MailboxTestUtil;
import com.zimbra.cs.mailbox.Message;
import com.zimbra.cs.mailbox.OperationContext;
import com.zimbra.cs.mime.ParsedMessage;
import com.zimbra.cs.service.util.ItemId;

/**
 * Unit test for {@link ExistingMessageHandler}.
 */
public final class ExistingMessageHandlerTest {

    @BeforeClass
    public static void init() throws Exception {
        MailboxTestUtil.initServer();
        Provisioning prov = Provisioning.getInstance();
        prov.createAccount("test@zimbra.com", "secret", new HashMap<String, Object>());
    }

    @Before
    public void setUp() throws Exception {
        MailboxTestUtil.clearData();
    }

    @Test
    public void existing() throws Exception {
        Account account = Provisioning.getInstance().getAccount(MockProvisioning.DEFAULT_ACCOUNT_ID);
        RuleManager.clearCachedRules(account);
        Mailbox mbox = MailboxManager.getInstance().getMailboxByAccount(account);
        OperationContext octx = new OperationContext(mbox);
        Message msg = mbox.addMessage(octx,
                new ParsedMessage("From: sender@zimbra.com\nTo: test@zimbra.com\nSubject: test".getBytes(), false),
                new DeliveryOptions().setFolderId(Mailbox.ID_FOLDER_INBOX).setFlags(Flag.BITMASK_PRIORITY),
                new DeliveryContext());

        Folder f = mbox.createFolder(null, "test", new Folder.FolderOptions().setDefaultView(MailItem.Type.MESSAGE));
        ExistingMessageHandler handler = new ExistingMessageHandler(octx, mbox, msg.getId(), (int) msg.getSize());
        ItemId newMsgItemId = handler.fileInto("test", new ArrayList<ActionFlag>(), new String[0]);
        Message newMsg = mbox.getMessageById(octx, newMsgItemId.getId());
        Assert.assertEquals(msg.getFolderId(), Integer.parseInt(newMsg.getUnderlyingData().getPrevFolders().split(":")[1]));
    }
}

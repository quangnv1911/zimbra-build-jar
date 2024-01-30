/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

package com.zimbra.soap.admin.message;

import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.HsmConstants;

/**
 * @zm-api-command-network-edition
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required true
 * @zm-api-command-description Queries the status of the most recent HSM session.  Status information for a given HSM
 * session is available until the next time HSM runs or until the server is restarted.
 * <br />
 * Notes:
 * <ul>
 * <li> If an HSM session is running, "endDate" is not specified in the response.
 * <li> As an HSM session runs, numMoved and numMailboxes increase with subsequent requests.
 * <li> A response sent while HSM is aborting returns aborted="0" and aborting="1".
 * <li> If HSM completed successfully, numMailboxes == totalMailboxes.
 * <li> If &lt;GetHsmStatusRequest> is sent after a server restart but before an HSM session, the response will contain
 *      running="0" and no additional information.
 * <li> Once HSM completes, the same &lt;GetHsmStatusResponse> will be returned until another HSM session or a server
 *      restart.
 * </ul>
 */
@XmlRootElement(name=HsmConstants.E_GET_HSM_STATUS_REQUEST)
public class GetHsmStatusRequest {
}

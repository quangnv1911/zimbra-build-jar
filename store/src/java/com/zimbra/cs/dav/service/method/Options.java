/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2006, 2007, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.dav.service.method;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.zimbra.common.service.ServiceException;
import com.zimbra.cs.dav.DavContext;
import com.zimbra.cs.dav.DavException;
import com.zimbra.cs.dav.DavProtocol;
import com.zimbra.cs.dav.resource.DavResource;
import com.zimbra.cs.dav.service.DavMethod;
import com.zimbra.cs.dav.service.DavServlet;

public class Options extends DavMethod {
	public static final String OPTIONS  = "OPTIONS";
	public String getName() {
		return OPTIONS;
	}
	public void handle(DavContext ctxt) throws DavException, IOException, ServiceException {
		HttpServletResponse resp = ctxt.getResponse();
		DavServlet.setAllowHeader(resp);
		if (ctxt.isMsft())
		    resp.addHeader(DavProtocol.HEADER_MS_AUTHOR_VIA, "DAV");
		resp.setContentLength(0);
		try {
	        DavResource rs = ctxt.getRequestedResource();
	        ctxt.setDavCompliance(DavProtocol.getComplianceString(rs.getComplianceList()));
		} catch (Exception e) {}
		sendResponse(ctxt);
	}
}

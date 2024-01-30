/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2010, 2011, 2012, 2013, 2014, 2016 Synacor, Inc.
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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.AdminConstants;
import com.zimbra.soap.type.ZmBoolean;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required true
 * @zm-api-command-description Get all servers defined in the system or all servers that have a particular service
 * enabled (eg, mta, antispam, spell).
 * @zm-api-request-description
 */
@XmlRootElement(name=AdminConstants.E_GET_ALL_SERVERS_REQUEST)
public class GetAllServersRequest {

    /**
     * @zm-api-field-tag service-name
     * @zm-api-field-description Service name.  e.g. mta, antispam, spell.
     */
    @XmlAttribute(name=AdminConstants.A_SERVICE /* service */, required=false)
    private String service;

    /**
     * @zm-api-field-tag alwaysOnClusterId
     * @zm-api-field-description alwaysOnClusterId.
     */
    @XmlAttribute(name=AdminConstants.A_ALWAYSONCLUSTER_ID /* alwaysOnClusterId */, required=false)
    private String alwaysOnClusterId;

    /**
     * @zm-api-field-tag apply-config
     * @zm-api-field-description if {apply-config} is 1 (true), then certain unset attrs on a server will get their
     * value from the global config.
     * <br />
     * if {apply-config} is 0 (false), then only attributes directly set on the server will be returned
     */
    @XmlAttribute(name=AdminConstants.A_APPLY_CONFIG /* applyConfig */, required=false)
    private ZmBoolean applyConfig;

    public GetAllServersRequest() {
        this(null, null);
    }

    public GetAllServersRequest(String service, Boolean applyConfig) {
        setService(service);
        setApplyConfig(applyConfig);
    }

    public GetAllServersRequest(String service, Boolean applyConfig, String clusterId) {
        setService(service);
        setApplyConfig(applyConfig);
        setAlwaysOnClusterId(clusterId);
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setAlwaysOnClusterId(String alwaysOnClusterId) {
        this.alwaysOnClusterId = alwaysOnClusterId;
    }

    public void setApplyConfig(Boolean applyConfig) {
        this.applyConfig = ZmBoolean.fromBool(applyConfig);
    }

    public String getService() { return service; }
    public String getAlwaysOnClusterId() { return alwaysOnClusterId; }
    public Boolean isApplyConfig() { return ZmBoolean.toBool(applyConfig); }
}

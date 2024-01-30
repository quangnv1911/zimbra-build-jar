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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.AdminConstants;
import com.zimbra.soap.admin.type.FreeBusyProviderInfo;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AdminConstants.E_GET_ALL_FREE_BUSY_PROVIDERS_RESPONSE)
public class GetAllFreeBusyProvidersResponse {

    /**
     * @zm-api-field-description Information on Free/Busy providers
     */
    @XmlElement(name=AdminConstants.E_PROVIDER, required=false)
    private List<FreeBusyProviderInfo> providers = Lists.newArrayList();

    public GetAllFreeBusyProvidersResponse() {
    }

    public void setProviders(Iterable <FreeBusyProviderInfo> providers) {
        this.providers.clear();
        if (providers != null) {
            Iterables.addAll(this.providers,providers);
        }
    }

    public GetAllFreeBusyProvidersResponse addProvider(
                    FreeBusyProviderInfo provider) {
        this.providers.add(provider);
        return this;
    }

    public List<FreeBusyProviderInfo> getProviders() {
        return Collections.unmodifiableList(providers);
    }
}

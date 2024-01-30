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

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


import com.google.common.collect.Lists;
import com.zimbra.common.soap.AdminConstants;
import com.zimbra.common.soap.AccountConstants;
import com.zimbra.soap.admin.type.LocaleInfo;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AdminConstants.E_GET_ALL_LOCALES_RESPONSE)
@XmlType(propOrder = {})
public class GetAllLocalesResponse {

    /**
     * @zm-api-field-description Information for system locales
     */
    @XmlElement(name=AccountConstants.E_LOCALE, required=false)
    private List <LocaleInfo> locales = Lists.newArrayList();

    public GetAllLocalesResponse() {
    }

    public GetAllLocalesResponse setLocales(Collection<LocaleInfo> locales) {
        this.locales.clear();
        if (locales != null) {
            this.locales.addAll(locales);
        }
        return this;
    }

    public GetAllLocalesResponse addLocale(LocaleInfo locale) {
        locales.add(locale);
        return this;
    }

    public List<LocaleInfo> getLocales() {
        return Collections.unmodifiableList(locales);
    }
}

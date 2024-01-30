/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.account.ldap.upgrade;

import java.util.HashMap;
import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.StringUtil;
import com.zimbra.cs.account.Config;
import com.zimbra.cs.account.Provisioning;

public class BUG_57039 extends UpgradeOp {

    @Override
    void doUpgrade() throws ServiceException {
        upgradeZimbraGalLdapAttrMap();
    }
    
    void upgradeZimbraGalLdapAttrMap() throws ServiceException {
        Config config = prov.getConfig();
        
        String attrName = Provisioning.A_zimbraGalLdapAttrMap;
        String oldValue = "zimbraCalResLocationDisplayName,displayName=zimbraCalResLocationDisplayName";
        String newValue = "zimbraCalResLocationDisplayName=zimbraCalResLocationDisplayName";
        
        String[] curValues = config.getMultiAttr(attrName);
         
        for (String value : curValues) {
            if (value.equalsIgnoreCase(oldValue)) {
                Map<String, Object> attrs = new HashMap<String, Object>();
                StringUtil.addToMultiMap(attrs, "-" + attrName, oldValue);
                StringUtil.addToMultiMap(attrs, "+" + attrName, newValue);
                
                modifyAttrs(config, attrs);
            }
        }
    }

}

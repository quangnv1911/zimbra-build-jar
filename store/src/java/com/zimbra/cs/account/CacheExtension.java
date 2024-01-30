/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2008, 2009, 2010, 2012, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.account;

import java.util.HashMap;
import java.util.Map;

import com.zimbra.common.service.ServiceException;
import com.zimbra.common.util.ZimbraLog;
import com.zimbra.soap.admin.type.CacheEntryType;

public abstract class CacheExtension {

    private static Map<String, CacheExtension> mHandlers;
    
    /*
     * Register a cache type that can be flushed by the zmprov fc <cache> command.
     * 
     * It should be invoked from the init() method of ZimbraExtension.
     */
    public synchronized static void register(String cacheType, CacheExtension handler) {
        
        if (mHandlers == null)
            mHandlers = new HashMap<String, CacheExtension>();
        else {
            //  make sure the cache is not already registered
            CacheExtension obj = mHandlers.get(cacheType);
            if (obj != null) {
                ZimbraLog.account.warn("cache type " + cacheType + " is already registered, " +
                                       "registering of " + obj.getClass().getCanonicalName() + " is ignored");
                return;
            }    
            
            // make sure the cache type does not clash with one on of the internal cache type
            CacheEntryType cet = null;
            try {
                cet = CacheEntryType.fromString(cacheType);
            } catch (ServiceException e) {
                // this is good
            }
            if (cet != null) {
                ZimbraLog.account.warn("cache type " + cacheType + " is one of the internal cache type, " +
                        "registering of " + obj.getClass().getCanonicalName() + " is ignored");
                return;
            }
        }
        mHandlers.put(cacheType, handler);
    }
    
    public synchronized static CacheExtension getHandler(String cacheType) {
        if (mHandlers == null)
            return null;
        else    
            return mHandlers.get(cacheType);
    }
    
    public abstract void flushCache() throws ServiceException;
    
}

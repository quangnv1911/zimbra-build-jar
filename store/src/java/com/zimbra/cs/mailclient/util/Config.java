/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2008, 2009, 2010, 2013, 2014, 2016 Synacor, Inc.
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
package com.zimbra.cs.mailclient.util;

import java.util.Properties;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;

/**
 * Utility methods for saving and restoring configuration to Java Properties.
 */
public final class Config {
    /**
     * Returns properties for specified configuration.
     *
     * @param bean the configuration bean
     * @return the <tt>Properties</tt> for the bean
     */
    public static Properties toProperties(Object bean) {
        Properties props = new Properties();
        try {
            BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (isSupportedProperty(pd)) {
                    Object value = pd.getReadMethod().invoke(bean);
                    if (value != null) {
                        props.setProperty(pd.getName(), value.toString());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid config bean", e);
        }
        return props;
    }

    /**
     * Applies specified properties to configuration bean.
     *
     * @param bean the configuration bean
     * @param props the <tt>Properties</tt> to be applied to the bean
     */
    public static void applyProperties(Object bean, Properties props) {
        try {
            BeanInfo bi = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                String name = pd.getName();
                String value = props.getProperty(name);
                if (value != null && isSupportedProperty(pd)) {
                    Object obj = parseValue(value, pd.getPropertyType());
                    pd.getWriteMethod().invoke(bean, obj);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid bean", e);
        }
    }

    /**
     * Loads properties from specified configuration file.
     * 
     * @param file the file containing configuration properties
     * @return the configuration <tt>Properties</tt>
     * @throws IOException if an I/O error occurred
     */
    public static Properties loadProperties(File file) throws IOException {
        FileInputStream is = new FileInputStream(file);
        Properties props = new Properties();
        try {
            props.load(is);
        } finally {
            is.close();
        }
        return props;
    }

    /**
     * Saves configuration to specified properties file.
     * 
     * @param file the file to which configuration properties should be saved
     * @param props the <tt>Properties</tt> to be saved
     * @throws IOException if an I/O error occurred
     */
    public static void saveProperties(File file, Properties props)
        throws IOException {
        FileOutputStream os = new FileOutputStream(file);
        try {
            props.store(os, null);
        } finally {
            os.close();
        }
    }
    
    // Check if descriptor indicates a supported property
    private static boolean isSupportedProperty(PropertyDescriptor pd) {
        // Must have both setter and getter method
        if (pd.getReadMethod() == null || pd.getWriteMethod() == null) {
            return false;
        }
        Class type = pd.getPropertyType();
        return type == Long.class || type == Long.TYPE ||
               type == Integer.class || type == Integer.TYPE ||
               type == Short.class || type == Short.TYPE ||
               type == Character.class || type == Character.TYPE ||
               type == Byte.class || type == Byte.TYPE ||
               type == Boolean.class || type == Boolean.TYPE ||
               type == String.class || type.isEnum();
    }

    // Parse property value according to type
    @SuppressWarnings("unchecked")
    private static Object parseValue(String value, Class type) {
        if (type == Long.class || type == Long.TYPE) {
            return Long.parseLong(value);
        } else if (type == Integer.class || type == Integer.TYPE) {
            return Integer.parseInt(value);
        } else if (type == Short.class || type == Short.TYPE) {
            return Short.parseShort(value);
        } else if (type == Character.class || type == Character.TYPE) {
            if (value.length() != 1) {
                throw new IllegalArgumentException(
                    "Invalid character value: " + value);
            }
            return value.charAt(0);
        } else if (type == Byte.class || type == Byte.TYPE) {
            return Byte.parseByte(value);
        } else if (type == Boolean.class || type == Boolean.TYPE) {
            return Boolean.parseBoolean(value);
        } else if (type == String.class) {
            return value;
        } else if (type.isEnum()) {
            return Enum.valueOf(type, value);
        } else {
            return null;
        }
    }
}

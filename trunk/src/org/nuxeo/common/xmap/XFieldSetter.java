/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2008 All Rights Reserved.
 */
package org.nuxeo.common.xmap;

import java.lang.reflect.Field;

/**
 * ��������<code>Contribution</code>����<code>Field</code>�ϱ�ע<code>Annotation</code>
 * ��Ӧֵ�õ�<code>setter</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XFieldSetter.java,v 0.1 2008-4-17 ����01:31:32 xi.hux Exp $
 */
public class XFieldSetter implements XSetter {

    /** field */
    private final Field field;

    /**
     * ����һ��<code>XFieldSetter</code>����
     * 
     * @param field <code>Field</code>����
     */
    public XFieldSetter(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    /**
     * @see org.nuxeo.common.xmap.XSetter#getType()
     */
    public Class<?> getType() {
        return field.getType();
    }

    /**
     * @see org.nuxeo.common.xmap.XSetter#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object instance, Object value) throws IllegalAccessException {
        field.set(instance, value);
    }
}

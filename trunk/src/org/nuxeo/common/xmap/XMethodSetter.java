/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2008 All Rights Reserved.
 */
package org.nuxeo.common.xmap;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * ��������<code>Contribution</code>����<code>Method</code>�ϱ�ע<code>Annotation</code>
 * ��Ӧֵ�õ�<code>getter</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XMethodSetter.java,v 0.1 2008-4-17 ����10:11:17 xi.hux Exp $
 */
public class XMethodSetter implements XSetter {

    /** method */
    private final Method method;

    /**
     * ����һ��<code>XMethodSetter</code>����
     * 
     * @param method <code>Mehtod</code>ʵ��
     */
    public XMethodSetter(Method method) {
        this.method = method;
        this.method.setAccessible(true);
    }

    /**
     * @see org.nuxeo.common.xmap.XSetter#getType()
     */
    public Class<?> getType() {
        return method.getParameterTypes()[0];
    }

    /**
     * @see org.nuxeo.common.xmap.XSetter#setValue(java.lang.Object, java.lang.Object)
     */
    public void setValue(Object instance, Object value)
            throws IllegalAccessException, InvocationTargetException {
        method.invoke(instance, value);
    }

}

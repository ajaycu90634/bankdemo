/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2008 All Rights Reserved.
 */
package org.nuxeo.common.xmap;

import java.lang.reflect.Method;

/**
 * �������<code>Contribution</code>����<code>Field</code>�ϱ�ע<code>Annotation</code>
 * ��Ӧֵ�õ�<code>getter</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XMethodGetter.java,v 0.1 2008-4-17 ����10:02:06 xi.hux Exp $
 */

public class XMethodGetter implements XGetter {

    /** method */
    private Method method;
    
    /** class */
    private Class<?> clazz;
    
    /** bean name */
    private String name;

    /**
     * ����һ��<code>XMethodGetter</code>����
     * 
     * @param method <code>Method</code>ʵ��
     * @param clazz <code>Class</code>ʵ��
     * @param name bean name
     */
    public XMethodGetter(Method method,Class<?> clazz ,String name) {
        this.method = method;
        this.clazz = clazz;
        this.name = name;
    }

    /**
     * @see org.nuxeo.common.xmap.XGetter#getType()
     */
    public Class<?> getType() {
        if(method == null){
            throw new IllegalArgumentException("no such getter method: " + clazz.getName() + '.' + name);
        }
        
        return method.getReturnType();
    }

    /**
     * @see org.nuxeo.common.xmap.XGetter#getValue(java.lang.Object)
     */
    public Object getValue(Object instance) throws Exception {
        if(method == null){
            throw new IllegalArgumentException("no such getter method: " + clazz.getName() + '.' + name);
        }
        
        if (instance == null) {
            return null;
        }
        return method.invoke(instance, new Object[0]);
    }

}

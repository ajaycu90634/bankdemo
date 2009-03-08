/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2007 All Rights Reserved.
 */
package org.nuxeo.common.xmap.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuxeo.common.xmap.annotation.XMemberAnnotation;

/**
 * ����Spring list�ڵ��XMap����
 * @author xi.hux@alipay.com
 *
 * @version $Id$
 */
@XMemberAnnotation(XMemberAnnotation.NODE_LIST_SPRING)
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface XNodeListSpring {

    /**
     * �ڵ�·��.
     *
     * @return the node xpath
     */
    String value();

    /**
     * �Ƿ���Ҫtrim����.
     *
     * @return
     */
    boolean trim() default true;

    /**
     * collection ��������.
     *
     * @return the type of items
     */
    @SuppressWarnings("unchecked")
    Class type();

    /**
     * ��collection�еĶ�������.
     *
     * @return the type of items
     */
    @SuppressWarnings("unchecked")
    Class componentType();
}

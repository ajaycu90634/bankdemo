/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2007 All Rights Reserved.
 */
package org.nuxeo.common.xmap.annotation.spring;

import org.nuxeo.common.xmap.Context;
import org.nuxeo.common.xmap.Path;
import org.nuxeo.common.xmap.XAnnotatedMember;
import org.nuxeo.common.xmap.XGetter;
import org.nuxeo.common.xmap.XMap;
import org.nuxeo.common.xmap.XSetter;
import org.nuxeo.common.xmap.spring.XNodeSpring;
import org.w3c.dom.Element;

/**
 * ��ʾ<code>XNodeSpring</code><code>Annotation</code>�Ķ���
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedSpring.java,v 0.1 2008-4-17 ����10:37:17 xi.hux Exp $
 */
public class XAnnotatedSpring extends XAnnotatedMember {
    /**
     * ��������XAnnotatedSpringObject����
     * ��Ҫ��������������Spring ioc ����ʵ��
     */
    public XAnnotatedSpringObject xaso;

    /**
     * ����һ��<code>XAnnotationSpring</code>���� 
     */
    protected XAnnotatedSpring(XMap xmap, XSetter setter, XGetter getter) {
        super(xmap, setter, getter);
    }

    /**
     * ����һ��<code>XAnnotatedSpring</code>����
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     * @param getter <code>XGetter</code>����
     * @param anno <code>XNodeSrping</code>����
     * @param xaso <code>XAnnotatedSpringObject</coed>����
     */
    public XAnnotatedSpring(XMap xmap, XSetter setter, XGetter getter, XNodeSpring anno,
                            XAnnotatedSpringObject xaso) {
        super(xmap, setter, getter);
        path = new Path(anno.value());
        trim = anno.trim();
        type = setter.getType();
        this.xaso = xaso;
    }

    @Override
    protected Object getValue(Context ctx, Element base) throws Exception {
        // scalar field
        if (type == Element.class) {
            // allow DOM elements as values
            return base;
        }
        //��Spring������ȡ�ö�Ӧ��bean����Resource����
        return XMapSpringUtil.getSpringOjbect(this, xaso.getApplicationContext(), base);
    }
}
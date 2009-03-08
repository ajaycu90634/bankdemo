/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2007 All Rights Reserved.
 */
package org.nuxeo.common.xmap.annotation.spring;

import java.util.Map;

import org.nuxeo.common.xmap.Context;
import org.nuxeo.common.xmap.DOMHelper;
import org.nuxeo.common.xmap.Path;
import org.nuxeo.common.xmap.XAnnotatedMap;
import org.nuxeo.common.xmap.XAnnotatedMember;
import org.nuxeo.common.xmap.XGetter;
import org.nuxeo.common.xmap.XMap;
import org.nuxeo.common.xmap.XSetter;
import org.nuxeo.common.xmap.spring.XNodeMapSpring;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * ��ʾ<code>XNodeMapSpring</code></code>Annotation</code>�Ķ���
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedMapSpring.java,v 0.1 2008-4-17 ����10:32:49 xi.hux Exp $
 */
public class XAnnotatedMapSpring extends XAnnotatedMap {

    /**
     * dom visitor
     */
    protected static final ElementValueMapVisitor   elementVisitor   = new ElementValueMapVisitor();
    protected static final AttributeValueMapVisitor attributeVisitor = new AttributeValueMapVisitor();

    /**
     * ��������XAnnotatedSpringObject����
     * ��Ҫ��������������Spring ioc ����ʵ��
     */
    private XAnnotatedSpringObject                  xaso;

    /**
     * ����һ��<code>XAnnotatedMapSpring</code>����
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     * @param getter <code>XGetter</code>����
     * @param anno <code>XNodeMapSpring</code>����
     * @param xaso <code>XAnnotatedSpringObject</code>����
     */
    public XAnnotatedMapSpring(XMap xmap, XSetter setter, XGetter getter, XNodeMapSpring anno,
                               XAnnotatedSpringObject xaso) {
        super(xmap, setter, getter, null);
        this.setXaso(xaso);
        path = new Path(anno.value());
        trim = anno.trim();
        key = new Path(anno.key());
        type = anno.type();
        componentType = anno.componentType();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getValue(Context ctx, Element base) throws IllegalAccessException,
                                                        InstantiationException {
        Map<String, Object> values = (Map) type.newInstance();
        if (path.attribute != null) {
            // attribute list
            DOMHelper.visitMapNodes(ctx, this, base, path, attributeVisitor, values);
        } else {
            // element list
            DOMHelper.visitMapNodes(ctx, this, base, path, elementVisitor, values);
        }
        return values;
    }

    public void setXaso(XAnnotatedSpringObject xaso) {
        this.xaso = xaso;
    }

    public XAnnotatedSpringObject getXaso() {
        return xaso;
    }
}

class ElementValueMapVisitor extends DOMHelper.NodeMapVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, String key,
                          Map<String, Object> result) {
        String val = node.getTextContent();
        if (val != null && val.length() > 0) {
            if (xam.trim)
                val = val.trim();
            //��Spring������ȡ�ö�Ӧ��bean����Resource����
            Object object = XMapSpringUtil.getSpringObject(
                ((XAnnotatedListSpring) xam).componentType, val, ((XAnnotatedListSpring) xam)
                    .getXaso().getApplicationContext());
            if (object != null)
                result.put(key, object);
        }
    }
}

class AttributeValueMapVisitor extends DOMHelper.NodeMapVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, String key,
                          Map<String, Object> result) {
        String val = node.getNodeValue();
        if (val != null && val.length() > 0) {
            if (xam.trim)
                val = val.trim();
            //��Spring������ȡ�ö�Ӧ��bean����Resource����
            Object object = XMapSpringUtil.getSpringObject(
                ((XAnnotatedMapSpring) xam).componentType, val, ((XAnnotatedMapSpring) xam)
                    .getXaso().getApplicationContext());
            if (object != null)
                result.put(key, object);
        }
    }
}
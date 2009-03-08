/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2007 All Rights Reserved.
 */
package org.nuxeo.common.xmap.annotation.spring;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import org.nuxeo.common.xmap.Context;
import org.nuxeo.common.xmap.DOMHelper;
import org.nuxeo.common.xmap.Path;
import org.nuxeo.common.xmap.XAnnotatedList;
import org.nuxeo.common.xmap.XAnnotatedMember;
import org.nuxeo.common.xmap.XGetter;
import org.nuxeo.common.xmap.XMap;
import org.nuxeo.common.xmap.XSetter;
import org.nuxeo.common.xmap.spring.XNodeListSpring;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * ��ʾ<code>XNodeListSpring</code><code>Annotation</code>�Ķ���
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedListSpring.java,v 0.1 2008-4-17 ����10:28:48 xi.hux Exp $
 */
public class XAnnotatedListSpring extends XAnnotatedList {

    /**
     * dom visitor
     */
    protected static final ElementValueVisitor   elementVisitor   = new ElementValueVisitor();
    protected static final AttributeValueVisitor attributeVisitor = new AttributeValueVisitor();

    /**
     * ��������XAnnotatedSpringObject����
     * ��Ҫ��������������Spring ioc ����ʵ��
     */
    private XAnnotatedSpringObject               xaso;

    /**
     * ����һ��<code>XAnnotatedListSpring</code>���� 
     */
    protected XAnnotatedListSpring(XMap xmap, XSetter setter, XGetter getter) {
        super(xmap, setter, getter);
    }

    /**
     * ����һ��<code>XAnnotatedListSpring</code>���� 
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     * @param getter <code>XGetter</code>����
     * @param anno <code>XNodeListSpring</code>����
     * @param xaso <code>XAnnotatedSpringObject</code>����
     */
    public XAnnotatedListSpring(XMap xmap, XSetter setter, XGetter getter, XNodeListSpring anno,
                                XAnnotatedSpringObject xaso) {
        super(xmap, setter, getter);
        path = new Path(anno.value());
        trim = anno.trim();
        type = anno.type();
        componentType = anno.componentType();
        this.xaso = xaso;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getValue(Context ctx, Element base) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        if (path.attribute != null) {
            // attribute list
            DOMHelper.visitNodes(ctx, this, base, path, attributeVisitor, values);
        } else {
            // element list
            DOMHelper.visitNodes(ctx, this, base, path, elementVisitor, values);
        }

        if (type != ArrayList.class) {
            //��������鲢���ǲ���ԭ����������
            if (type.isArray() && !componentType.isPrimitive()) {
                values.toArray((Object[]) Array.newInstance(componentType, values.size()));
            }
            //�����������Collection
            else {
                Collection col = (Collection) type.newInstance();
                col.addAll(values);
                return col;
            }
        }
        return values;
    }

    public void setXaso(XAnnotatedSpringObject xaso) {
        this.xaso = xaso;
    }

    public XAnnotatedSpringObject getXaso() {
        return xaso;
    }

    public static void main(String[] args) throws Exception {
        Field field = XAnnotatedListSpring.class.getField("list");
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type rawType = pType.getRawType();
            System.out.println("RawType:" + rawType.getClass().getName());
            System.out.println("(" + rawType + ")");

            Type[] actualTypes = pType.getActualTypeArguments();
            System.out.println("actual type arguments are:");
            for (int i = 0; i < actualTypes.length; i++) {
                System.out.println(" instance of " + actualTypes[i].getClass().getName() + ":");
                System.out.println("  (" + actualTypes[i] + ")");
            }
            //            TypeVariable[] tvar = null;
            //            for (int i = 0; i < tvar.length; i++) {
            //                System.out.println("GenericDeclaration:" + tvar[0].getGenericDeclaration());
            //                System.out.println(tvar[0]);
            //                System.out.println("Name:"+ tvar[0].getName());
            //                System.out.println("Class:"+tvar[0].getClass());
            //                Type[] bounds = tvar[0].getBounds();
            //                for (int j = 0; j < bounds.length; j++) {
            //                    System.out.println("Bounds["+j+"]:" +bounds.getClass());
            //                }
            //            }
        }
    }
}

class ElementValueVisitor extends DOMHelper.NodeVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, Collection<Object> result) {
        String val = node.getTextContent();
        if (val != null && val.length() > 0) {
            if (xam.trim)
                val = val.trim();
            Object object = XMapSpringUtil.getSpringObject(
                ((XAnnotatedListSpring) xam).componentType, val, ((XAnnotatedListSpring) xam)
                    .getXaso().getApplicationContext());
            if (object != null)
                result.add(object);
        }
    }
}

class AttributeValueVisitor extends DOMHelper.NodeVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, Collection<Object> result) {
        String val = node.getNodeValue();
        if (val != null && val.length() > 0) {
            if (xam.trim)
                val = val.trim();
            Object object = XMapSpringUtil.getSpringObject(
                ((XAnnotatedListSpring) xam).componentType, val, ((XAnnotatedListSpring) xam)
                    .getXaso().getApplicationContext());
            if (object != null)
                result.add(object);
        }
    }
}

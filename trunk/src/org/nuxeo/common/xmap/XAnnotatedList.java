/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2008 All Rights Reserved.
 */
package org.nuxeo.common.xmap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nuxeo.common.xmap.annotation.XNodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <code>XAnnotatedList</code>���󣬶�Ӧ<code>XNodeList</code><code>Annotation</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedList.java,v 0.1 2008-4-17 ����10:16:19 xi.hux Exp $
 */

public class XAnnotatedList extends XAnnotatedMember {

    protected static final ElementVisitor        elementListVisitor = new ElementVisitor();
    protected static final ElementValueVisitor   elementVisitor     = new ElementValueVisitor();
    protected static final AttributeValueVisitor attributeVisitor   = new AttributeValueVisitor();

    // indicates the type of the collection components
    public Class<?>                              componentType;

    /**
     * ����һ��<code>XAnnotatedList</code>���� 
     */
    protected XAnnotatedList(XMap xmap, XSetter setter, XGetter getter) {
        super(xmap, setter, getter);
    }

    /**
     * ����һ��<code>XAnnotatedList</code>����
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     * @param getter <code>XGetter</code>����
     * @param anno <code> XNodeList</code>����
     */
    public XAnnotatedList(XMap xmap, XSetter setter, XGetter getter, XNodeList anno) {
        super(xmap, setter, getter);
        path = new Path(anno.value());
        trim = anno.trim();
        type = anno.type();
        cdata = anno.cdata();
        componentType = anno.componentType();
        valueFactory = xmap.getValueFactory(componentType);
        xao = xmap.register(componentType);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object getValue(Context ctx, Element base) throws Exception {
        ArrayList<Object> values = new ArrayList<Object>();
        if (xao != null) {
            DOMHelper.visitNodes(ctx, this, base, path, elementListVisitor, values);
        } else {
            if (path.attribute != null) {
                // attribute list
                DOMHelper.visitNodes(ctx, this, base, path, attributeVisitor, values);
            } else {
                // element list
                DOMHelper.visitNodes(ctx, this, base, path, elementVisitor, values);
            }
        }

        if (type != ArrayList.class) {
            if (type.isArray()) {
                if (componentType.isPrimitive()) {
                    // primitive arrays cannot be casted to Object[]
                    return PrimitiveArrays.toPrimitiveArray(values, componentType);
                } else {
                    return values.toArray((Object[]) Array
                        .newInstance(componentType, values.size()));
                }
            } else {
                Collection col = (Collection) type.newInstance();
                col.addAll(values);
                return col;
            }
        }

        return values;
    }

    @SuppressWarnings("unchecked")
    public void decode(Object instance, Node base, Document document, List<String> filters)
                                                                                           throws Exception {
        if (!isFilter(filters)) {
            return;
        }

        Collection col = null;
        if (Collection.class.isAssignableFrom(type)) {
            col = (Collection) getter.getValue(instance);
        } else {
            if (type.isArray()) {
                //                if (componentType.isPrimitive()) {
                //                    throw new Exception(
                //                        "@XNodeList " + base.getNodeName()
                //                                + " 'componentType' atrribute cannt support primitive type!");
                //                }

                col = new ArrayList();

                Object obj = getter.getValue(instance);

                int length = Array.getLength(obj);

                for (int i = 0; i < length; i++) {
                    col.add(Array.get(obj, i));
                }
            } else {
                throw new Exception("@XNodeList " + base.getNodeName()
                                    + " 'type' only support Collection ande Array type");
            }
        }

        // ������Ӧ��Node
        Node node = base;
        int len = path.segments.length - 1;
        for (int i = 0; i < len; i++) {
            // ��ȡ�ö�Ӧ��Node����
            Node n = DOMHelper.getElementNode(node, path.segments[i]);
            // û�д�����Ӧ��Node����
            if (n == null) {
                Element element = document.createElement(path.segments[i]);
                node = node.appendChild(element);
            } else {
                node = n;
            }
        }

        //��Ҫѭ�������Ľڵ�����
        String name = path.segments[len];

        Node lastParentNode = node;

        for (Object object : col) {

            Element element = document.createElement(name);
            node = lastParentNode.appendChild(element);

            // Ƕ�ױ�ǩ
            if (xao != null) {
                xao.decode(object, node, document, filters);
            } else {
                String value = object == null ? "" : object.toString();

                // �����������
                if (path.attribute != null && path.attribute.length() > 0) {
                    Attr attr = document.createAttribute(path.attribute);
                    attr.setNodeValue(value);

                    ((Element) node).setAttributeNode(attr);
                } else {
                    if (cdata) {
                        CDATASection cdataSection = document.createCDATASection(value);
                        node.appendChild(cdataSection);
                    } else {
                        node.setTextContent(value);
                    }
                }
            }
        }

    }
}

/**
 * <code>Element</code><code>NodeVisitor</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedList.java,v 0.1 2008-4-17 ����10:17:44 xi.hux Exp $
 */
class ElementVisitor extends DOMHelper.NodeVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, Collection<Object> result) {
        try {
            result.add(xam.xao.newInstance(ctx, (Element) node));
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }
}

/**
 * <code>ElementValue</code><code>NodeVisitor</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedList.java,v 0.1 2008-4-17 ����10:18:17 xi.hux Exp $
 */
class ElementValueVisitor extends DOMHelper.NodeVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, Collection<Object> result) {
        String val = node.getTextContent();
        if (xam.trim) {
            val = val.trim();
        }
        if (xam.valueFactory != null) {
            result.add(xam.valueFactory.getValue(ctx, val));
        } else {
            // TODO: log warning?
            result.add(val);
        }
    }
}

/**
 * <code>AttributeValue</code><code>NodeVisitor</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedList.java,v 0.1 2008-4-17 ����10:18:30 xi.hux Exp $
 */
class AttributeValueVisitor extends DOMHelper.NodeVisitor {
    @Override
    public void visitNode(Context ctx, XAnnotatedMember xam, Node node, Collection<Object> result) {
        String val = node.getNodeValue();
        if (xam.valueFactory != null) {
            result.add(xam.valueFactory.getValue(ctx, val));
        } else {
            // TODO: log warning?
            result.add(val);
        }
    }
}

package org.nuxeo.common.xmap;

import java.util.List;
import java.util.Map;

import org.nuxeo.common.xmap.annotation.XNode;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * ��ʾһ��<code>Annotation</code>���󣬰�����Ӧ<code>Annotation</code>������
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XAnnotatedMember.java,v 0.1 2008-4-15 ����01:38:10 xi.hux Exp $
 */
public class XAnnotatedMember {

    public final XMap       xmap;
    public final XSetter    setter;
    public final XGetter    getter;
    public Path             path;
    public boolean          trim;
    public boolean          cdata;

    // the java type of the described element
    @SuppressWarnings("unchecked")
    public Class            type;
    // not null if the described object is an xannotated object
    public XAnnotatedObject xao;
    // the value factory used to transform strings in objects compatible
    // with this member type
    // In the case of collection types this factory is
    // used for collection components
    public XValueFactory    valueFactory;

    /**
     * ����һ��<code>XAnnotatedMember</code>����
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     */
    protected XAnnotatedMember(XMap xmap, XSetter setter, XGetter getter) {
        this.xmap = xmap;
        this.setter = setter;
        this.getter = getter;
    }

    /**
     * ����һ��<code>XAnnotatedMember</code>����
     * 
     * @param xmap <code>XMap</code>����
     * @param setter <code>XSetter</code>����
     * @param getter <code>GSetter</code>����
     * @param anno <code>XNode</code>����
     */
    public XAnnotatedMember(XMap xmap, XSetter setter, XGetter getter, XNode anno) {
        this.xmap = xmap;
        this.setter = setter;
        this.getter = getter;
        path = new Path(anno.value());
        trim = anno.trim();
        cdata = anno.cdata();
        type = setter.getType();
        valueFactory = xmap.getValueFactory(type);
        xao = xmap.register(type);
    }

    protected void setValue(Object instance, Object value) throws Exception {
        setter.setValue(instance, value);
    }

    public void process(Context ctx, Element element) throws Exception {
        Object value = getValue(ctx, element);
        if (value != null) {
            setValue(ctx.getObject(), value);
        }
    }

    public void process(Context ctx, Map<String, Object> map, String keyPrefix) throws Exception {
        Object value = getValue(ctx, map, keyPrefix);
        if (value != null) {
            setValue(ctx.getObject(), value);
        }
    }

    public void decode(Object instance, Node base, Document document, List<String> filters)
                                                                                           throws Exception {
        if (!isFilter(filters)) {
            return;
        }

        Node node = base;

        // ������Ӧ�Ľڵ����
        int len = path.segments.length;
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

        // ȡ�ö�Ӧ��ֵ
        Object object = getter.getValue(instance);

        // ��ʱ��֧��ֵΪElement�ĳ���
        if (object != null && Element.class.isAssignableFrom(object.getClass())) {
            return;
        }

        // Ƕ�׶���
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

    protected Object getValue(Context ctx, Element base) throws Exception {
        if (xao != null) {
            Element el = (Element) DOMHelper.getElementNode(base, path);
            return el == null ? null : xao.newInstance(ctx, el);
        }
        // scalar field
        if (type == Element.class) {
            // allow DOM elements as values
            return base;
        }
        String val = DOMHelper.getNodeValue(base, path);
        if (val != null) {
            if (trim) {
                val = val.trim();
            }
            return valueFactory.getValue(ctx, val);
        }
        return null;
    }

    protected Object getValue(Context ctx, Map<String, Object> map, String keyPrefix)
                                                                                     throws Exception {
        String key = keyPrefix == null ? path.path : keyPrefix + path.path;
        Object val = map.get(key);
        Object result = null;

        if (val == null) {
            ;
        } else if (val instanceof String) {
            String str = (String) val;
            if (str != null) {
                if (trim) {
                    str = str.trim();
                }
                result = valueFactory.getValue(ctx, str);
            }
        } else {
            result = val;
        }

        return result;
    }

    protected boolean isFilter(List<String> filters) {
        boolean filter = false;

        if (filters == null || filters.size() == 0) {
            filter = true;
        } else {
            filter = filters.contains(path.path);
        }

        return filter;
    }
}

package org.nuxeo.common.xmap;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * Value factories are used to decode values from XML strings.
 * <p>
 * To register a new factory for a given XMap instance use the method
 * {@link XMap#setValueFactory(Class, XValueFactory)}
 *
 *
 */
@SuppressWarnings("unchecked")
public abstract class XValueFactory {

    static final Map<Class, XValueFactory> defaultFactories = new Hashtable<Class, XValueFactory>();

    public abstract Object getValue(Context ctx, String value);

    public final Object getElementValue(Context ctx, Node element, boolean trim) {
        String text = element.getTextContent();
        return getValue(ctx, trim ? text.trim() : text);
    }

    public final Object getAttributeValue(Context ctx, Node element, String name) {
        Node at = element.getAttributes().getNamedItem(name);
        return at != null ? getValue(ctx, at.getNodeValue()) : null;
    }

    public static void addFactory(Class klass, XValueFactory factory) {
        defaultFactories.put(klass, factory);
    }

    public static XValueFactory getFactory(Class type) {
        return defaultFactories.get(type);
    }

    public static Object getValue(Context ctx, Class klass, String value) {
        XValueFactory factory = defaultFactories.get(klass);
        if (factory == null) {
            return null;
        }
        return factory.getValue(ctx, value);
    }

    public static final XValueFactory STRING   = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return value;
                                                   }
                                               };

    public static final XValueFactory INTEGER  = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return Integer.valueOf(value);
                                                   }
                                               };

    public static final XValueFactory LONG     = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return Long.valueOf(value);
                                                   }
                                               };

    public static final XValueFactory DOUBLE   = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return Double.valueOf(value);
                                                   }
                                               };

    public static final XValueFactory FLOAT    = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return Float.valueOf(value);
                                                   }
                                               };

    public static final XValueFactory BOOLEAN  = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return Boolean.valueOf(value);
                                                   }
                                               };

    public static final XValueFactory DATE     = new XValueFactory() {
                                                   final DateFormat df = DateFormat
                                                                           .getDateInstance();

                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       try {
                                                           return df.parse(value);
                                                       } catch (Exception e) {
                                                           return null;
                                                       }
                                                   }
                                               };

    public static final XValueFactory FILE     = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       return new File(value);
                                                   }
                                               };

    public static final XValueFactory URL      = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       try {
                                                           return new java.net.URL(value);
                                                       } catch (Exception e) {
                                                           return null;
                                                       }
                                                   }
                                               };

    public static final XValueFactory CLASS    = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       try {
                                                           return ctx.loadClass(value);
                                                       } catch (Exception e) {
                                                           e.printStackTrace(); //TODO
                                                           return null;
                                                       }
                                                   }
                                               };

    public static final XValueFactory RESOURCE = new XValueFactory() {
                                                   @Override
                                                   public Object getValue(Context ctx, String value) {
                                                       try {
                                                           return new Resource(ctx
                                                               .getResource(value));
                                                       } catch (Exception e) {
                                                           e.printStackTrace(); //TODO
                                                           return null;
                                                       }
                                                   }
                                               };

    static {
        addFactory(String.class, STRING);
        addFactory(Integer.class, INTEGER);
        addFactory(Long.class, LONG);
        addFactory(Double.class, DOUBLE);
        addFactory(Date.class, DATE);
        addFactory(Boolean.class, BOOLEAN);
        addFactory(File.class, FILE);
        addFactory(java.net.URL.class, URL);

        addFactory(int.class, INTEGER);
        addFactory(long.class, LONG);
        addFactory(double.class, DOUBLE);
        addFactory(float.class, FLOAT);
        addFactory(boolean.class, BOOLEAN);

        addFactory(Class.class, CLASS);
        addFactory(Resource.class, RESOURCE);
    }

}

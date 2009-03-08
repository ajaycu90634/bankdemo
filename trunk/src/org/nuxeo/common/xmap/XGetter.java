/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2008 All Rights Reserved.
 */
package org.nuxeo.common.xmap;

/**
 * �������<code>Contribution</code>�ж�Ӧֵ�õ�<code>getter</code>
 * 
 * @author xi.hux@alipay.com
 * @version $Id: XGetter.java,v 0.1 2008-4-17 ����09:40:53 xi.hux Exp $
 */

public interface XGetter {
    
    /**
     * ����getter��Ӧ��<coede>Class</code>����
     *
     * @return <code>Class</code>
     */
    Class<?> getType();

    /**
     * ����getter�а�����ֵ
     *
     * @param <code>Contribution</code>����
     * @throws Exception 
     */
    Object getValue(Object instance) throws Exception;
}

/*
 * Created on Jul 21, 2004
 */
package org.springframework.jmx;

import org.springframework.jmx.naming.MetadataNamingStrategy;
import org.springframework.jmx.naming.ObjectNamingStrategy;

/**
 * @author robh
 */
public class MetadataNamingStrategyTests extends AbstractNamingStrategyTests {

    private static final String OBJECT_NAME = "spring:bean=test";


    protected ObjectNamingStrategy getStrategy() throws Exception {
        return new MetadataNamingStrategy();
    }

    protected Object getManagedResource() throws Exception {
        return new JmxTestBean();
    }

    protected String getKey() {
        return "foo";
    }

    protected String getCorrectObjectName() {
        return OBJECT_NAME;
    }
}

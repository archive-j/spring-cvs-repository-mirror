/*
 * The Spring Framework is published under the terms of the Apache Software
 * License.
 */
package org.springframework.rules.predicates.beans;

import org.springframework.rules.values.AspectAccessStrategy;
import org.springframework.util.Assert;

/**
 * Convenience superclass for bean property expressions.
 * 
 * @author Keith Donald
 */
public abstract class AbstractBeanPropertyExpression implements
        BeanPropertyExpression {
    private String propertyName;

    protected AbstractBeanPropertyExpression() {
        
    }
    
    protected AbstractBeanPropertyExpression(String propertyName) {
        setPropertyName(propertyName);
    }

    public String getPropertyName() {
        return propertyName;
    }

    protected void setPropertyName(String propertyName) {
        Assert.notNull(propertyName);
        this.propertyName = propertyName;
    }

    public boolean test(Object o) {
        if (o instanceof AspectAccessStrategy) {
            return test((AspectAccessStrategy)o);
        }
        else {
            return testJavaBean(o);
        }
    }

    protected abstract boolean test(
            AspectAccessStrategy domainObjectAccessStrategy);

    protected abstract boolean testJavaBean(Object javaBean);

    public String toString() {
        return getPropertyName();
    }

}
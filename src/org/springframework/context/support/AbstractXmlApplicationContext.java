/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.support;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.core.io.Resource;

/**
 * Convenient abstract superclass for ApplicationContext implementations,
 * drawing configuration from XML documents containing bean definitions
 * understood by an XmlBeanDefinitionParser.
 *
 * <p>Subclasses just have to implement the <code>getConfigLocations</code>
 * method. Furthermore, they might override the <code>getResourceByPath</code>
 * hook to interpret relative paths in an environment-specific fashion, and/or
 * <code>getResourcePatternResolver</code> for extended pattern resolution.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see #getConfigLocations
 * @see #getResourceByPath
 * @see #getResourcePatternResolver
 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionParser
 */
public abstract class AbstractXmlApplicationContext extends AbstractApplicationContext  {

	/** Bean factory for this context */
	private ConfigurableListableBeanFactory beanFactory;

	/**
	 * Create a new AbstractXmlApplicationContext with no parent.
	 */
	public AbstractXmlApplicationContext() {
	}

	/**
	 * Create a new AbstractXmlApplicationContext with the given parent context.
	 * @param parent the parent context
	 */
	public AbstractXmlApplicationContext(ApplicationContext parent) {
		super(parent);
	}

	protected final void refreshBeanFactory() throws BeansException {
		// Shut down previous bean factory, if any.
		if (this.beanFactory != null) {
			this.beanFactory.destroySingletons();
			this.beanFactory = null;
		}

		// Initialize fresh bean factory.
		try {
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
			beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
			initBeanDefinitionReader(beanDefinitionReader);
			loadBeanDefinitions(beanDefinitionReader);
			this.beanFactory = beanFactory;
			if (logger.isInfoEnabled()) {
				logger.info("Bean factory for application context [" + getDisplayName() + "]: " + beanFactory);
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException(
					"I/O error parsing XML document for application context [" + getDisplayName() + "]", ex);
		}
	}

	public final ConfigurableListableBeanFactory getBeanFactory() {
		if (this.beanFactory == null) {
			throw new IllegalStateException("BeanFactory not initialized - " +
					"call 'refresh' before accessing beans via the context: " + this);

		}
		return this.beanFactory;
	}

	/**
	 * Create the bean factory for this context.
	 * <p>Default implementation creates a DefaultListableBeanFactory with the
	 * internal bean factory of this context's parent as parent bean factory.
	 * <p>Can be overridden in subclasses.
	 * @return the bean factory for this context
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory
	 * @see #getInternalParentBeanFactory
	 */
	protected DefaultListableBeanFactory createBeanFactory() {
		return new DefaultListableBeanFactory(getInternalParentBeanFactory());
	}

	/**
	 * Initialize the bean definition reader used for loading the bean
	 * definitions of this context. Default implementation is empty.
	 * <p>Can be overridden in subclasses, e.g. for turning off XML validation
	 * or using a different XmlBeanDefinitionParser implementation.
	 * @param beanDefinitionReader the bean definition reader used by this context
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setValidating
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setParserClass
	 */
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
	}

	/**
	 * Load the bean definitions with the given XmlBeanDefinitionReader.
	 * <p>The lifecycle of the bean factory is handled by the refreshBeanFactory method;
	 * therefore this method is just supposed to load and/or register bean definitions.
	 * <p>Delegates to a ResourcePatternResolver for resolving location patterns
	 * into Resource instances.
	 * @throws BeansException in case of bean registration errors
	 * @throws IOException if the required XML document isn't found
	 * @see #refreshBeanFactory
	 * @see #getConfigLocations
	 * @see #getResources
	 * @see #getResourcePatternResolver
	 */
	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			for (int i = 0; i < configLocations.length; i++) {
				Resource[] configResources = getResources(configLocations[i]);
				for (int j = 0; j < configResources.length; j++) {
					reader.loadBeanDefinitions(configResources[j]);
				}
			}
		}
	}

	/**
	 * Return an array of resource locations, referring to the XML bean
	 * definition files that this context should be built with.
	 * <p>Can also include location patterns, which will get resolved
	 * via a ResourcePatternResolver.
	 * @return an array of resource locations, or null if none
	 * @see #getResources
	 * @see #getResourcePatternResolver
	 */
	protected abstract String[] getConfigLocations();

}

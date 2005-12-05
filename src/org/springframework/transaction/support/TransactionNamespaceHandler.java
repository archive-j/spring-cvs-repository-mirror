/*
 * Copyright 2002-2005 the original author or authors.
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

package org.springframework.transaction.support;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.support.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.support.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.support.NamespaceHandlerSupport;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @author Rob Harrop
 */
public class TransactionNamespaceHandler extends NamespaceHandlerSupport {

	public TransactionNamespaceHandler() {
		registerBeanDefinitionParser("advice", new TxAdviceBeanDefinitionParser());
		registerBeanDefinitionDecorator("metadata", new TxMetadataBeanDefinitionDecorator());
	}


	private static class TxMetadataBeanDefinitionDecorator implements BeanDefinitionDecorator {


		public BeanDefinitionHolder decorate(Element element, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {

			String attributes = DomUtils.getTextValue(element);

			// create the NameMatchTransactionAttributeSource bean
			RootBeanDefinition metadataDefinition = new RootBeanDefinition(NameMatchTransactionAttributeSource.class);
			metadataDefinition.setPropertyValues(new MutablePropertyValues());
			metadataDefinition.getPropertyValues().addPropertyValue("properties", attributes);

			String name = definition.getBeanName() + TransactionReactor.METADATA_SUFFIX;
			registry.registerBeanDefinition(name, metadataDefinition);

			return definition;
		}
	}

	private static class TxAdviceBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

		private static final String ANNOTATION_SOURCE_CLASS_NAME = "org.springframework.transaction.annotation.AnnotationTransactionAttributeSource";

		public static final String TRANSACTION_ATTRIBUTES = "transactionAttributes";

		public static final String TRANSACTION_MANAGER = "transactionManager";

		public static final String ATTRIBUTES = "attributes";

		protected BeanDefinition doParse(Element element) {
			RootBeanDefinition definition = new RootBeanDefinition(TransactionInterceptor.class);

			MutablePropertyValues mpvs = new MutablePropertyValues();
			definition.setPropertyValues(mpvs);

			// set the transaction manager property
			mpvs.addPropertyValue(TRANSACTION_MANAGER, new RuntimeBeanReference(element.getAttribute(TRANSACTION_MANAGER)));

			List txAttributes = DomUtils.getChildElementsByTagName(element, ATTRIBUTES, true);

			if (txAttributes.size() > 1) {
				throw new IllegalStateException("Element 'attributes' is allowed at most once inside element 'advice'");
			}
			else if (txAttributes.size() == 1) {
				// using Attributes source
				String attributes = DomUtils.getTextValue((Element) txAttributes.get(0));
				mpvs.addPropertyValue(TRANSACTION_ATTRIBUTES, attributes);
			}
			else {
				// assume annotations source
				// TODO: fix this to use direct class reference when building all on 1.5
				// TODO: do we bother supporting Commons Attributes?
				Class beanClass = null;
				try {
					beanClass = ClassUtils.forName(ANNOTATION_SOURCE_CLASS_NAME);
				}
				catch (ClassNotFoundException e) {
					throw new IllegalStateException("Unable to locate AnnotationTransactionAttributeSource. Are you running on Java 1.5?");
				}

				mpvs.addPropertyValue("transactionAttributeSource", new RootBeanDefinition(beanClass));
			}
			return definition;
		}
	}
}

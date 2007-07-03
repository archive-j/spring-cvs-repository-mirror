/*
 * Copyright 2002-2007 the original author or authors.
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

package org.springframework.beans.factory.annotation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.PropertyValues;
import org.springframework.util.ReflectionUtils;

/**
 * Internal class for managing injection metadata.
 * Not intended for direct use in applications.
 *
 * <p>Used by {@link CommonAnnotationBeanPostProcessor},
 * {@link AutowiredAnnotationBeanPostProcessor} and
 * {@link org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor}.
 *
 * @author Juergen Hoeller
 * @since 2.1
 */
public class InjectionMetadata {

	private Set<InjectedElement> resourceFields = new LinkedHashSet<InjectedElement>();

	private Set<InjectedElement> resourceMethods = new LinkedHashSet<InjectedElement>();


	public void addInjectedField(InjectedElement element) {
		this.resourceFields.add(element);
	}

	public void addInjectedMethod(InjectedElement element) {
		this.resourceMethods.add(element);
	}


	public void injectFields(Object target, String beanName) throws Throwable {
		for (Iterator it = this.resourceFields.iterator(); it.hasNext();) {
			InjectedElement element = (InjectedElement) it.next();
			element.inject(target, beanName, null);
		}
	}

	public void injectMethods(Object target, String beanName, PropertyValues pvs) throws Throwable {
		for (Iterator it = this.resourceMethods.iterator(); it.hasNext();) {
			InjectedElement element = (InjectedElement) it.next();
			element.inject(target, beanName, pvs);
		}
	}


	public static abstract class InjectedElement {

		protected final Member member;

		protected final boolean isField;

		private PropertyDescriptor pd;

		protected InjectedElement(Member member) {
			this.member = member;
			this.isField = (member instanceof Field);
		}

		protected InjectedElement(Member member, PropertyDescriptor pd) {
			this(member);
			this.pd = pd;
		}

		protected final Class getResourceType() {
			return (this.isField ?
					((Field) this.member).getType() : ((Method) this.member).getParameterTypes()[0]);
		}

		protected final void checkResourceType(Class resourceType) {
			if (this.isField) {
				Class fieldType = ((Field) this.member).getType();
				if (!fieldType.isAssignableFrom(resourceType)) {
					throw new IllegalStateException("Specified resource type [" + resourceType.getName() +
							"] is not assignable to field type [" + fieldType + "]");
				}
			}
			else {
				Class paramType = ((Method) this.member).getParameterTypes()[0];
				if (!paramType.isAssignableFrom(resourceType)) {
					throw new IllegalStateException("Specified resource type [" + resourceType.getName() +
							"] is not assignable to method parameter type [" + paramType + "]");
				}
			}
		}

		/**
		 * Either this or {@link #getResourceToInject} needs to be overridden.
		 */
		protected void inject(Object target, String requestingBeanName, PropertyValues pvs) throws Throwable {
			if (this.isField) {
				Field field = (Field) this.member;
				ReflectionUtils.makeAccessible(field);
				field.set(target, getResourceToInject(requestingBeanName));
			}
			else {
				if (this.pd != null && pvs != null && pvs.contains(this.pd.getName())) {
					// Explicit value provided as part of the bean definition.
					return;
				}
				try {
					Method method = (Method) this.member;
					ReflectionUtils.makeAccessible(method);
					method.invoke(target, getResourceToInject(requestingBeanName));
				}
				catch (InvocationTargetException ex) {
					throw ex.getTargetException();
				}
			}
		}

		/**
		 * Either this or {@link #inject} needs to be overridden.
		 */
		protected Object getResourceToInject(String requestingBeanName) {
			return null;
		}

		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (!(other instanceof InjectedElement)) {
				return false;
			}
			InjectedElement otherElement = (InjectedElement) other;
			if (this.isField) {
				return this.member.equals(otherElement.member);
			}
			else {
				return (otherElement.member instanceof Method &&
						this.member.getName().equals(otherElement.member.getName()) &&
						Arrays.equals(((Method) this.member).getParameterTypes(),
								((Method) otherElement.member).getParameterTypes()));
			}
		}

		public int hashCode() {
			return this.member.getClass().hashCode() * 29 + this.member.getName().hashCode();
		}
	}

}

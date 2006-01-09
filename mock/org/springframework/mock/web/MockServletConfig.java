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

package org.springframework.mock.web;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.springframework.util.Assert;

/**
 * Mock implementation of the ServletConfig interface.
 *
 * <p>Used for testing the web framework; typically not
 * necessary for testing application controllers.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 1.0.2
 */
public class MockServletConfig implements ServletConfig {

	private final ServletContext servletContext;

	private final String servletName;

	private final Properties initParameters = new Properties();


	/**
	 * Create new MockServletConfig with empty String as name.
	 * @param servletContext the ServletContext that the servlet runs in
	 */
	public MockServletConfig(ServletContext servletContext) {
		this(servletContext, "");
	}

	/**
	 * Create new MockServletConfig.
	 * @param servletContext the ServletContext that the servlet runs in
	 * @param servletName the name of the servlet
	 */
	public MockServletConfig(ServletContext servletContext, String servletName) {
		this.servletContext = servletContext;
		this.servletName = servletName;
	}


	public String getServletName() {
		return servletName;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void addInitParameter(String name, String value) {
		Assert.notNull(name, "Parameter name must not be null");
		this.initParameters.setProperty(name, value);
	}

	public String getInitParameter(String name) {
		Assert.notNull(name, "Parameter name must not be null");
		return this.initParameters.getProperty(name);
	}

	public Enumeration getInitParameterNames() {
		return this.initParameters.keys();
	}

}

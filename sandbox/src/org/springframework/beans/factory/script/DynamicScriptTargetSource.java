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

package org.springframework.beans.factory.script;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.dynamic.DynamicBeanTargetSource;

/**
 * Introduction interceptor that provides DynamicScript implementation for all Groovy objects. <br>
 * 
 * @author Rod Johnson
 */
public class DynamicScriptTargetSource extends DynamicBeanTargetSource implements DynamicScript {

	private Script script;

	public DynamicScriptTargetSource(Object initialTarget, BeanFactory factory, String name, Script script) {
		super(initialTarget, factory, name, null);
		setExpirableObject(script);
		this.script = script;
	}
	
	public Script getScript() {
		return script;
	}

	public String getResourceString() {
		return script.getResourceString();
	}

	public Object createObject() throws BeansException {
		return script.createObject();
	}


	public Class[] getInterfaces() {
		return script.getInterfaces();
	}

	public void addInterface(Class intf) {
		throw new UnsupportedOperationException("Not supported on constructed scripts");
	}
}
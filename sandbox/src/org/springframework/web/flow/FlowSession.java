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
package org.springframework.web.flow;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.DefaultObjectStyler;
import org.springframework.util.ToStringCreator;
import org.springframework.util.closure.Constraint;

/**
 * A user session instance of a flow.
 * 
 * @author Keith Donald
 */
public class FlowSession implements MutableAttributesAccessor, Serializable {
	private static final Log logger = LogFactory.getLog(FlowSession.class);

	public static final String CURRENT_STATE_ID_ATTRIBUTE_NAME = "currentStateId";

	public static final String FLOW_SESSION_ID_ATTRIBUTE_NAME = "flowSessionId";

	private String flowId;

	private String currentStateId;

	private Map attributes = new HashMap();

	public FlowSession(String flowId, String startingStateId) {
		this.flowId = flowId;
		this.currentStateId = startingStateId;
	}

	public FlowSession(String flowId, String startingStateId, Map input) {
		this.flowId = flowId;
		this.currentStateId = startingStateId;
		if (input != null) {
			setAttributes(input);
		}
	}

	/**
	 * @return Returns the id.
	 */
	public String getFlowId() {
		return flowId;
	}

	/**
	 * @return Returns the stateId.
	 */
	public String getCurrentStateId() {
		return currentStateId;
	}
	
	protected void setCurrentStateId(String stateId) {
		Assert.notNull(stateId, "The currentStateId is required");
		if (this.currentStateId != null) {
			if (this.currentStateId.equals(stateId)) {
				throw new IllegalArgumentException("The current state is already set to '" + this.currentStateId
						+ "' - this should not happen!");
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Setting current state of this '" + getFlowId() + "' flow session to '" + stateId + "'");
		}
		this.currentStateId = stateId;
	}

	/**
	 * @return Returns the attributes.
	 */
	public Map getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	public Object getAttribute(String attributeName) {
		return attributes.get(attributeName);
	}

	public Object getAttribute(String attributeName, Class requiredType) throws IllegalStateException {
		Object value = getAttribute(attributeName);
		if (requiredType != null && value != null) {
			Assert.isInstanceOf(requiredType, value);
		}
		return value;
	}

	public Object getRequiredAttribute(String attributeName) throws IllegalStateException {
		Object value = getAttribute(attributeName);
		if (value == null) {
			throw new IllegalStateException("Required attribute '" + attributeName
					+ "' is not present in flow scope for flow '" + getFlowId()
					+ "'; attributes currently in scope are = " + DefaultObjectStyler.call(attributes));
		}
		return value;
	}

	public Object getRequiredAttribute(String attributeName, Class clazz) throws IllegalStateException {
		Object value = getRequiredAttribute(attributeName);
		if (clazz != null) {
			Assert.isInstanceOf(clazz, value);
		}
		return value;
	}

	public boolean containsAttribute(String attributeName) {
		return attributes.containsKey(attributeName);
	}

	public boolean containsAttribute(String attributeName, Class requiredType) {
		try {
			getRequiredAttribute(attributeName, requiredType);
			return true;
		}
		catch (IllegalStateException e) {
			return false;
		}
	}

	public void assertAttributePresent(String attributeName, Class requiredType) throws IllegalStateException {
		getRequiredAttribute(attributeName, requiredType);
	}

	public void assertAttributePresent(String attributeName) throws IllegalStateException {
		getRequiredAttribute(attributeName);
	}

	public Collection attributeNames() {
		return Collections.unmodifiableCollection(attributes.keySet());
	}

	public Collection attributeValues() {
		return Collections.unmodifiableCollection(attributes.values());
	}

	public Collection attributeEntries() {
		return Collections.unmodifiableCollection(attributes.entrySet());
	}

	public Collection findAttributes(Constraint criteria) {
		Iterator it = attributeEntries().iterator();
		Collection filteredEntries = new LinkedHashSet();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			if (criteria.test(entry)) {
				filteredEntries.add(entry);
			}
		}
		return filteredEntries;
	}
	
	public void setAttribute(String attributeName, Object attributeValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting flow '" + getFlowId() + "' attribute '" + attributeName + "' to '" + attributeValue
					+ "'");
		}
		this.attributes.put(attributeName, attributeValue);
	}

	public void setAttributes(Map attributes) {
		if (attributes == null) {
			return;
		}
		Iterator it = attributes.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			Assert.isInstanceOf(String.class, e.getKey());
			setAttribute((String)e.getKey(), e.getValue());
		}
	}

	public void removeAttribute(String attributeName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing flow '" + getFlowId() + "' attribute '" + attributeName);
		}
		this.attributes.remove(attributeName);
	}

	public String toString() {
		return new ToStringCreator(this).append("flowId", flowId).append("currentStateId", currentStateId).append(
				"attributesCount", (attributes != null ? attributes.size() : 0)).append("attributes", attributes)
				.toString();
	}

}
/*
 * Copyright 2002-2006 the original author or authors.
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

package org.springframework.beans.factory.support;

import org.springframework.beans.Mergeable;
import org.springframework.core.CollectionFactory;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Tag collection class used to hold managed Map values,
 * which may include runtime bean references.
 * <p/>
 * <p>Wraps a target Map, which will be a linked map if possible
 * (that is, if running on JDK 1.4 or if Commons Collections 3.x is available).
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @see org.springframework.core.CollectionFactory#createLinkedMapIfPossible
 * @since 27.05.2003
 */
public class ManagedMap implements Map, Mergeable {

	private Map targetMap;

	private boolean mergeEnabled;

	private Object source;

	public ManagedMap() {
		this(16);
	}

	public ManagedMap(int initialCapacity) {
		this.targetMap = CollectionFactory.createLinkedMapIfPossible(initialCapacity);
	}

	public ManagedMap(Map targetMap) {
		this.targetMap = targetMap;
	}


	public void setMergeEnabled(boolean mergeEnabled) {
		this.mergeEnabled = mergeEnabled;
	}

	public boolean isMergeEnabled() {
		return mergeEnabled;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public synchronized Object merge(Object parent) {
		if (!this.mergeEnabled) {
			throw new IllegalStateException("Cannot merge when the mergeEnabled property is false.");
		}
		Assert.notNull(parent);
		if (parent instanceof Map) {
			Map parentMap = (Map) parent;
			Map temp = CollectionFactory.createLinkedMapIfPossible(parentMap.size() + this.size());
			temp.putAll(parentMap);
			temp.putAll(this);
			return temp;
		}
		throw new IllegalArgumentException("Cannot merge object with object of type [" + parent.getClass() + "]");
	}


	public int size() {
		return this.targetMap.size();
	}

	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return this.targetMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.targetMap.containsValue(value);
	}

	public Object get(Object key) {
		return this.targetMap.get(key);
	}

	public Object put(Object key, Object value) {
		return this.targetMap.put(key, value);
	}

	public Object remove(Object key) {
		return this.targetMap.remove(key);
	}

	public void putAll(Map t) {
		this.targetMap.putAll(t);
	}

	public void clear() {
		this.targetMap.clear();
	}

	public Set keySet() {
		return this.targetMap.keySet();
	}

	public Collection values() {
		return this.targetMap.values();
	}

	public Set entrySet() {
		return this.targetMap.entrySet();
	}

	public int hashCode() {
		return this.targetMap.hashCode();
	}

	public boolean equals(Object obj) {
		return this.targetMap.equals(obj);
	}

	public String toString() {
		return this.targetMap.toString();
	}

}

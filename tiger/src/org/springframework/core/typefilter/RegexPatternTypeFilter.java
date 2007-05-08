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

package org.springframework.core.typefilter;

import java.util.regex.Pattern;

/**
 * A simple filter for matching a fully-qualified classname with a {@link Pattern}.
 * 
 * @author Mark Fisher
 * @since 2.1
 */
public class RegexPatternTypeFilter extends AbstractClassTestingTypeFilter {

	private Pattern pattern;
	
	public RegexPatternTypeFilter(Pattern pattern) {
		this.pattern = pattern;
	}
	
	@Override
	protected boolean match(ClassNameAndTypesReadingVisitor v) {
		return pattern.matcher(v.getClassName()).matches();
	}

}

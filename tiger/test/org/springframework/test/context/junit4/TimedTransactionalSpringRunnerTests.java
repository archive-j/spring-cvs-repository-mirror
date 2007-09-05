/*
 * Copyright 2007 the original author or authors.
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
package org.springframework.test.context.junit4;

import static org.springframework.test.context.junit4.AbstractTransactionalSpringRunnerTests.assertInTransaction;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.NotTransactional;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * JUnit 4 based unit test which verifies support of Spring's
 * {@link Transactional @Transactional} and
 * {@link NotTransactional @NotTransactional} annotations in conjunction with
 * {@link Timed @Timed} and JUnit 4's {@link Test#timeout() timeout} attribute.
 * </p>
 *
 * @author Sam Brannen
 * @version $Revision$
 * @since 2.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "transactionalTests-context.xml" })
@Transactional
public class TimedTransactionalSpringRunnerTests {

	// ------------------------------------------------------------------------|
	// --- STATIC METHODS -----------------------------------------------------|
	// ------------------------------------------------------------------------|

	// XXX Remove suite() once we've migrated to Ant 1.7 with JUnit 4 support.
	public static junit.framework.Test suite() {

		return new JUnit4TestAdapter(TimedTransactionalSpringRunnerTests.class);
	}

	// ------------------------------------------------------------------------|
	// --- INSTANCE METHODS ---------------------------------------------------|
	// ------------------------------------------------------------------------|

	@Timed(millis = 10000)
	public void transactionalWithSpringTimeout() {

		assertInTransaction(true);
	}

	@Test(timeout = 10000)
	public void transactionalWithJUnitTimeout() {

		// Note: the combination of JUnit's @Test(timeout=...) and Spring's
		// @Transactional do NOT mix, since JUnit 4 runs timeout code in a new
		// thread in which the ThreadLocal for the transaction management is not
		// visible. Thus the following assertion is 'false'.
		assertInTransaction(false);
	}

	@Test
	@NotTransactional
	@Timed(millis = 10000)
	public void notTransactionalWithSpringTimeout() {

		assertInTransaction(false);
	}

	@Test(timeout = 10000)
	@NotTransactional
	public void notTransactionalWithJUnitTimeout() {

		assertInTransaction(false);
	}
}

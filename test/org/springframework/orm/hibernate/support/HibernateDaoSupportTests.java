package org.springframework.orm.hibernate.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.sf.hibernate.SessionFactory;

import org.easymock.MockControl;

/**
 * @author Juergen Hoeller
 * @since 30.07.2003
 */
public class HibernateDaoSupportTests extends TestCase {

	public void testHibernateDaoSupport() throws Exception {
		MockControl sfControl = MockControl.createControl(SessionFactory.class);
		SessionFactory sf = (SessionFactory) sfControl.getMock();
		sfControl.replay();
		final List test = new ArrayList();
		HibernateDaoSupport dao = new HibernateDaoSupport() {
			protected void initDao() {
				test.add("test");
			}
		};
		dao.setSessionFactory(sf);
		dao.afterPropertiesSet();
		assertEquals("Correct SessionFactory", dao.getSessionFactory(), sf);
		assertEquals("Correct HibernateTemplate", dao.getHibernateTemplate().getSessionFactory(), sf);
		assertEquals("initDao called", test.size(), 1);
	}

}

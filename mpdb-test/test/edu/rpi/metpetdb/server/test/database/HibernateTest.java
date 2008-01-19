package edu.rpi.metpetdb.server.test.database;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.After;
import org.junit.Before;

import edu.rpi.metpetdb.server.DataStoreTest;

public class HibernateTest extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Basically test the database to make sure every hibernate class is in
	 * there
	 * 
	 * @throws Exception
	 */
	public void testClassExistsInDatabase() throws Exception {
		DataStoreTest.init();
		SessionFactory sessionFactory = DataStoreTest.open().getSessionFactory();
		Map metadata = sessionFactory.getAllClassMetadata();
		for (Iterator i = metadata.values().iterator(); i.hasNext();) {
			Session session = sessionFactory.openSession();
			try {
				EntityPersister persister = (EntityPersister) i.next();
				String className = persister.getClassMetadata().getEntityName();
				List result = session.createQuery("from " + className + " c")
						.list();
			} finally {
				session.close();
			}
		}
	}

	@After
	public void tearDown() throws Exception {
	}

}

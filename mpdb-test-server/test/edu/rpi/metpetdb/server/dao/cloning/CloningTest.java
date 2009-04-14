package edu.rpi.metpetdb.server.dao.cloning;

import static org.junit.Assert.*;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.beanlib.transformer.CustomTransformersFactory;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.stateless.StatelessProxyStore;

import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.GeometryCustomTransformer;

/**
 * Tests cloning/merging from gilead
 * @author anthony
 *
 */
public class CloningTest extends DatabaseTestCase {


	public CloningTest() {
		super("test-data/test-sample-data.xml");
	}
	
	
	@Before
	public void setUp() {
		super.setUp();
		DataStore.initFactory();
		final HibernateUtil hu = new HibernateUtil() {
			@Override
			public boolean isPersistentClass(Class<?> clazz) {
				if (clazz.equals(Results.class)) {
					return true;
				} else {
					return super.isPersistentClass(clazz);
				}
			}
		};
		hu.setSessionFactory(DataStore.getFactory());
		PersistentBeanManager.getInstance().setPersistenceUtil(hu);
		final StatelessProxyStore sps = new StatelessProxyStore();
		PersistentBeanManager.getInstance().setProxyStore(sps);
		CustomTransformersFactory.getInstance().addCustomBeanTransformer(
				GeometryCustomTransformer.class);
	}
	
	@Test
	public void cloneSample() throws NoSuchObjectException {
		final Sample s = super.byId("Sample", PUBLIC_SAMPLE);
		session.close();
		final Sample cloned = (Sample) PersistentBeanManager.getInstance().clone(s);
		assertEquals(s, cloned);
	}
	
	@Test
	public void mergeSample() throws NoSuchObjectException {
		final Sample s = super.byId("Sample", PUBLIC_SAMPLE);
		session.close();
		final Sample merged = (Sample) PersistentBeanManager.getInstance().merge(s);
		assertEquals(s, merged);
	}
}

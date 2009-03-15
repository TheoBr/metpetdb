package edu.rpi.metpetdb.server.dao.oxide;

import static org.junit.Assert.assertEquals;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.server.DataStore;

public class OxideDaoTest {
	
	private static Session s;

	@Before
	public void setUp() {
		DataStore.initFactory();

		s = DataStore.open();

	}

	@After
	public void tearDown() {
		s.close();
	}
	
	@Test
	public void loadOxide() {
		final Query q = s.getNamedQuery("Oxide.bySpecies").setParameter("species",
				"Al2O3");
		final Oxide oxide = (Oxide) q.uniqueResult();
		assertEquals("Al2O3", oxide.getSpecies());
	}

}

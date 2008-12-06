package edu.rpi.metpetdb.server.dao;



import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.DatabaseTestCase;

public class PermissionsTest extends DatabaseTestCase {

	public PermissionsTest() {
		super("test-data/test-sample-data.xml");
	}
	
	public void testLoadSamplePermissions() throws NoSuchObjectException {
		final Sample s = super.byId("Sample", 1);
		assertEquals(s.getId(),0);
	}

	public void testCreateSamplePermissions() {
		final Sample s = new Sample();
		final Session session = DataStore.open();
		session.beginTransaction();
		session.persist(s);
		session.getTransaction().commit();
	}

}

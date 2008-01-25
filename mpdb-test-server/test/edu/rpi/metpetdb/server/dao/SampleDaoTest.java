package edu.rpi.metpetdb.server.dao;

import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;

public class SampleDaoTest extends DatabaseTestCase {
	
	public SampleDaoTest() {
		super("test-data/test-sample-data.xml");
	}
	
	@Test
	public void testSampleById() {
		final Query q = InitDatabase.getSession().getNamedQuery("Sample.byId");
		q.setLong("id", 1);
		final Sample s = (Sample) q.uniqueResult();
		assertEquals(s.getId(),1);
	}
	
	@Test
	public void testSampleAllSize() {
		final Query q = InitDatabase.getSession().getNamedQuery("Sample.all,size");
		assertEquals(((Number) q.uniqueResult()).intValue(), 1);
	}
}

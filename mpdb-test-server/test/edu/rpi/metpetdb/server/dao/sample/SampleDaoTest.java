package edu.rpi.metpetdb.server.dao.sample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.dao.Verify;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class SampleDaoTest extends DatabaseTestCase {

	private final static String typeName = "Sample";

	public SampleDaoTest() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Test loading a sample by its id, a valid id should be given
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void loadById() throws NoSuchObjectException {
		final Sample s = (Sample) super.byId(typeName, 1);
		assertEquals(1, s.getId());
		assertEquals("testing sample", s.getNumber());
		assertEquals("Frank Spear", s.getCollector());
		assertEquals("Amphibolite", s.getRockType().getRockType());
	}
	
	@Test
	public void addRegion() throws NoSuchObjectException, MpDbException {
		final Sample s = (Sample) super.byId(typeName, 1);
		s.addRegion("Anthony's place");
		new SampleDAO(InitDatabase.getSession()).save(s);
		//load and verify
		final Sample loaded = (Sample) super.byId(typeName, 1);
		Verify.verifyEqual(s, loaded);
	}
	
}

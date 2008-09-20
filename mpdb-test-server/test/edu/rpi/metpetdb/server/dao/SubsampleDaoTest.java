package edu.rpi.metpetdb.server.dao;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.server.DatabaseTestCase;

public class SubsampleDaoTest extends DatabaseTestCase {

	private final static String typeName = "Subsample";

	public SubsampleDaoTest() {
		super("test-data/test-sample-data.xml");
	}
	
	@Test
	public void testSubsampleSampleName() throws NoSuchObjectException {
		final Subsample s = (Subsample) super.byId(typeName, 5);
		assertEquals("1", s.getSampleName());
	}
	
	@Test
	public void testSubsampleImageCount() throws NoSuchObjectException {
		final Subsample s = (Subsample) super.byId(typeName, 5);
		assertEquals(0, s.getImageCount());
	}
	
	@Test
	public void testSubsampleAnalysisCount() throws NoSuchObjectException {
		final Subsample s = (Subsample) super.byId(typeName, 5);
		assertEquals(1, s.getAnalysisCount());
	}
}
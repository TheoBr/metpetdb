package edu.rpi.metpetdb.server.dao;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Sample;

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
	public void testSampleById() throws NoSuchObjectException {
		final Sample s = (Sample) super.byId(typeName, 1);
		assertEquals(1, s.getId());
	}

	/**
	 * Test that loading an object fials with an incorrect id, this case -1
	 */
	@Test
	public void testSampleByIdFail() {
		try {
			super.byId(typeName, -1);
			fail("No Exception thrown");
		} catch (NoSuchObjectException nsoe) {

		} catch (Exception e) {
			fail("Wrong exception caught\n" + e.getMessage());
		}
	}

	/**
	 * Test the hql query for the size of all of the samples
	 */
	@Test
	public void testSampleAllSize() {
		final Query q = InitDatabase.getSession().getNamedQuery(
				"Sample.all,size");
		assertEquals(9, ((Number) q.uniqueResult()).intValue());
	}

	/**
	 * Test that loading all samples returns the correct amount and is correctly
	 * sorted
	 */
	@Test
	public void testSampleAllSesarNumberAsc() {
		final List<Sample> l = pageQuery(typeName, "sesarNumber", true, 0, 5);
		// Verify the size
		assertEquals(5, l.size());
		// Now verify the correct order
		for (int i = 0; i < l.size() - 1; ++i) {
			final Sample first = l.get(i);
			final Sample second = l.get(i + 1);
			assertTrue(first.getSesarNumber()
					.compareTo(second.getSesarNumber()) <= 0);
		}
	}
}

package edu.rpi.metpetdb.server.dao.sample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

/**
 * Tests things relating to a sample, for example that the formulas return the
 * correct amounts
 * 
 * @author anthony
 * 
 */
public class SampleDaoTest extends DatabaseTestCase {

	private final static String typeName = "Sample";

	public SampleDaoTest() {
		super("test-data/test-sample-data.xml");
	}
	@Test
	public void imageCount() throws NoSuchObjectException {
		final Sample s = super.byId(typeName, PUBLIC_SAMPLE);
		assertEquals(2, s.getImageCount());
	}

	/**
	 * Tests when loading a public sample it only loads public subsamples/images
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void loadRelatedObjects() throws NoSuchObjectException {
		final Sample s = super.byId(typeName, PUBLIC_SAMPLE);
		assertEquals(1, s.getSubsamples().size());
		assertEquals(1, s.getImages().size());
	}

	/**
	 * Tests loading public sample that has private data of a user
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void privateLoadRelatedObjects() throws NoSuchObjectException {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		session.enableFilter("samplePublicOrUser").setParameter("userId", 1);
		final Sample s = super.byId(typeName, PUBLIC_SAMPLE);
		assertEquals(2, s.getSubsamples().size());
		assertEquals(2, s.getImages().size());
	}

	@Test
	public void loadSample() throws NoSuchObjectException {
		final Sample s = super.byId("Sample", PUBLIC_SAMPLE);
		assertEquals(PUBLIC_SAMPLE, s.getId());
	}
	
	@Test
	public void deleteSample() throws NoSuchObjectException, MpDbException {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		session.enableFilter("samplePublicOrUser").setParameter("userId", 1);
		final Sample s = super.byId("Sample", 1);
		new SampleDAO(session).delete(s);
	}

}

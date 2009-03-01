package edu.rpi.metpetdb.server.dao.subsample;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

/**
 * Tests a subsamples formulas and that it fetches only public data or data
 * owned by the current user
 * 
 * @author anthony
 * 
 */
public class SubsampleDaoTest extends DatabaseTestCase {

	private final static String typeName = "Subsample";

	public SubsampleDaoTest() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Tests loading the sample name, and that it correctly loads public
	 * images/analyses
	 * 
	 * @throws Throwable
	 */
	@Test
	public void sampleName() throws Throwable {
		session.enableFilter("subsamplePublicOrUser").setParameter(
				"userId", 0l);
		final Subsample s = super.byId("Subsample", (int) PUBLIC_SUBSAMPLE);
		assertEquals("testing public", s.getSampleName());
		assertEquals(1, s.getImages().size());
		assertEquals(1, s.getChemicalAnalyses().size());
	}

	/**
	 * Tests the formula query for the number of images, since there is no
	 * logged in user it should only be 1 (for the 1 public image)
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void imageCount() throws NoSuchObjectException {
		final Subsample s = (Subsample) super.byId(typeName,
				(int) PUBLIC_SUBSAMPLE);
		assertEquals(1, s.getImageCount());
	}

	/**
	 * Since the user is logged in it should return their private images as well
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void privateImageCount() throws NoSuchObjectException {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		final Subsample s = (Subsample) super.byId(typeName,
				(int) PUBLIC_SUBSAMPLE);
		assertEquals(2, s.getImageCount());
	}

	@Test
	public void analysisCount() throws NoSuchObjectException {
		final Subsample s = (Subsample) super.byId(typeName,
				(int) PUBLIC_SUBSAMPLE);
		assertEquals(2, s.getAnalysisCount());
	}
	
	/**
	 * Tests when loading a public subsample it only loads public images/analyses
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void loadRelatedObjects() throws NoSuchObjectException  {
		session.enableFilter("subsamplePublicOrUser").setParameter("userId", 0l);
		final Subsample s = (Subsample) super.byId(typeName,
				(int) PUBLIC_SUBSAMPLE);
		assertEquals(1, s.getImages().size());
		assertEquals(1, s.getChemicalAnalyses().size());
	}
	
	/**
	 * Tests when loading a public subsample it loads private images/analyses
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void loadPrivateRelatedObjects() throws NoSuchObjectException  {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		session.enableFilter("subsamplePublicOrUser").setParameter("userId", 1l);
		final Subsample s = (Subsample) super.byId(typeName,
				(int) PUBLIC_SUBSAMPLE);
		assertEquals(2, s.getImages().size());
		assertEquals(3, s.getChemicalAnalyses().size());
	}
}

package edu.rpi.metpetdb.server.dao.permissions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.security.Principal;
import java.util.ArrayList;

import org.hibernate.CallbackException;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleCommentDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class ContributorSavingPermissionsTest extends DatabaseTestCase {

	public ContributorSavingPermissionsTest() {
		super("test-data/test-sample-data.xml");
	}

	@Before
	public void setUp() {
		super.setUp();
		// setup a request from a contributor
		final MpDbServlet.Req req = new MpDbServlet.Req();
		try {
			req.user = super.byId("User", 1);
		} catch (NoSuchObjectException e) {
			req.user = null;
		}
		req.principals = new ArrayList<Principal>();
		req.principals.add(new OwnerPrincipal(req.user));
		MpDbServlet.testReq = req;
		setupSession(1);
	}

	/**
	 * Tests saving a new private sample
	 * 
	 * @throws MpDbException
	 */
	@Test
	public void savePrivateSample() throws MpDbException {
		final Sample s = new Sample();
		final RockType rt = new RockType();
		rt.setRockType("Slate");
		s.setOwner(MpDbServlet.currentReq().user);
		s.setNumber("Bill");
		s.setLongitude(32d);
		s.setLatitude(12d);
		s.setRockType(rt);
		s.setPublicData(false);
		final Sample saved = new SampleDAO(session).save(s);
		assertFalse(saved.mIsNew());
	}

	/**
	 * Tests trying to save a public sample
	 * 
	 * @throws Throwable
	 */
	@Test(expected = UnableToModifyPublicDataException.class)
	public void savePublicSample() throws Throwable {
		try {
			final Sample s = super.byId("Sample", 6);
			s.setPublicData(false);
			new SampleDAO(session).save(s);
		} catch (CallbackException e) {
			session.clear();
			throw e.getCause();
		}
	}

	/**
	 * Test saving a comment to a public sample
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test
	public void savePublicSampleComment() throws NoSuchObjectException,
			MpDbException {
		final SampleComment sc = new SampleComment();
		final Sample sample = super.byId("Sample", 6);
		sc.setSample(sample);
		sc.setOwner(MpDbServlet.testReq.user);
		sc.setText("Sample comment");
		sample.getComments().add(sc);
		final SampleComment saved = new SampleCommentDAO(session).save(sc);
		final SampleComment loaded = super.byId("SampleComment", (int) saved
				.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("Sample comment", loaded.getText());
	}

	/**
	 * Tries to add a comment to a private sample, it should fail because the
	 * sample is private
	 * 
	 * @throws Throwable
	 */
	@Test(expected = NotOwnerException.class)
	public void saveOthersPrivateSampleComment() throws Throwable {
		final Sample sample = new Sample();
		sample.setId(2);
		final SampleComment sc = new SampleComment();
		sc.setSample(sample);
		sc.setOwner(MpDbServlet.testReq.user);
		sc.setText("comment samples");
		try {
			new SampleCommentDAO(session).save(sc);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}

	/**
	 * Saves a sampe comment to a user's own private sample
	 * 
	 * @throws Throwable
	 */
	@Test
	public void savePrivateSampleComment() throws Throwable {
		final Sample sample = new Sample();
		sample.setId(1);
		final SampleComment sc = new SampleComment();
		sc.setSample(sample);
		sc.setOwner(MpDbServlet.testReq.user);
		sc.setText("comment samples");
		final SampleComment saved = new SampleCommentDAO(session).save(sc);
		final SampleComment loaded = super.byId("SampleComment", (int) saved
				.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("comment samples", loaded.getText());
	}

	/**
	 * Tests saving a chemical analysis to the user's own private sample
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test
	public void saveChemicalAnalysisToPrivateSubsample()
			throws NoSuchObjectException, MpDbException {
		final Subsample subsample = super.byId("Subsample", 1);
		final ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setSubsample(subsample);
		ca.setSpotId("my ca");
		ca.setOwner(MpDbServlet.testReq.user);
		ca.setLargeRock(false);
		final ChemicalAnalysis saved = new ChemicalAnalysisDAO(session)
				.save(ca);
		final ChemicalAnalysis loaded = super.byId("ChemicalAnalysis",
				(int) saved.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("my ca", loaded.getSpotId());
	}

	/**
	 * Tests adding a chemical analysis to a public sample (with a different
	 * owner)
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test
	public void saveChemicalAnalysisToPublicSubsample()
			throws NoSuchObjectException, MpDbException {
		final Subsample subsample = super.byId("Subsample",
				(int) PUBLIC_SUBSAMPLE);
		final ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setSubsample(subsample);
		ca.setSpotId("my ca");
		ca.setOwner(MpDbServlet.testReq.user);
		ca.setLargeRock(false);
		final ChemicalAnalysis saved = new ChemicalAnalysisDAO(session)
				.save(ca);
		final ChemicalAnalysis loaded = super.byId("ChemicalAnalysis",
				(int) saved.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("my ca", loaded.getSpotId());
	}

	/**
	 * Tries to add a chemical analysis to another user's private sample, should
	 * fail because this is not allowed
	 * 
	 * @throws Throwable
	 */
	@Test(expected = NotOwnerException.class)
	public void saveChemicalAnalysisToOthersPrivateSubsample() throws Throwable {
		final Subsample subsample = new Subsample();
		subsample.setId(6);
		final ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setSubsample(subsample);
		ca.setSpotId("my ca");
		ca.setOwner(MpDbServlet.testReq.user);
		ca.setLargeRock(false);
		try {
			new ChemicalAnalysisDAO(session).save(ca);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}

	/**
	 * Tests saving a subsample to the user's own private sample
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test
	public void saveSubsampleToPrivateSample() throws NoSuchObjectException,
			MpDbException {
		final Sample sample = super.byId("Sample", 1);
		final Subsample subsample = new Subsample();
		subsample.setSubsampleType((SubsampleType) super.byId("SubsampleType",
				1));
		subsample.setOwner(MpDbServlet.testReq.user);
		subsample.setName("my subsample");
		subsample.setSample(sample);
		final Subsample saved = new SubsampleDAO(session).save(subsample);
		final Subsample loaded = super.byId("Subsample", (int) saved.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("my subsample", loaded.getName());
	}

	/**
	 * Tests adding a subsample to a public sample (with a different owner)
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test
	public void saveSubsampleToPublicSample() throws NoSuchObjectException,
			MpDbException {
		final Sample sample = super.byId("Sample", 7);
		final Subsample subsample = new Subsample();
		subsample.setSubsampleType((SubsampleType) super.byId("SubsampleType",
				1));
		subsample.setOwner(MpDbServlet.testReq.user);
		subsample.setName("my subsample");
		subsample.setSample(sample);
		final Subsample saved = new SubsampleDAO(session).save(subsample);
		final Subsample loaded = super.byId("Subsample", (int) saved.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("my subsample", loaded.getName());
	}

	/**
	 * Tries to add a subsample to another user's private sample, should fail
	 * because this is not allowed
	 * 
	 * @throws Throwable
	 */
	@Test(expected = NotOwnerException.class)
	public void saveSubsampleToOthersPrivateSample() throws Throwable {
		final Sample sample = new Sample();
		sample.setId(2);
		final Subsample subsample = new Subsample();
		subsample.setSubsampleType((SubsampleType) super.byId("SubsampleType",
				1));
		subsample.setOwner(MpDbServlet.testReq.user);
		subsample.setName("my subsample");
		subsample.setSample(sample);
		try {
			new SubsampleDAO(session).save(subsample);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}
}

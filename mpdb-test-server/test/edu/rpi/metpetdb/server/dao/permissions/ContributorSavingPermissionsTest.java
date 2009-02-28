package edu.rpi.metpetdb.server.dao.permissions;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.hibernate.CallbackException;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.MpDbServlet;
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
		rt.setRockType("Schist");
		s.setOwner(MpDbServlet.currentReq().user);
		s.setNumber("Bill");
		s.setLongitude(32d);
		s.setLatitude(12d);
		s.setRockType(rt);
		s.setPublicData(false);
		InitDatabase.getSession().beginTransaction();
		final Sample saved = new SampleDAO(InitDatabase.getSession()).save(s);
		InitDatabase.getSession().getTransaction().commit();
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
			InitDatabase.getSession().beginTransaction();
			new SampleDAO(InitDatabase.getSession()).save(s);
			InitDatabase.getSession().getTransaction().commit();
		} catch (CallbackException e) {
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
		InitDatabase.getSession().beginTransaction();
		final SampleComment saved = new SampleCommentDAO(InitDatabase
				.getSession()).save(sc);
		InitDatabase.getSession().getTransaction().commit();
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
			new SampleCommentDAO(InitDatabase.getSession()).save(sc);
		} catch (CallbackException ce) {
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
		InitDatabase.getSession().beginTransaction();
		final SampleComment saved = new SampleCommentDAO(InitDatabase
				.getSession()).save(sc);
		InitDatabase.getSession().getTransaction().commit();
		final SampleComment loaded = super.byId("SampleComment", (int) saved
				.getId());
		assertEquals(loaded.getId(), saved.getId());
		assertEquals("comment samples", loaded.getText());
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
		InitDatabase.getSession().beginTransaction();
		final Subsample saved = new SubsampleDAO(InitDatabase.getSession())
				.save(subsample);
		InitDatabase.getSession().getTransaction().commit();
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
		InitDatabase.getSession().beginTransaction();
		final Subsample saved = new SubsampleDAO(InitDatabase.getSession())
				.save(subsample);
		InitDatabase.getSession().getTransaction().commit();
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
			new SubsampleDAO(InitDatabase.getSession()).save(subsample);
		} catch (CallbackException ce) {
			throw ce.getCause();
		}
	}

}

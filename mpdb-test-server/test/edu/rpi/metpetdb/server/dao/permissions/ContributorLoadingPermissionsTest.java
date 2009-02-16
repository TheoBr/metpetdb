 package edu.rpi.metpetdb.server.dao.permissions;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.hibernate.CallbackException;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.NotOwnerException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.permissions.principals.EnabledPrincipal;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class ContributorLoadingPermissionsTest extends DatabaseTestCase {

	public ContributorLoadingPermissionsTest() {
		super("test-data/test-sample-data.xml");
	}

	@Before
	public void setUp() {
		super.setUp();
		// setup a  request from a contributor
		final MpDbServlet.Req req = new MpDbServlet.Req();
		try {
			req.user = super.byId("User", 1);
		} catch (NoSuchObjectException e) {
			req.user = null;
		}
		req.principals = new ArrayList<Principal>();
		req.principals.add(new OwnerPrincipal(req.user));
		req.principals.add(new EnabledPrincipal(req.user));
		MpDbServlet.testReq = req;
	}
	
	/**
	 * Tests loading own sample
	 * @throws Throwable
	 */
	@Test
	public void loadPrivateSample() throws Throwable {
		try {
			final Sample s = super.byId("Sample", 1);
			assertEquals(1, s.getOwner().getId());
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}
	
	/**
	 * Tests loading own subsample
	 * @throws Throwable
	 */
	@Test
	public void loadPrivateSubsample() throws Throwable {
		try {
			final Subsample s = super.byId("Subsample", 1);
			assertEquals(1, s.getOwner().getId());
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}
	
	/**
	 * Tests loading own chemical analysis
	 * @throws Throwable
	 */
	@Test
	public void loadPrivateChemicalAnalysis() throws Throwable {
		try {
			final ChemicalAnalysis ca = super.byId("ChemicalAnalysis", 1);
			assertEquals(1, ca.getOwner().getId());
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}
	
	/**
	 * Tests loading our own image
	 * @throws Throwable
	 */
	@Test
	public void loadPrivateImage() throws Throwable {
		try {
			final Image ca = super.byId("Image", 1);
			assertEquals(1, ca.getOwner().getId());
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}
	
	/**
	 * Tests loading the private sample of another user
	 * @throws Throwable
	 */
	@Test(expected = NotOwnerException.class)
	public void loadOthersPrivateSample() throws Throwable {
		try {
			super.byId("Sample", 2);
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}
	
	/**
	 * Tries loading a private image of another user
	 * @throws Throwable
	 */
	@Test(expected = NotOwnerException.class)
	public void loadOthersPrivateImage() throws Throwable {
		try {
			super.byId("Image", 3);
		} catch (CallbackException c) {
			throw c.getCause();
		}
	}

}

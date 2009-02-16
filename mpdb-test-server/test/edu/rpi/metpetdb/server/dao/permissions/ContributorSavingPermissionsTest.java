package edu.rpi.metpetdb.server.dao.permissions;

import java.security.Principal;
import java.util.ArrayList;

import static org.junit.Assert.assertFalse;

import org.hibernate.CallbackException;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.UnableToModifyPublicDataException;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
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
		s.setAlias("Bill");
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

}

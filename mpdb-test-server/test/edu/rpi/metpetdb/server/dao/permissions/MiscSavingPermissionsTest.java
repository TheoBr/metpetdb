package edu.rpi.metpetdb.server.dao.permissions;

import static org.junit.Assert.assertEquals;

import java.security.Principal;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

/**
 * Tests saving misc things, for example when registering
 * 
 * @author anthony
 * 
 */
public class MiscSavingPermissionsTest extends DatabaseTestCase {

	public MiscSavingPermissionsTest() {
		super("test-data/test-sample-data.xml");
	}

	@Before
	public void setUp() {
		super.setUp();
		// setup an anonymous request
		final MpDbServlet.Req req = new MpDbServlet.Req();
		req.user = null;
		req.principals = new ArrayList<Principal>();
		MpDbServlet.testReq = req;
	}

	/**
	 * Simulates registering a new user through the interface
	 * @throws MpDbException
	 * @throws NoSuchObjectException
	 */
	@Test
	public void registerNewUser() throws MpDbException, NoSuchObjectException {
		final User u = new User();
		u.setRole((Role) super.byId("Role", 1));
		u.setEnabled(false);
		u.setName("tester");
		u.setEmailAddress("tester@test.com");
		u.setEncryptedPassword(PasswordEncrypter.crypt("asdf"));
		final User saved = new UserDAO(session).save(u);
		// load it back
		final User loaded = super.byId("User", saved.getId());
		assertEquals(loaded.getEmailAddress(), saved.getEmailAddress());
		assertEquals(loaded.getId(), saved.getId());
	}

	/**
	 * Tests loaded a disabled users and then trying to enable their account,
	 * simulates entering a confirmation code
	 * 
	 * @throws MpDbException
	 * @throws NoSuchObjectException
	 */
	@Test
	public void confirmAccount() throws MpDbException, NoSuchObjectException {
		final User u = super.byId("User", 8);
		MpDbServlet.testReq.user = u;
		u.setEnabled(true);
		final User saved = new UserDAO(session).save(u);
		// load it back
		final User loaded = super.byId("User", saved.getId());
		assertEquals(loaded.getEmailAddress(), saved.getEmailAddress());
		assertEquals(loaded.getId(), saved.getId());
	}
}

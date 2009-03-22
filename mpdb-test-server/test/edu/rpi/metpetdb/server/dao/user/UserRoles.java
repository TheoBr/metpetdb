package edu.rpi.metpetdb.server.dao.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.security.CannotCreateRoleChangeException;
import edu.rpi.metpetdb.client.error.security.CannotLoadRoleChangeException;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.RoleChangeDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class UserRoles extends DatabaseTestCase {

	public UserRoles() {
		super("test-data/test-sample-data.xml");
	}

	@Test
	public void fetchEligableSponsors0() throws MpDbException {
		final Role rank0 = new Role();
		rank0.setRank(0);
		final UserDAO ud = new UserDAO(session);
		final Collection<User> sponsors = ud.getEligableSponsors(rank0);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank0.getRank());
		}
		assertEquals(9, sponsors.size());
	}

	@Test
	public void fetchEligableSponsors1() throws MpDbException {
		final Role rank1 = new Role();
		rank1.setRank(1);
		final UserDAO ud = new UserDAO(session);
		final Collection<User> sponsors = ud.getEligableSponsors(rank1);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank1.getRank());
		}
		assertEquals(4, sponsors.size());
	}

	@Test
	public void fetchEligableSponsors2() throws MpDbException {
		final Role rank2 = new Role();
		rank2.setRank(2);
		final UserDAO ud = new UserDAO(session);
		final Collection<User> sponsors = ud.getEligableSponsors(rank2);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank2.getRank());
		}
		assertEquals(1, sponsors.size());
	}

	/**
	 * Tests a role change from a member to a contributor
	 * 
	 * @throws NoSuchObjectException
	 * @throws HibernateException
	 * @throws MpDbException
	 */
	@Test
	public void roleChangeFromMemberToContributor()
			throws NoSuchObjectException, HibernateException, MpDbException {
		MpDbServlet.currentReq().user = super.byId("User", 2);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		final User contributor = byId("User", 1);
		final User member = byId("User", 2);
		final Role contributorRole = byId("Role", 2);
		RoleChange rc = new RoleChange();
		rc.setUser(member);
		rc.setSponsor(contributor);
		rc.setRole(contributorRole);
		rc = new RoleChangeDAO(session).save(rc);
		// load it back
		final RoleChange loaded = byId("RoleChange", rc.getId());
		assertEquals(rc.getId(), loaded.getId());
	}

	/**
	 * Test creating another user's role change, this should fail
	 * 
	 * @throws Throwable
	 */
	@Test(expected = CannotCreateRoleChangeException.class)
	public void createOtherUsersRoleChange() throws Throwable {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		final User contributor = byId("User", 1);
		final User member = byId("User", 2);
		final Role contributorRole = byId("Role", 2);
		RoleChange rc = new RoleChange();
		rc.setUser(member);
		rc.setSponsor(contributor);
		rc.setRole(contributorRole);
		try {
			rc = new RoleChangeDAO(session).save(rc);
		} catch (CallbackException ce) {
			session.clear();
			throw ce.getCause();
		}
	}

	/**
	 * Tries to load the role changes for another user (fails because it is not
	 * our role change and we are not the sponsor)
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test(expected = CannotLoadRoleChangeException.class)
	public void getRoleChangeForOtherUser() throws NoSuchObjectException,
			MpDbException {
		final User u = byId("User", 3);
		final RoleChangeDAO rcd = new RoleChangeDAO(session);
		rcd.forUser(u);
	}

	@Test
	public void getRoleChangeForUser() throws NoSuchObjectException,
			MpDbException {
		MpDbServlet.currentReq().user = super.byId("User", 3);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		final User u = byId("User", 3);
		final RoleChangeDAO rcd = new RoleChangeDAO(session);
		final RoleChange rc = rcd.forUser(u);
		assertEquals(3, rc.getUser().getId());
		assertEquals(2, rc.getRole().getId());
	}

	/**
	 * Tests loading the pending role changes for a sponsor, fails because the
	 * current user is not the sponsor
	 * 
	 * @throws NoSuchObjectException
	 * @throws MpDbException
	 */
	@Test(expected = CannotLoadRoleChangeException.class)
	public void getRoleChangeForOtherSponsor() throws NoSuchObjectException,
			MpDbException {
		final User s = byId("User", 1);
		final RoleChangeDAO prd = new RoleChangeDAO(session);
		prd.forSponsor(s);
	}

	@Test
	public void getRoleChangeForSponsor() throws NoSuchObjectException,
			MpDbException {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		final User s = byId("User", 1);
		final RoleChangeDAO prd = new RoleChangeDAO(session);
		final Collection<RoleChange> prs = prd.forSponsor(s);
		for (RoleChange pr : prs) {
			assertEquals(2, pr.getRole().getId());
			assertEquals(s.getId(), pr.getSponsor().getId());
		}
		assertEquals(2, prs.size());
	}
}

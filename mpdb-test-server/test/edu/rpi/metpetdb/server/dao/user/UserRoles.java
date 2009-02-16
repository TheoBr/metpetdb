package edu.rpi.metpetdb.server.dao.user;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hibernate.HibernateException;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.PendingRole;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.dao.impl.PendingRoleDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;

public class UserRoles extends DatabaseTestCase {

	public UserRoles() {
		super("test-data/test-user-data.xml");
	}

	public void testEligableSponsors() throws MpDbException {
		final Role rank1 = new Role();
		rank1.setRank(0);
		final UserDAO ud = new UserDAO(InitDatabase.getSession());
		final Collection<User> sponsors = ud.getEligableSponsors(rank1);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank1.getRank());
		}
		assertEquals(8, sponsors.size());
	}

	public void testEligableSponsors2() throws MpDbException {
		final Role rank1 = new Role();
		rank1.setRank(1);
		final UserDAO ud = new UserDAO(InitDatabase.getSession());
		final Collection<User> sponsors = ud.getEligableSponsors(rank1);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank1.getRank());
		}
		assertEquals(3, sponsors.size());
	}

	public void testEligableSponsors3() throws MpDbException {
		final Role rank1 = new Role();
		rank1.setRank(2);
		final UserDAO ud = new UserDAO(InitDatabase.getSession());
		final Collection<User> sponsors = ud.getEligableSponsors(rank1);
		for (User s : sponsors) {
			assertTrue(s.getRole().getRank() >= rank1.getRank());
		}
		assertEquals(1, sponsors.size());
	}

	public void testPendingRole() throws NoSuchObjectException, HibernateException, MpDbException {
		final User u = byId("User", 26);
		final User s = byId("User", 25);
		final Role r = byId("Role", 2);
		PendingRole pr = new PendingRole();
		pr.setUser(u);
		pr.setSponsor(s);
		pr.setRole(r);
		InitDatabase.getSession().beginTransaction();
		pr = new PendingRoleDAO(InitDatabase.getSession()).save(pr);
		InitDatabase.getSession().getTransaction().commit();
		// load it back
		final PendingRole loaded = byId("PendingRole", pr.getId());
		assertEquals(pr.getId(), loaded.getId());
	}

	public void testGetPendingRolesForUser() throws NoSuchObjectException, MpDbException {
		final User u = byId("User", 27);
		final PendingRoleDAO prd = new PendingRoleDAO(InitDatabase.getSession());
		final PendingRole pr = prd.forUser(u);
		assertEquals(27, pr.getUser().getId());
		assertEquals(2, pr.getRole().getId());
	}

	public void testGetPendingRolesForSponsor() throws NoSuchObjectException, MpDbException {
		final User s = byId("User", 25);
		final PendingRoleDAO prd = new PendingRoleDAO(InitDatabase.getSession());
		final Collection<PendingRole> prs = prd.forSponsor(s);
		for (PendingRole pr : prs) {
			assertEquals(2, pr.getRole().getId());
			assertEquals(s.getId(), pr.getSponsor().getId());
		}
		assertEquals(2, prs.size());
	}
}

package edu.rpi.metpetdb.server.dao.user;
import static org.junit.Assert.*;
import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;


public class UserDaoTest extends DatabaseTestCase {
	
	public UserDaoTest() {
		super("test-data/test-sample-data.xml");
	}
	
	@Test
	public void testUserById() throws NoSuchObjectException {
		final User u = (User) super.byId("User", 1);
		assertEquals(1,u.getId());
	}

	@Test
	public void testUserByUsername() {
		final Query q = session.getNamedQuery("User.byEmailAddress");
		q.setString("emailAddress", "watera2@cs.rpi.edu");
		final User u = (User) q.uniqueResult();
		assertEquals("watera2@cs.rpi.edu",u.getEmailAddress());
	}
	

}

package edu.rpi.metpetdb.server.dao.user;
import static org.junit.Assert.*;
import org.hibernate.Query;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;


public class UserDaoTest extends DatabaseTestCase {
	
	public UserDaoTest() {
		super("test-data/test-sample-data.xml");
	}
	
	@Test
	public void testUserById() {
		final Query q = InitDatabase.getSession().getNamedQuery("User.byId");
		q.setLong("id", 1);
		final User u = (User) q.uniqueResult();
		assertEquals(1,u.getId());
	}

	@Test
	public void testUserByUsername() {
		final Query q = InitDatabase.getSession().getNamedQuery("User.byUsername");
		q.setString("username", "anthony");
		final User u = (User) q.uniqueResult();
		assertEquals("anthony",u.getEmailAddress());
	}
	

}

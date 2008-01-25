package edu.rpi.metpetdb.server;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.server.dao.SampleDaoTest;
import edu.rpi.metpetdb.server.dao.UserDaoTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server");
		//
		final InitDatabase initDb = new InitDatabase();
		//Backup database and clear it
		try {
			initDb.setUp();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		//$JUnit-BEGIN$
		suite.addTestSuite(SampleDaoTest.class);
		suite.addTestSuite(UserDaoTest.class);
		//$JUnit-END$
		//Clear database and restore it
		try {
			initDb.tearDown();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		return suite;
	}

}

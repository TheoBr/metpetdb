package edu.rpi.metpetdb.server.test.database;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDatabaseTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.rpi.metpetdb.server.test.database");
		//$JUnit-BEGIN$
		suite.addTestSuite(HibernateTest.class);
		//$JUnit-END$
		return suite;
	}

}

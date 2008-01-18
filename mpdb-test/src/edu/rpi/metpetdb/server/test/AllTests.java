package edu.rpi.metpetdb.server.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.server.test.beans.SampleTest;
import edu.rpi.metpetdb.server.test.database.HibernateTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(SampleTest.class);
		suite.addTestSuite(HibernateTest.class);
		//$JUnit-END$
		return suite;
	}

}

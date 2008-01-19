package edu.rpi.metpetdb.server.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.server.test.beans.AllBeanTest;
import edu.rpi.metpetdb.server.test.database.AllDatabaseTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server.test");
		//$JUnit-BEGIN$
		suite.addTest(AllBeanTest.suite());
		suite.addTest(AllDatabaseTest.suite());
		//$JUnit-END$
		return suite;
	}

}

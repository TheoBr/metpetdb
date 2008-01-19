package edu.rpi.metpetdb;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.server.AllImplTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server.test");
		//$JUnit-BEGIN$
		suite.addTest(AllImplTest.suite());
		//$JUnit-END$
		return suite;
	}

}

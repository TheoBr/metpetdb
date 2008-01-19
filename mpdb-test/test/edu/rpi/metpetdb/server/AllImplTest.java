package edu.rpi.metpetdb.server;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllImplTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.rpi.metpetdb.server.test.beans");
		//$JUnit-BEGIN$
		suite.addTestSuite(SampleServiceImplTest.class);
		//$JUnit-END$
		return suite;
	}

}

package edu.rpi.metpetdb;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.client.ui.object.details.SampleDetailsTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(SampleDetailsTest.class);
		//$JUnit-END$
		return suite;
	}

}

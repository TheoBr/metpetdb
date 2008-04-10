package edu.rpi.metpetdb.client;

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.rpi.metpetdb.client.ui.sample.LoadSampleTest;
import edu.rpi.metpetdb.client.ui.sample.SaveSampleTest;

public class AllTest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for edu.rpi.metpetdb.server.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(LoadSampleTest.class);
		suite.addTestSuite(SaveSampleTest.class);
		// $JUnit-END$
		return suite;
	}

}

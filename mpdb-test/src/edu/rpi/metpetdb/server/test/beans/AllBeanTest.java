package edu.rpi.metpetdb.server.test.beans;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllBeanTest {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.rpi.metpetdb.server.test.beans");
		//$JUnit-BEGIN$
		suite.addTestSuite(SampleTest.class);
		//$JUnit-END$
		return suite;
	}

}

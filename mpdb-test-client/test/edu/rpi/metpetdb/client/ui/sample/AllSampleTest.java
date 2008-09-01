package edu.rpi.metpetdb.client.ui.sample;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class AllSampleTest extends GWTTestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for edu.rpi.metpetdb.client.ui.sample");
		// $JUnit-BEGIN$
		suite.addTestSuite(LoadSampleTest.class);
		suite.addTestSuite(SaveSampleTest.class);
		// $JUnit-END$
		return suite;
	}

}

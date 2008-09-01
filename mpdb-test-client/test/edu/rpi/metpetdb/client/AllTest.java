package edu.rpi.metpetdb.client;

import com.google.gwt.junit.tools.GWTTestSuite;

import edu.rpi.metpetdb.client.ui.sample.LoadSampleTest;
import edu.rpi.metpetdb.client.ui.sample.SaveSampleTest;

public class AllTest extends GWTTestSuite {

	public AllTest() {
		super("Test for edu.rpi.metpetdb cient");
		addTestSuite(LoadSampleTest.class);
		addTestSuite(SaveSampleTest.class);
	}

}

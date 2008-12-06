package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.DataStore;
import junit.framework.TestCase;

public class ConstraintsTest extends TestCase {
	
	public void testMakeConstraints() {
		DataStore.getInstance().getDatabaseObjectConstraints();
	}

}

package edu.rpi.metpetdb.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.rpi.metpetdb.server.dao.sample.SampleDaoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	SampleDaoTest.class,
})
public class AllTests {
	// the class remains completely empty,
	// being used only as a holder for the above annotations
}

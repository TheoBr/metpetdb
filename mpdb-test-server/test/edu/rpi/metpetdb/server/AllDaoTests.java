package edu.rpi.metpetdb.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import edu.rpi.metpetdb.server.dao.chemical.analysis.ChemicalAnalysisDaoTest;
import edu.rpi.metpetdb.server.dao.element.ElementDaoTest;
import edu.rpi.metpetdb.server.dao.mineral.MineralDaoTest;
import edu.rpi.metpetdb.server.dao.oxide.OxideDaoTest;
import edu.rpi.metpetdb.server.dao.sample.SampleDaoTest;
import edu.rpi.metpetdb.server.dao.subsample.SubsampleDaoTest;
import edu.rpi.metpetdb.server.dao.user.UserDaoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	SampleDaoTest.class,
	ChemicalAnalysisDaoTest.class,
	ElementDaoTest.class,
	MineralDaoTest.class,
	OxideDaoTest.class,
	SubsampleDaoTest.class,
	UserDaoTest.class
	
})
public class AllDaoTests {
	// the class remains completely empty,
	// being used only as a holder for the above annotations
}

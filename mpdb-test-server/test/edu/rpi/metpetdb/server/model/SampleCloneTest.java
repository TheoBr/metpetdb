package edu.rpi.metpetdb.server.model;
import static org.junit.Assert.*;
import net.sf.hibernate4gwt.core.HibernateBeanManager;
import net.sf.hibernate4gwt.core.beanlib.mapper.DirectoryClassMapper;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;

public class SampleCloneTest extends DatabaseTestCase {

	private final static String typeName = "Sample";

	public SampleCloneTest() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Test loading a sample by its id, a valid id should be given
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void testCloneSample() throws Exception {
		final Sample sample = (Sample) super.byId(typeName, 1);
		HibernateBeanManager hbm = new HibernateBeanManager();
		hbm.setSessionFactory(InitDatabase.getSession().getSessionFactory());
		DirectoryClassMapper cloneMapper = new DirectoryClassMapper();
		cloneMapper.setRootDomainPackage("edu.rpi.metpetdb.server.model");
		cloneMapper.setRootClonePackage("edu.rpi.metpetdb.client.model");
		cloneMapper.setCloneSuffix("DTO");
		hbm.setClassMapper(cloneMapper);

		final Sample sample2 = (Sample) hbm.clone(sample);
		assertEquals(sample2.getLocation(), sample.getLocation());
	}

}

package edu.rpi.metpetrest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import edu.rpi.metpetrest.dao.SampleDAOImpl;
import edu.rpi.metpetrest.model.SampleData;

@ContextConfiguration(locations="../../../test-services-servlet.xml")
public class SampleDAOTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Test
	public void testQuery() {

		SampleDAOImpl sampleDAO = (SampleDAOImpl) applicationContext
				.getBean("sampleDAO");

		Assert.assertTrue(sampleDAO
				.findPublicChemicalAnalysisIdsOwnedByPublication().size() > 0);

	}
	
	@Test
	public void testQuery2()
	{
		SampleDAOImpl sampleDAO = (SampleDAOImpl) applicationContext
		.getBean("sampleDAO");
		
		List<SampleData> sampleData = sampleDAO.getSamplesForPublication("150");
		
		Assert.assertTrue(sampleData.size() > 0);
		
	
	}
}

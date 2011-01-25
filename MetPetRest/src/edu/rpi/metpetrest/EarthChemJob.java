package edu.rpi.metpetrest;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import edu.rpi.metpetrest.dao.SampleDAOImpl;

public class EarthChemJob extends QuartzJobBean {

	
	private SampleDAOImpl sampleDAO = null;
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		sampleDAO.getSamples();
		
	}

	public SampleDAOImpl getSampleDAO() {
		return sampleDAO;
	}

	public void setSampleDAO(SampleDAOImpl sampleDAO) {
		this.sampleDAO = sampleDAO;
	}

}

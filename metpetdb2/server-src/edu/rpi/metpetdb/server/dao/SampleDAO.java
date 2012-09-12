package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.model.Sample;

public interface SampleDAO {

	public void saveSample(Sample sample);
	
	public Sample loadSample(Long id);
	
}

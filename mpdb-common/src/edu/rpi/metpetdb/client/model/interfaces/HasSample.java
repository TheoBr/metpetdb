package edu.rpi.metpetdb.client.model.interfaces;

import edu.rpi.metpetdb.client.model.Sample;

public interface HasSample {
	
	public Sample getSample();
	
	public void setSample(final Sample sample);

}

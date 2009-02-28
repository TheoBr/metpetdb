package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;

public class SampleComment extends Comment implements HasSample, HasOwner {

	private static final long serialVersionUID = 1L;
	private Sample sample;
	private User owner;
	
	public SampleComment() {
		
	}
	
	public SampleComment(final String text) {
		setText(text);
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(final User u) {
		owner = u;
	}
}

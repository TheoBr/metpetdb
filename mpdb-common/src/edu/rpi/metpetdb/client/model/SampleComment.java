package edu.rpi.metpetdb.client.model;

public class SampleComment extends Comment {

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

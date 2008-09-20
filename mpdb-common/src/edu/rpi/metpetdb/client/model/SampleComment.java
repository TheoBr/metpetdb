package edu.rpi.metpetdb.client.model;

public class SampleComment extends Comment {

	private static final long serialVersionUID = 1L;
	private Sample sample;

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
}

package edu.rpi.metpetdb.client.model;

public class SampleCommentDTO extends CommentDTO {
	
	private static final long serialVersionUID = 1L;
	private SampleDTO sample;

	public final SampleDTO getSample() {
		return sample;
	}

	public final void setSample(SampleDTO sample) {
		this.sample = sample;
	}
}

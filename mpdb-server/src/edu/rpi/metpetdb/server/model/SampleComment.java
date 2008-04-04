package edu.rpi.metpetdb.server.model;

public class SampleComment extends MObject {

	private static final long serialVersionUID = 1L;
	private long id;
	private Sample sample;
	private String text;
	private int version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Sample getSample() {
		return sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean mIsNew() {
		// TODO Auto-generated method stub
		return false;
	}

}

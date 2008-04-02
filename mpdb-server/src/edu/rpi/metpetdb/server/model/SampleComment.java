package edu.rpi.metpetdb.server.model;

public class SampleComment extends MObject {

	private static final long serialVersionUID = 1L;
	private long id;
	private Sample sample;
	private String text;
	private int version;

	public final long getId() {
		return id;
	}

	public final void setId(long id) {
		this.id = id;
	}

	public final int getVersion() {
		return version;
	}

	public final void setVersion(int version) {
		this.version = version;
	}

	public final Sample getSample() {
		return sample;
	}

	public final void setSample(Sample sample) {
		this.sample = sample;
	}

	public final String getText() {
		return text;
	}

	public final void setText(String text) {
		this.text = text;
	}

	@Override
	public boolean mIsNew() {
		// TODO Auto-generated method stub
		return false;
	}

}

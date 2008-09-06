package edu.rpi.metpetdb.server.model;

public abstract class Comment extends MObject {

	private static final long serialVersionUID = 1L;
	private long id;
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

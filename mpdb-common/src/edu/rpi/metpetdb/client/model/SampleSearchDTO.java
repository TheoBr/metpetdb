package edu.rpi.metpetdb.client.model;

import java.util.Set;

public class SampleSearchDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;

	private long id;
	private Set<String> rockTypes;

	private String sesarNumber;

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public final String getSesarNumber() {
		return sesarNumber;
	}

	public final void setSesarNumber(final String sesarNumber) {
		this.sesarNumber = sesarNumber;
	}

	public Set<String> getRockTypes() {
		return rockTypes;
	}

	public void setRockTypes(final Set<String> rockTypes) {
		this.rockTypes = rockTypes;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
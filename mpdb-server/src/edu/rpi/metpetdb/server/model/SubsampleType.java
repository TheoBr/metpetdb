package edu.rpi.metpetdb.server.model;

public class SubsampleType extends MObject {
	private static final long serialVersionUID = 1L;

	private short id;
	private String subsampleType;

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getSubsampleType() {
		return subsampleType;
	}

	public void setSubsampleType(final String s) {
		subsampleType = s;
	}

	public boolean equals(final Object o) {
		return subsampleType != null && o instanceof SubsampleType
				&& subsampleType.equals(((SubsampleType) o).subsampleType);
	}

	public int hashCode() {
		return subsampleType != null ? subsampleType.hashCode() : 0;
	}

	public String toString() {
		return subsampleType;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}

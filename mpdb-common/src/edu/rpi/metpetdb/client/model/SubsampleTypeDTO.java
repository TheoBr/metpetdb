package edu.rpi.metpetdb.client.model;


public class SubsampleTypeDTO extends MObjectDTO {
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
		return subsampleType != null && o instanceof SubsampleTypeDTO
				&& subsampleType.toLowerCase().equals(((SubsampleTypeDTO) o).subsampleType.toLowerCase());
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

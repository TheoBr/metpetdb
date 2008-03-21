package edu.rpi.metpetdb.client.model;


public class ImageTypeDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private short id;
	private String name;

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(final String s) {
		name = s;
	}

	public boolean equals(final Object o) {
		return name != null && o instanceof ImageTypeDTO
				&& name.equals(((ImageTypeDTO) o).name);
	}

	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}

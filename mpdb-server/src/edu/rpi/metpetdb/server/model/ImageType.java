package edu.rpi.metpetdb.server.model;


public class ImageType extends MObject {
	private static final long serialVersionUID = 1L;
	public static final int P_name = 0;

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
		return name != null && o instanceof ImageType
				&& name.equals(((ImageType) o).name);
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

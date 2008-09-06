package edu.rpi.metpetdb.server.model;

public class ImageType extends MObject {
	private static final long serialVersionUID = 1L;

	private short id;
	private String imageType;

	public short getId() {
		return id;
	}

	public void setId(final short i) {
		id = i;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(final String s) {
		imageType = s;
	}

	public boolean equals(final Object o) {
		return imageType != null && o instanceof ImageType
				&& imageType.equals(((ImageType) o).imageType);
	}

	public int hashCode() {
		return imageType != null ? imageType.hashCode() : 0;
	}

	public String toString() {
		return imageType;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}

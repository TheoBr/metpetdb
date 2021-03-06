package edu.rpi.metpetdb.client.model;

public class ImageType extends MObject {
	private static final long serialVersionUID = 1L;

	private short id;
	private String imageType;
	private String abbreviation;

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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public boolean equals(final Object o) {
		return imageType != null
				&& o instanceof ImageType
				&& (imageType.toLowerCase().equals(
						((ImageType) o).imageType.toLowerCase()) || (abbreviation
						.toLowerCase().equals(((ImageType) o).imageType
						.toLowerCase())));
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

package edu.rpi.metpetdb.client.model;



public class ImageTypeDTO extends MObjectDTO {
	private static final long serialVersionUID = 1L;

	private short id;
	private String imageType;
	
	public ImageTypeDTO() {
		
	}

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
		return imageType != null && o instanceof ImageTypeDTO
				&& imageType.toLowerCase().equals(((ImageTypeDTO) o).imageType.toLowerCase());
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

package edu.rpi.metpetdb.client.model;

public class ImageComment extends Comment {

	private static final long serialVersionUID = 1L;
	private Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}

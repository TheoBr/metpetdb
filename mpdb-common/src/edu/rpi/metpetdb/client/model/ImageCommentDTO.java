package edu.rpi.metpetdb.client.model;

public class ImageCommentDTO extends CommentDTO {
	
	private static final long serialVersionUID = 1L;
	private ImageDTO image;

	public final ImageDTO getImage() {
		return image;
	}

	public final void setImage(ImageDTO sample) {
		this.image = sample;
	}
}

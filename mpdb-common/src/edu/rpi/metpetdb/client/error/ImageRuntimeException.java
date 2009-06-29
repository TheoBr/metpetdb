package edu.rpi.metpetdb.client.error;


public class ImageRuntimeException extends DAOException{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ImageRuntimeException() {
		message = "Error Uploading Image - Make sure each image is a TIFF, BMP, GIF, JPEG, PNG, or PNM";
	}
	
	public ImageRuntimeException(final String message) {
		this.message = message;
	}

	@Override
	public String format() {
		return message;
	}

}

package edu.rpi.metpetdb.client.error.bulk.upload;

import edu.rpi.metpetdb.client.error.MpDbException;

public class ImageForAnalysisNotFound extends MpDbException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public ImageForAnalysisNotFound(){
		message = "";
	}
	
	public ImageForAnalysisNotFound(final String message) {
		this.message = message;
	}

	@Override
	public String format() {
		return message;
	}
	

}

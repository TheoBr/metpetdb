package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UnableToUploadImageException extends ValidationException implements
		IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;
	
	public UnableToUploadImageException(final String msg) {
		this.msg = msg;
	}

	@Override
	public String format() {
		if (msg == null)
			return "Unable to upload image";
		else
			return msg;
	} 
		
	
		
}

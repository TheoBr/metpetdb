package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LoginRequiredException extends MpDbException implements IsSerializable {
	private static final long serialVersionUID = 1L;
	private String message;

	public LoginRequiredException() {
	}
	
	public LoginRequiredException(final String message) {
		this.message = message;
	}

	@Override
	public String format() {
		return message == null ? "" : message;
	}
}

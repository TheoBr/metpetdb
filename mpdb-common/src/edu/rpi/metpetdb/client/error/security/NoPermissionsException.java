package edu.rpi.metpetdb.client.error.security;

import edu.rpi.metpetdb.client.error.DAOException;

public class NoPermissionsException extends DAOException {

	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public NoPermissionsException() {
		
	}
	
	public NoPermissionsException(final String message) {
		this.message = message;
	}

	@Override
	public String format() {
		return message;
	}
	
	

}

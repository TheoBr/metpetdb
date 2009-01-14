package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.interfaces.MObject;

public abstract class DAOException extends MpDbException implements
		IsSerializable {
	private static final long serialVersionUID = 1L;

	protected String msg;

	public DAOException() {
	}

	public DAOException(final String message) {
		msg = message;
	}
	
	public void handleObject(final MObject object) {
		
	}
}

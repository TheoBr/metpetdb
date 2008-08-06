package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class DAOException extends MpDbException implements
		IsSerializable {
	private static final long serialVersionUID = 1L;

	protected String msg;

	public DAOException() {
	}

	public DAOException(final String message) {
		msg = message;
	}
}

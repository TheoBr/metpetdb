package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UnableToSendEmailException extends Exception
		implements
			IsSerializable {
	private static final long serialVersionUID = 1L;

	public UnableToSendEmailException() {
	}
}

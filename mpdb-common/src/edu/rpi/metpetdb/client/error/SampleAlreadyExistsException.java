package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SampleAlreadyExistsException extends Exception
		implements
			IsSerializable {
	private static final long serialVersionUID = 1L;

	public SampleAlreadyExistsException() {
	}
}

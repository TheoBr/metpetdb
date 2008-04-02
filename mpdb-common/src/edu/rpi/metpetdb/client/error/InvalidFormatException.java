package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class InvalidFormatException extends Exception implements IsSerializable {
	static final long serialVersionUID = 1L;

	public InvalidFormatException() {
	}
}

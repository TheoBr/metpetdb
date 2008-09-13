package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccoundNotEnabledException extends LoginRequiredException implements IsSerializable {
	private static final long serialVersionUID = 1L;

	public AccoundNotEnabledException() {
	}
}
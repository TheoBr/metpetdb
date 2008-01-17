package edu.rpi.metpetdb.client.error;

import com.google.gwt.user.client.rpc.IsSerializable;

/** Indicates an object was not found. */
public class NoSuchObjectException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 1L;

	private String type;
	private String id;

	public NoSuchObjectException() {
	}

	public NoSuchObjectException(final String type, final String id) {
		super(type + " (id: " + id + ") not found.");
		this.type = type;
		this.id = id;
	}

	public String getType() {
		return type;
	}
	public String getId() {
		return id;
	}
}

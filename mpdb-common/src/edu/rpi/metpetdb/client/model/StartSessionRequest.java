package edu.rpi.metpetdb.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Sent by the browser to log a user into the application.
 * <p>
 * This object is never saved in the database. Instead we use it to bind
 * together the username nad password values and transport them to the server.
 * </p>
 */
public class StartSessionRequest implements edu.rpi.metpetdb.client.model.interfaces.MObject, IsSerializable {
	private static final long serialVersionUID = 1L;

	private String emailAddress;
	private String password;

	public StartSessionRequest() {
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String n) {
		emailAddress = n;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String p) {
		password = p;
	}

	public boolean mIsNew() {
		return true;
	}

	public Object mGet(Property property) {
		return property.get(this);
	}

	public void mSet(Property property, Object newVal) {
		property.set(this, newVal);
	}
}

package edu.rpi.metpetdb.client.model;

/**
 * Sent by the browser to log a user into the application.
 * <p>
 * This object is never saved in the database. Instead we use it to bind
 * together the username nad password values and transport them to the server.
 * </p>
 */
public class StartSessionRequest extends MObject {
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	public StartSessionRequest() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String n) {
		username = n;
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

	public void setEmailAddress(String username2) {
		// TODO Auto-generated method stub

	}

	public String getEmailAddress() {
		// TODO Auto-generated method stub
		return null;
	}
}

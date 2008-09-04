package edu.rpi.metpetdb.client.model;


/**
 * Sent by the browser to log a user into the application.
 * <p>
 * This object is never saved in the database. Instead we use it to bind
 * together the username nad password values and transport them to the server.
 * </p>
 */
public class StartSessionRequestDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private String emailAddress;
	private String password;

	public StartSessionRequestDTO() {
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
}

package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

/**
 * Sent by the browser to log a user into the application.
 * <p>
 * This object is never saved in the database. Instead we use it to bind
 * together the username nad password values and transport them to the server.
 * </p>
 */
public class StartSessionRequestDTO extends MObjectDTO {
	public static final int P_username = 0;
	public static final int P_password = 1;

	private String username;
	private String password;

	public StartSessionRequestDTO() {
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
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_username :
				if (newValue != GET_ONLY)
					setUsername((String) newValue);
				return getUsername();

			case P_password :
				if (newValue != GET_ONLY)
					setPassword((String) newValue);
				return getPassword();
		}
		throw new InvalidPropertyException(propertyId);
	}
}

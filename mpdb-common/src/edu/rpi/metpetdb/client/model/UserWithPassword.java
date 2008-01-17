package edu.rpi.metpetdb.client.model;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;

public class UserWithPassword extends MObject {
	public static final int P_oldPassword = 0;
	public static final int P_newPassword = 1;
	public static final int P_vrfPassword = 2;
	public static final int P_user = 3;

	private User user;
	private String oldPassword;
	private String newPassword;
	private String vrfPassword;

	public UserWithPassword() {
	}
	public UserWithPassword(final User u) {
		user = u;
	}

	public User getUser() {
		return user;
	}
	public void setUser(final User u) {
		user = u;
	}

	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(final String p) {
		oldPassword = p;
	}

	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(final String p) {
		newPassword = p;
	}

	public String getVrfPassword() {
		return vrfPassword;
	}
	public void setVrfPassword(final String p) {
		vrfPassword = p;
	}

	public boolean mIsNew() {
		return true;
	}
	protected Object mSetGet(final int propertyId, final Object newValue) {
		switch (propertyId) {
			case P_oldPassword :
				if (newValue != GET_ONLY)
					setOldPassword((String) newValue);
				return getOldPassword();

			case P_newPassword :
				if (newValue != GET_ONLY)
					setNewPassword((String) newValue);
				return getNewPassword();

			case P_vrfPassword :
				if (newValue != GET_ONLY)
					setVrfPassword((String) newValue);
				return getVrfPassword();

			case P_user :
				if (newValue != GET_ONLY)
					setUser((User) newValue);
				return getUser();
		}
		throw new InvalidPropertyException(propertyId);
	}
}

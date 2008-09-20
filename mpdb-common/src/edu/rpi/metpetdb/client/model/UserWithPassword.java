package edu.rpi.metpetdb.client.model;

public class UserWithPassword extends MObject {
	private static final long serialVersionUID = 1L;

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
}

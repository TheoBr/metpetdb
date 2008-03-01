package edu.rpi.metpetdb.server.model;


public class UserWithPassword extends MObject {
	private static final long serialVersionUID = 1L;
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
}

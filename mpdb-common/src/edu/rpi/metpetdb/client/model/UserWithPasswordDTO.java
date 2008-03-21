package edu.rpi.metpetdb.client.model;


public class UserWithPasswordDTO extends MObjectDTO {

	private static final long serialVersionUID = 1L;
	private UserDTO user;
	private String oldPassword;
	private String newPassword;
	private String vrfPassword;

	public UserWithPasswordDTO() {
	}

	public UserWithPasswordDTO(final UserDTO u) {
		user = u;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(final UserDTO u) {
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

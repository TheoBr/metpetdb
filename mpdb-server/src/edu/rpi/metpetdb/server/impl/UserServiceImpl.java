package edu.rpi.metpetdb.server.impl;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DuplicateValueException;
import edu.rpi.metpetdb.client.error.LoginFailureException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;
import edu.rpi.metpetdb.client.service.ResumeSessionResponse;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.User;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class UserServiceImpl extends MpDbServlet implements UserService {
	private static final long serialVersionUID = 1L;

	private static boolean authenticate(final User u, final String pw) {
		return PasswordEncrypter.verify(u.getEncryptedPassword(), pw);
	}

	public UserDTO details(final String username) throws NoSuchObjectException {
		UserDTO UserDTO = (UserDTO) clone( byKey("User", "username", username));
		return UserDTO;
	}

	public UserDTO startSession(final StartSessionRequestDTO ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		try {
			final User u = (User) byKey("User", "username", ssr.getUsername());
			if (!authenticate(u, ssr.getPassword()))
				throw new LoginFailureException(doc.StartSessionRequest_password);
			setCurrentUser(u);
			return (UserDTO) clone(u);
		} catch (NoSuchObjectException nsoe) {
			setCurrentUser(null);
			throw new LoginFailureException(doc.StartSessionRequest_password);
		}
	}

	public ResumeSessionResponse resumeSession() {
		final ResumeSessionResponse r = new ResumeSessionResponse();
		r.databaseObjectConstraints = doc;
		r.objectConstraints = oc;
		try {
			r.user =  (UserDTO) clone( byId("User", (long) currentUser()));
		} catch (NoSuchObjectException err) {
			r.user = null;
		} catch (LoginRequiredException err) {
			r.user = null;
		}
		forgetChanges();
		return r;
	}

	public UserDTO beginEditMyProfile() throws NoSuchObjectException,
			LoginRequiredException {
		return (UserDTO)clone( byId("User", (long)currentUser()));
	}

	public UserDTO registerNewUser(final UserWithPasswordDTO newbie)
			throws ValidationException, UnableToSendEmailException {
		doc.UserWithPassword_user.validateEntity(newbie);
		doc.UserWithPassword_newPassword.validateEntity(newbie);
		doc.UserWithPassword_vrfPassword.validateEntity(newbie);

		final UserDTO UserDTO = newbie.getUser();
		if (!UserDTO.mIsNew())
			throw new SecurityException("Cannot register non-new UserDTO.");

		final String pass = newbie.getNewPassword();
		UserDTO.setEncryptedPassword(PasswordEncrypter.crypt(pass));
		doc.validate(UserDTO);
		User u = (User) merge(newbie);
		try {
			insert(u);
			commit();
			EmailSupport.sendMessage(this, u.getEmailAddress(),
					"registerNewUser", new Object[]{u.getUsername(),
							getModuleBaseURL()});
			setCurrentUser(u);
			return (UserDTO) clone(u);
		} catch (ConstraintViolationException cve) {
			final String who = UserDTO.getUsername();
			if ("users_nk_username".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.User_username, who);
			throw new DuplicateValueException(doc.User_username, who);
		}
	}

	public void changePassword(final UserWithPasswordDTO uwp)
			throws LoginRequiredException, LoginFailureException,
			NoSuchObjectException, ValidationException {
		doc.UserWithPassword_user.validateEntity(uwp);
		doc.UserWithPassword_oldPassword.validateEntity(uwp);
		doc.UserWithPassword_newPassword.validateEntity(uwp);
		doc.UserWithPassword_vrfPassword.validateEntity(uwp);

		final UserDTO UserDTO = uwp.getUser();
		if (UserDTO.getId() != currentUser())
			throw new SecurityException("Administrators are not supported!");

		final User u = (User) byId("UserDTO", UserDTO.getId());
		if (!authenticate(u, uwp.getOldPassword()))
			throw new LoginFailureException(doc.UserWithPassword_oldPassword);
		u.setEncryptedPassword(PasswordEncrypter.crypt(uwp.getNewPassword()));
		update(u);
		commit();
	}

	public void emailPassword(final String username)
			throws NoSuchObjectException, UnableToSendEmailException {
		final User u = (User) byKey("UserDTO", "username", username);
		if (u == null)
			throw new NoSuchObjectException();
		final String newpass = PasswordEncrypter.randomPassword();
		u.setEncryptedPassword(PasswordEncrypter.crypt(newpass));
		update(u);
		commit();
		EmailSupport.sendMessage(this, u.getEmailAddress(), "emailPassword",
				new Object[]{u.getUsername(), newpass, getModuleBaseURL()});
	}
}

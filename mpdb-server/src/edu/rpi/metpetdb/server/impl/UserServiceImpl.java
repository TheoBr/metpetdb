package edu.rpi.metpetdb.server.impl;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DuplicateValueException;
import edu.rpi.metpetdb.client.error.LoginFailureException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.service.ResumeSessionResponse;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class UserServiceImpl extends MpDbServlet implements UserService {
	private static final long serialVersionUID = 1L;

	private static boolean authenticate(final User u, final String pw) {
		return PasswordEncrypter.verify(u.getEncryptedPassword(), pw);
	}

	public User details(final String username) throws NoSuchObjectException {
		User user = (User) byKey("User", "username", username);
		user.setProjects(load(user.getProjects()));
		forgetChanges();
		return user;
	}

	public User startSession(final StartSessionRequest ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		try {
			final User u = (User) byKey("User", "username", ssr.getUsername());
			if (!authenticate(u, ssr.getPassword()))
				throw new LoginFailureException(doc.StartSessionRequest_password);
			u.setProjects(load(u.getProjects()));
			setCurrentUser(u);
			forgetChanges();
			return u;
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
			r.user = (User) byId("User", currentUser());
			r.user.setProjects(load(r.user.getProjects()));
		} catch (NoSuchObjectException err) {
			r.user = null;
		} catch (LoginRequiredException err) {
			r.user = null;
		}
		forgetChanges();
		return r;
	}

	public User beginEditMyProfile() throws NoSuchObjectException,
			LoginRequiredException {
		return (User) byId("User", currentUser());
	}

	public User registerNewUser(final UserWithPassword newbie)
			throws ValidationException, UnableToSendEmailException {
		doc.UserWithPassword_user.validateEntity(newbie);
		doc.UserWithPassword_newPassword.validateEntity(newbie);
		doc.UserWithPassword_vrfPassword.validateEntity(newbie);

		final User user = newbie.getUser();
		if (!user.mIsNew())
			throw new SecurityException("Cannot register non-new user.");

		final String pass = newbie.getNewPassword();
		user.setEncryptedPassword(PasswordEncrypter.crypt(pass));
		doc.validate(user);

		try {
			insert(user);
			commit();
			EmailSupport.sendMessage(this, user.getEmailAddress(),
					"registerNewUser", new Object[]{user.getUsername(),
							getModuleBaseURL()});
			user.setProjects(load(user.getProjects()));
			setCurrentUser(user);
			forgetChanges();
			return user;
		} catch (ConstraintViolationException cve) {
			final String who = user.getUsername();
			if ("users_nk_username".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.User_username, who);
			throw new DuplicateValueException(doc.User_username, who);
		}
	}

	public void changePassword(final UserWithPassword uwp)
			throws LoginRequiredException, LoginFailureException,
			NoSuchObjectException, ValidationException {
		doc.UserWithPassword_user.validateEntity(uwp);
		doc.UserWithPassword_oldPassword.validateEntity(uwp);
		doc.UserWithPassword_newPassword.validateEntity(uwp);
		doc.UserWithPassword_vrfPassword.validateEntity(uwp);

		final User user = uwp.getUser();
		if (user.getId() != currentUser())
			throw new SecurityException("Administrators are not supported!");

		final User u = (User) byId("User", user.getId());
		if (!authenticate(u, uwp.getOldPassword()))
			throw new LoginFailureException(doc.UserWithPassword_oldPassword);
		u.setEncryptedPassword(PasswordEncrypter.crypt(uwp.getNewPassword()));
		update(u);
		commit();
	}

	public void emailPassword(final String username)
			throws NoSuchObjectException, UnableToSendEmailException {
		final User u = (User) byKey("User", "username", username);
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

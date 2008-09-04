package edu.rpi.metpetdb.server.impl;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.DuplicateValueException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;
import edu.rpi.metpetdb.client.service.ResumeSessionResponse;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.model.User;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

public class UserServiceImpl extends MpDbServlet implements UserService {
	private static final long serialVersionUID = 1L;

	private static boolean authenticate(final User u, final String pw) {
		return PasswordEncrypter.verify(u.getEncryptedPassword(), pw);
	}

	public UserDTO details(final String emailAddress) throws DAOException {
		User u = new User();
		u.setEmailAddress(emailAddress);
		u = (new UserDAO(this.currentSession())).fill(u);
		return cloneBean(u);
	}

	public UserDTO startSession(final StartSessionRequestDTO ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		try {
			User u = new User();
			u.setEmailAddress(ssr.getEmailAddress());
			u = (new UserDAO(this.currentSession())).fill(u);

			if (!authenticate(u, ssr.getPassword()))
				throw new LoginFailureException(
						doc.StartSessionRequest_password);
			setCurrentUser(u);
			return (UserDTO) clone(u);
		} catch (DAOException daoe) {
			setCurrentUser(null);
			throw new LoginFailureException(doc.StartSessionRequest_password);
		}
	}

	public ResumeSessionResponse resumeSession() {
		final ResumeSessionResponse r = new ResumeSessionResponse();
		r.databaseObjectConstraints = doc;
		r.objectConstraints = oc;
		try {
			User u = new User();
			u.setId(currentUser());
			u = (new UserDAO(this.currentSession())).fill(u);
			r.user = cloneBean(u);
			// r.user = (UserDTO) clone(byId("User", (long) currentUser()));
		} catch (DAOException daoe) {
			r.user = null;
		} catch (LoginRequiredException err) {
			r.user = null;
		}
		forgetChanges();
		return r;
	}

	public UserDTO beginEditMyProfile() throws DAOException,
			LoginRequiredException {
		User u = new User();
		u.setId(currentUser());
		u = (new UserDAO(this.currentSession())).fill(u);
		return cloneBean(u);
	}

	public UserDTO registerNewUser(final UserWithPasswordDTO newbie)
			throws ValidationException, DAOException,
			UnableToSendEmailException {
		doc.UserWithPassword_user.validateEntity(newbie);
		doc.UserWithPassword_newPassword.validateEntity(newbie);
		doc.UserWithPassword_vrfPassword.validateEntity(newbie);

		final UserDTO newUser = newbie.getUser();
		if (!newUser.mIsNew())
			throw new SecurityException("Cannot register non-new user.");

		final String pass = newbie.getNewPassword();
		newUser.setEncryptedPassword(PasswordEncrypter.crypt(pass));
		doc.validate(newUser);
		User u = (User) merge(newUser);
		try {
			u = (new UserDAO(this.currentSession())).save(u);
			commit();
			EmailSupport.sendMessage(this, u.getEmailAddress(),
					"registerNewUser", new Object[] {
							u.toString(), getModuleBaseURL()
					});
			setCurrentUser(u);
		} catch (ConstraintViolationException cve) {
			final String who = newUser.getEmailAddress();
			if ("users_nk_username".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.User_emailAddress, who);
			else
				throw new RuntimeException("i dont know what happened");
		}

		return (UserDTO) clone(u);
	}

	public void changePassword(final UserWithPasswordDTO uwp)
			throws LoginRequiredException, LoginFailureException, DAOException,
			ValidationException {
		doc.UserWithPassword_user.validateEntity(uwp);
		doc.UserWithPassword_oldPassword.validateEntity(uwp);
		doc.UserWithPassword_newPassword.validateEntity(uwp);
		doc.UserWithPassword_vrfPassword.validateEntity(uwp);

		final UserDAO uDAO = new UserDAO(this.currentSession());
		final UserDTO UserDTO = uwp.getUser();
		if (UserDTO.getId() != currentUser())
			throw new SecurityException("Administrators are not supported!");

		User u = new User();
		u.setId(UserDTO.getId());
		u = uDAO.fill(u);

		if (!authenticate(u, uwp.getOldPassword()))
			throw new LoginFailureException(doc.UserWithPassword_oldPassword);
		u.setEncryptedPassword(PasswordEncrypter.crypt(uwp.getNewPassword()));

		uDAO.save(u);
		commit();
	}

	public void emailPassword(final String emailAddress) throws DAOException,
			UnableToSendEmailException {
		UserDAO uDAO = new UserDAO(this.currentSession());
		User u = new User();
		u.setEmailAddress(emailAddress);
		u = uDAO.fill(u);
		final String newpass = PasswordEncrypter.randomPassword();
		u.setEncryptedPassword(PasswordEncrypter.crypt(newpass));
		u = uDAO.save(u);
		commit();
		EmailSupport.sendMessage(this, u.getEmailAddress(), "emailPassword",
				new Object[] {
						u.toString(), newpass, getModuleBaseURL()
				});
	}
}

package edu.rpi.metpetdb.server.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.DuplicateValueException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;
import edu.rpi.metpetdb.server.security.permissions.principals.AdminPrincipal;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class UserServiceImpl extends MpDbServlet implements UserService {
	private static final long serialVersionUID = 1L;

	private static boolean authenticate(final User u, final String pw) {
		return PasswordEncrypter.verify(u.getEncryptedPassword(), pw);
	}

	public User details(final String emailAddress) throws DAOException {
		User u = new User();
		u.setEmailAddress(emailAddress);
		u = (new UserDAO(this.currentSession())).fill(u);
		return (u);
	}

	public User save(User user) throws DAOException, ValidationException {
		final UserDAO uDAO = new UserDAO(this.currentSession());
		doc.validate(user);
		User u = new User();
		u.setId(user.getId());
		u = uDAO.fill(u);
		user.setEncryptedPassword(u.getEncryptedPassword());
		user = uDAO.save(user);
		commit();
		user.setEncryptedPassword(null);
		return (user);
	}

	public Set<String> allNames() throws DAOException {
		final Object[] l = (new UserDAO(this.currentSession())).allNames();
		final Set<String> options = new HashSet<String>();
		for (int i = 0; i < l.length; i++) {
			if (l[i] != null)
				options.add(l[i].toString());
		}
		return options;
	}

	public User startSession(final StartSessionRequest ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		try {
			User u = new User();
			u.setEmailAddress(ssr.getEmailAddress());
			u = (new UserDAO(currentSession())).fill(u);
			
			if (!PasswordEncrypter.verify(u.getEncryptedPassword(), ssr.getPassword())) {
				throw new GenericDAOException();
			} else {
				final Collection<Principal> principals = new ArrayList<Principal>();
				principals.add(new OwnerPrincipal(u));
				if (ssr.getEmailAddress().equals("watera2@cs.rpi.edu"))
					principals.add(new AdminPrincipal());
				setCurrentUser(u, principals);
			}
			return u;
		} catch (DAOException e) {
			e.printStackTrace();
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
			r.user = (User) clone(u);
		} catch (DAOException daoe) {
			r.user = null;
		} catch (LoginRequiredException err) {
			r.user = null;
		}
		return r;
	}

	public User beginEditMyProfile() throws DAOException,
			LoginRequiredException {
		User u = new User();
		u.setId(currentUser());
		u = (new UserDAO(this.currentSession())).fill(u);
		return (u);
	}

	public User registerNewUser(final UserWithPassword newbie)
			throws ValidationException, DAOException,
			UnableToSendEmailException {
		doc.UserWithPassword_user.validateEntity(newbie);
		doc.UserWithPassword_newPassword.validateEntity(newbie);
		doc.UserWithPassword_vrfPassword.validateEntity(newbie);

		final User newUser = newbie.getUser();
		if (!newUser.mIsNew())
			throw new GenericDAOException("Cannot register non-new user.");

		final String pass = newbie.getNewPassword();
		newUser.setEncryptedPassword(PasswordEncrypter.crypt(pass));
		doc.validate(newUser);
		User u = newUser;
		u.setEnabled(false);
		u.setConfirmationCode(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			u = (new UserDAO(this.currentSession())).save(u);
			commit();
			EmailSupport.sendMessage(this, u.getEmailAddress(),
					"registerNewUser", new Object[] {
							u.toString(),
							getModuleBaseURL(),
							getModuleBaseURL() + "#ConfirmationCode-"
									+ u.getConfirmationCode()
					});
			setCurrentUser(u, null);
		} catch (ConstraintViolationException cve) {
			final String who = newUser.getEmailAddress();
			if ("users_nk_username".equals(cve.getConstraintName()))
				throw new DuplicateValueException(doc.User_emailAddress, who);
			else
				throw new RuntimeException("i dont know what happened");
		}

		return u;
	}

	public void changePassword(final UserWithPassword uwp)
			throws LoginRequiredException, LoginFailureException, DAOException,
			ValidationException {
		doc.UserWithPassword_user.validateEntity(uwp);
		doc.UserWithPassword_oldPassword.validateEntity(uwp);
		doc.UserWithPassword_newPassword.validateEntity(uwp);
		doc.UserWithPassword_vrfPassword.validateEntity(uwp);

		final UserDAO uDAO = new UserDAO(this.currentSession());
		final User User = uwp.getUser();
		if (User.getId() != currentUser())
			throw new GenericDAOException("Administrators are not supported!");

		User u = new User();
		u.setId(User.getId());
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

	public User confirmUser(String confirmationCode) throws DAOException,
			LoginRequiredException {
		User u = new User();
		u.setId(currentUser());
		final UserDAO ud = new UserDAO(this.currentSession());
		u = (ud).fill(u);
		if (u.getConfirmationCode().equals(confirmationCode) && !u.getEnabled()) {
			u.setEnabled(true);
			u.setConfirmationCode("");
			ud.save(u);
			commit();
			return u;
		} else {
			throw new GenericDAOException("Confirmation code does not equal");
		}
	}
}

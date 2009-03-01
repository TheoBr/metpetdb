package edu.rpi.metpetdb.server.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.DuplicateValueException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.Role;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.security.Action;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;
import edu.rpi.metpetdb.server.security.permissions.principals.AdminPrincipal;
import edu.rpi.metpetdb.server.security.permissions.principals.EnabledPrincipal;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class UserServiceImpl extends MpDbServlet implements UserService {
	private static final long serialVersionUID = 1L;

	private static boolean authenticate(final User u, final String pw) {
		return PasswordEncrypter.verify(u.getEncryptedPassword(), pw);
	}

	public void endSession() {
		setCurrentUser(null, null);
	}

	public User details(final String emailAddress) throws MpDbException {
		User u = new User();
		u.setEmailAddress(emailAddress);
		u = (new UserDAO(this.currentSession())).fill(u);
		return (u);
	}

	public User save(User user) throws MpDbException, ValidationException {
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

	public Set<String> allNames() throws MpDbException {
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
			currentReq().action = Action.LOGIN;
			u.setEmailAddress(ssr.getEmailAddress());
			u = (new UserDAO(currentSession())).fill(u);

			if (!PasswordEncrypter.verify(u.getEncryptedPassword(), ssr
					.getPassword())) {
				throw new GenericDAOException();
			} else {
				final Collection<Principal> principals = new ArrayList<Principal>();
				principals.add(new OwnerPrincipal(u));
				principals.add(new EnabledPrincipal(u));
				if (ssr.getEmailAddress().equals("watera2@cs.rpi.edu"))
					principals.add(new AdminPrincipal());
				setCurrentUser(u, principals);
			}
			return u;
		} catch (MpDbException e) {
			e.printStackTrace();
			throw new LoginFailureException(doc.StartSessionRequest_password);
		}
	}

	private void addPrincipal(final Principal newOne) {
		currentReq().principals.add(newOne);
		getThreadLocalRequest().getSession().setAttribute("principals",
				currentReq().principals);
	}

	public ResumeSessionResponse resumeSession() {
		final ResumeSessionResponse r = new ResumeSessionResponse();
		r.databaseObjectConstraints = doc;
		r.objectConstraints = oc;
		try {
			User u = new User();
			u.setId(currentUserId());
			currentReq().action = Action.LOGIN;
			u = (new UserDAO(this.currentSession())).fill(u);
			r.user = (User) clone(u);
			final Collection<Principal> principals = new ArrayList<Principal>();
			principals.add(new OwnerPrincipal(u));
			principals.add(new EnabledPrincipal(u));
			if (u.getEmailAddress().equals("watera2@cs.rpi.edu"))
				principals.add(new AdminPrincipal());
			setCurrentUser(u, principals);
		} catch (MpDbException daoe) {
			r.user = null;
		}
		return r;
	}

	public User beginEditMyProfile() throws MpDbException,
			LoginRequiredException {
		User u = new User();
		u.setId(currentUserId());
		u = (new UserDAO(this.currentSession())).fill(u);
		return (u);
	}

	public User registerNewUser(final UserWithPassword newbie)
			throws ValidationException, MpDbException,
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
		u.setRole((Role) currentSession().createQuery(
				"from Role r where r.rank=1").uniqueResult());
		u.setConfirmationCode(UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			u = (new UserDAO(this.currentSession())).save(u);
			commit();
			EmailSupport.sendMessage(this, u.getEmailAddress(),
					"registerNewUser", new Object[] {
							u.toString(),
							getModuleBaseURL(),
							getModuleBaseURL() + "#ConfirmationCode/"
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
			throws LoginRequiredException, LoginFailureException,
			MpDbException, ValidationException {
		doc.UserWithPassword_user.validateEntity(uwp);
		doc.UserWithPassword_oldPassword.validateEntity(uwp);
		doc.UserWithPassword_newPassword.validateEntity(uwp);
		doc.UserWithPassword_vrfPassword.validateEntity(uwp);

		final UserDAO uDAO = new UserDAO(this.currentSession());
		final User User = uwp.getUser();
		if (User.getId() != currentUserId())
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

	public void emailPassword(final String emailAddress) throws MpDbException,
			UnableToSendEmailException {
		UserDAO uDAO = new UserDAO(this.currentSession());
		User u = new User();
		u.setEmailAddress(emailAddress);
		currentReq().action = Action.EMAIL_PASSWORD;
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

	public void sendConfirmationCode(User user) throws ValidationException,
			MpDbException, UnableToSendEmailException {
		final UserDAO uDAO = new UserDAO(this.currentSession());
		User u = new User();
		u.setId(currentUserId());
		u = uDAO.fill(u);
		u.setConfirmationCode(UUID.randomUUID().toString().replaceAll("-",
		""));
		u = uDAO.save(u);
		commit();
		EmailSupport.sendMessage(this, u.getEmailAddress(),
				"sendConfirmationCode", new Object[] {
						u.toString(),
						getModuleBaseURL() + "#ConfirmationCode/"
								+ u.getConfirmationCode()
				});
		setCurrentUser(u, null);
	}

	public User confirmUser(String confirmationCode) throws MpDbException,
			LoginRequiredException {
		User u = new User();
		u.setId(currentUserId());
		final UserDAO ud = new UserDAO(this.currentSession());
		u = (ud).fill(u);
		if (u.getConfirmationCode().equals(confirmationCode) && !u.getEnabled()) {
			u.setEnabled(true);
			u.setConfirmationCode("");
			ud.save(u);
			commit();
			// update session with new permission
			addPrincipal(new EnabledPrincipal(u));
			return u;
		} else {
			throw new GenericDAOException("Confirmation code does not equal");
		}
	}
}

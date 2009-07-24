package edu.rpi.metpetdb.server.impl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.UserService;
import edu.rpi.metpetdb.server.EmailSupport;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.RoleChangeDAO;
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
		return objectArrayToStringSet((new UserDAO(this.currentSession())).allNames());
	}
	
	public Set<String> viewableNamesForUser(final int userId) throws MpDbException {
		this.currentSession().enableFilter("hasSamplePublicOrUser").setParameter("userId", userId);
		return objectArrayToStringSet((new UserDAO(this.currentSession())).allNames());
	}
	
	private Set<String> objectArrayToStringSet(final Object[] o){
		final Set<String> options = new HashSet();
		for (int i = 0; i < o.length; i++){
			if (o[i] != null)
				options.add(o[i].toString());
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
			final Collection<Principal> principals = new ArrayList<Principal>();
			principals.add(new OwnerPrincipal(u));
			setCurrentUser(u, principals);
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
		u.setConfirmationCode(UUID.randomUUID().toString().replaceAll("-", ""));
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
			u = ud.save(u);
			commit();
			// update session with new permission
			final Collection<Principal> principals;
			if (currentReq().principals != null)
				principals = currentReq().principals;
			else
				principals = new HashSet<Principal>();
			// remove any old enabled principals
			principals.remove(new EnabledPrincipal(false));
			principals.add(new EnabledPrincipal(u));
			setCurrentUser(u, principals);
			return u;
		} else {
			throw new GenericDAOException("Confirmation code does not equal");
		}
	}

	public Collection<User> getEligableSponsors(Role e) throws MpDbException {
		return new UserDAO(currentSession()).getEligableSponsors(e);
	}

	public RoleChange getRoleChange(int id) throws MpDbException {
		RoleChange rc = new RoleChange();
		rc.setId(id);
		rc = (new RoleChangeDAO(this.currentSession())).fill(rc);
		return rc;
	}

	public RoleChange saveRoleChange(RoleChange rc) throws MpDbException, UnableToSendEmailException {
		rc = new RoleChangeDAO(this.currentSession()).save(rc);
		commit();
		//if we get here the role change saved successfully so now
		//we should send an email to the sponsor
		EmailSupport.sendMessage(this, rc.getSponsor().getEmailAddress(),
				"sendRoleRequest", new Object[] {
						rc.getSponsor().toString(),
						rc.getUser().toString(),
						getModuleBaseURL() + "#ConfirmRoleChange/"
								+ rc.getSponsor().getId()
				});
		return rc;
	}

	public Collection<Role> getEligableRoles(int currentRank) throws MpDbException {
		return new UserDAO(currentSession()).getEligableRoles(currentRank);
	}
	
	public Results<RoleChange> getSponsorRoleChanges(int sponsorId, PaginationParameters p) throws MpDbException {
		return new UserDAO(currentSession()).getSponsorRoleChanges(sponsorId, p);
	}
	
	/**
	 * used for pagination tables to select all/public/private
	 * the boolean is null because users are not public or private
	 */
	public Map<Object,Boolean> getSponsorRoleChangeIds(int sponsorId) throws MpDbException {
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Long l : new UserDAO(this.currentSession()).getSponsorRoleChangeIds(sponsorId)){
			ids.put(l,null);
		}
		return ids;
	}

	public void approveRoleChange(RoleChange rc) throws MpDbException, UnableToSendEmailException {
		rc.setGranted(true);
		final UserDAO uDAO = new UserDAO(currentSession());
		rc.setUser(uDAO.fill(rc.getUser()));
		rc.getUser().setRole(rc.getRole());
		new UserDAO(currentSession()).save(rc.getUser());
		new RoleChangeDAO(currentSession()).save(rc);
		commit();
		//send approval message
		EmailSupport.sendMessage(this, rc.getUser().getEmailAddress(),
				"roleChangeApproved", new Object[] {
						rc.getUser().toString(),
						rc.getSponsor().toString(),
						rc.getGrantReason()
				});
	}

	public void denyRoleChange(RoleChange rc) throws MpDbException, UnableToSendEmailException {
		rc.setGranted(false);
		new RoleChangeDAO(currentSession()).save(rc);
		commit();
		//send denial message
		EmailSupport.sendMessage(this, rc.getUser().getEmailAddress(),
				"roleChangeDenied", new Object[] {
						rc.getUser().toString(),
						rc.getSponsor().toString(),
						rc.getGrantReason()
				});
	}
}

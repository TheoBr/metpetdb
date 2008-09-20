package edu.rpi.metpetdb.server.impl.admin;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.AdminUser;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.admin.AdminUserService;
import edu.rpi.metpetdb.server.MpDbServlet;

public class AdminUserServiceImpl extends MpDbServlet implements
		AdminUserService {

	private static final long serialVersionUID = 1L;

	public User startSession(StartSessionRequest ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		AdminUser u = new AdminUser();
		u.getUser().setEmailAddress(ssr.getUsername());
		// u = (new AdminUserDAO(this.currentSession())).fill(u);
		//
		// if (!authenticate(u, ssr.getPassword()))
		// throw new LoginFailureException(
		// doc.StartSessionRequest_password);
		// setCurrentUser(u);
		return u.getUser();
	}
}

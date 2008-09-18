package edu.rpi.metpetdb.server.impl.admin;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.admin.AdminUserService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.AdminUser;

public class AdminUserServiceImpl extends MpDbServlet implements
		AdminUserService {

	private static final long serialVersionUID = 1L;

	public UserDTO startSession(StartSessionRequestDTO ssr)
			throws LoginFailureException, ValidationException {
		doc.validate(ssr);
		AdminUser u = new AdminUser();
		u.getUser().setEmailAddress(ssr.getEmailAddress());
//			u = (new AdminUserDAO(this.currentSession())).fill(u);
//
//			if (!authenticate(u, ssr.getPassword()))
//				throw new LoginFailureException(
//						doc.StartSessionRequest_password);
//			setCurrentUser(u);
		return (UserDTO) clone(u);
	}
}

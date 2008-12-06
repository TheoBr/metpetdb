package edu.rpi.metpetdb.server.dao.permissions;

import junit.framework.TestCase;
import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.impl.UserServiceImpl;

public class LoginTests extends TestCase {

	public LoginTests() {
		//super("test-data/test-sample-data.xml");
	}
	public void testLogin() throws LoginFailureException, ValidationException, DAOException, UnableToSendEmailException {
		final UserServiceImpl userService = new UserServiceImpl();
		System.setProperty("java.security.auth.login.config", "../mpdb-server/src/edu/rpi/metpetdb/server/security/permissions/jaas.config");
		final StartSessionRequest ssr = new StartSessionRequest();
		ssr.setEmailAddress("watera2@cs.rpi.edu");
		ssr.setPassword("wht5nnmn");
		DataStore.open();
		//userService.emailPassword("watera2@cs.rpi.edu");
		userService.startSession(ssr);
	}
}

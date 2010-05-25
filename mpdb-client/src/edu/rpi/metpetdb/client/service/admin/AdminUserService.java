package edu.rpi.metpetdb.client.service.admin;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;

public interface AdminUserService extends RemoteService {

	User startSession(StartSessionRequest ssr) throws LoginFailureException,
			ValidationException;

}

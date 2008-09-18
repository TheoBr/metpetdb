package edu.rpi.metpetdb.client.service.admin;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.LoginFailureException;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public interface AdminUserService extends RemoteService {
	
	UserDTO startSession(StartSessionRequestDTO ssr)
			throws LoginFailureException, ValidationException;

}

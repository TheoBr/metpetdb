package edu.rpi.metpetdb.client.service.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public interface AdminUserServiceAsync  {
	
	void startSession(StartSessionRequestDTO ssr, AsyncCallback<UserDTO> ac);
}

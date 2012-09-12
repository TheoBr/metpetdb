package edu.rpi.metpetdb.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.gwt.client.dto.LoginRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

public interface LoginServiceAsync {

	void login(LoginRequestDTO loginRequest, AsyncCallback<UserDTO> callback)
	throws IllegalArgumentException;
}

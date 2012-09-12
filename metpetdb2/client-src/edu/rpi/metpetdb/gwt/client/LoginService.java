package edu.rpi.metpetdb.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.rpi.metpetdb.gwt.client.dto.LoginRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

@RemoteServiceRelativePath(value = "services/user")
public interface LoginService extends RemoteService {

	public UserDTO login(LoginRequestDTO loginDTO);
}

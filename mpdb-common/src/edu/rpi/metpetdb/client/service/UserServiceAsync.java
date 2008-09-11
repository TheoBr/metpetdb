package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;

/** @see UserService */
public interface UserServiceAsync {
	void startSession(StartSessionRequestDTO ssr, AsyncCallback<UserDTO> ac);

	void resumeSession(AsyncCallback<ResumeSessionResponse> ac);

	void beginEditMyProfile(AsyncCallback<UserDTO> ac);

	void registerNewUser(UserWithPasswordDTO newbie, AsyncCallback<UserDTO> ac);

	void changePassword(UserWithPasswordDTO uwp, AsyncCallback<Object> ac);

	void details(String username, AsyncCallback<UserDTO> ac);

	void emailPassword(String username, AsyncCallback<Object> ac);
	
	void confirmUser(String confirmationCode, AsyncCallback<UserDTO> ac);
}

package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.UserWithPasswordDTO;

/** @see UserService */
public interface UserServiceAsync {
	void startSession(StartSessionRequestDTO ssr, AsyncCallback ac);
	void resumeSession(AsyncCallback ac);
	void beginEditMyProfile(AsyncCallback ac);
	void registerNewUser(UserWithPasswordDTO newbie, AsyncCallback ac);
	void changePassword(UserWithPasswordDTO uwp, AsyncCallback ac);
	void details(String username, AsyncCallback ac);
	void emailPassword(String username, AsyncCallback ac);
}

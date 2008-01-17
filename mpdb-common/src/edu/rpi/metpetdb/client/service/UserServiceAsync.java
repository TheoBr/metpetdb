package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.UserWithPassword;

/** @see UserService */
public interface UserServiceAsync {
	void startSession(StartSessionRequest ssr, AsyncCallback ac);
	void resumeSession(AsyncCallback ac);
	void beginEditMyProfile(AsyncCallback ac);
	void registerNewUser(UserWithPassword newbie, AsyncCallback ac);
	void changePassword(UserWithPassword uwp, AsyncCallback ac);
	void details(String username, AsyncCallback ac);
	void emailPassword(String username, AsyncCallback ac);
}

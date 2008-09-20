package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.UserWithPassword;

/** @see UserService */
public interface UserServiceAsync {
	void startSession(StartSessionRequest ssr, AsyncCallback<User> ac);

	void resumeSession(AsyncCallback<ResumeSessionResponse> ac);

	void beginEditMyProfile(AsyncCallback<User> ac);

	void registerNewUser(UserWithPassword newbie, AsyncCallback<User> ac);

	void changePassword(UserWithPassword uwp, AsyncCallback<Object> ac);

	void details(String username, AsyncCallback<User> ac);

	void emailPassword(String username, AsyncCallback<Void> ac);

	void confirmUser(String confirmationCode, AsyncCallback<User> ac);
}

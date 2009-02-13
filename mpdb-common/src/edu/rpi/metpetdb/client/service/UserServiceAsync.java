package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.UnableToSendEmailException;
import edu.rpi.metpetdb.client.error.ValidationException;
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
	
	void allNames(AsyncCallback<Set<String>> ac);
	
	void save(User u, AsyncCallback<User> ac);
	
	void endSession(AsyncCallback<Void> ac);
	
	public void sendConfirmationCode(User u, AsyncCallback<Void> ac); 
}

package edu.rpi.metpetdb.client.service.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.User;

public interface AdminUserServiceAsync {

	void startSession(StartSessionRequest ssr, AsyncCallback<User> ac);
}

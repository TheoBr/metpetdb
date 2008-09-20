package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;

public interface MpDbGenericServiceAsync {
	void getBuildDate(AsyncCallback<String> ac);

	void getAutomaticLoginUser(AsyncCallback<User> ac);

	void regenerateConstraints(AsyncCallback<ResumeSessionResponse> ac);
}

package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;

public interface MpDbGenericServiceAsync {
	void getBuildDate(AsyncCallback<String> ac);

	void getAutomaticLoginUser(AsyncCallback<User> ac);

	void regenerateConstraints(AsyncCallback<ResumeSessionResponse> ac);
	
	void getCurrentTime(AsyncCallback<Long> ac);
	
	void  getStatistics(AsyncCallback<List<List>> ac);
}

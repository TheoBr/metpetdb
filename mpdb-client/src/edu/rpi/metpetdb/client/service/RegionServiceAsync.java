package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.error.MpDbException;

public interface RegionServiceAsync {
	void allNames(AsyncCallback<Set<String>> ac);
	
	void viewableNamesForUser(int userId, AsyncCallback<Set<String>> ac);
}

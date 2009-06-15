package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReferenceServiceAsync {
	void allReferences(AsyncCallback<Set<String>> ac);
	
	void viewableReferencesForUser(int userId, AsyncCallback<Set<String>> ac);
}

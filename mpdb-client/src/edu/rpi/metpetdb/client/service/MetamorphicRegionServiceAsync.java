package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetamorphicRegionServiceAsync {
	void allMetamorphicRegions(AsyncCallback<Set<String>> ac);
	
	void viewableNamesForUser(int userId, AsyncCallback<Set<String>> ac);
}

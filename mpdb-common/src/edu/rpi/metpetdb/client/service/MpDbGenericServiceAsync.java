package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.UserDTO;

public interface MpDbGenericServiceAsync {
	void getBuildDate(AsyncCallback ac);
	void getAutomaticLoginUser(AsyncCallback ac);
}
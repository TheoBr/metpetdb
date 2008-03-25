package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MpDbGenericServiceAsync {
	void getBuildDate(AsyncCallback ac);

	void getAutomaticLoginUser(AsyncCallback ac);

	void regenerateConstraints(AsyncCallback ac);
}
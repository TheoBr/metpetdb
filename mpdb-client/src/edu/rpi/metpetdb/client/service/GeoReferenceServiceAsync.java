package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeoReferenceServiceAsync {
	void getCount(AsyncCallback<Long> ac);
}

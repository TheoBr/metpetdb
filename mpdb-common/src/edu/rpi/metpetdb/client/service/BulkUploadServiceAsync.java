package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BulkUploadServiceAsync {
	void validate(String fileOnServer, AsyncCallback ac);

	void save(String fileOnServer, AsyncCallback ac);

}

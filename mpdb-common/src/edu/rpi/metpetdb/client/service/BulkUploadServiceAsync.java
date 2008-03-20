package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BulkUploadServiceAsync {
	void validate(final String fileOnServer, AsyncCallback<String> ac);

	void saveSamplesFromSpreadsheet(final String fileOnServer,
			AsyncCallback<String> ac);

}

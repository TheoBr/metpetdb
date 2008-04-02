package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BulkUploadServiceAsync {
	void saveSamplesFromSpreadsheet(final String fileOnServer,
			AsyncCallback<String> ac);

}

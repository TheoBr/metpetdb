package edu.rpi.metpetdb.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.error.ValidationException;

public interface BulkUploadServiceAsync {
	void saveSamplesFromSpreadsheet(final String fileOnServer,
			AsyncCallback<Map<Integer, ValidationException>> ac);

}

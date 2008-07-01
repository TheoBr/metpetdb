package edu.rpi.metpetdb.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.ValidationException;

public interface BulkUploadImagesServiceAsync extends RemoteService {
	void getHeaderMapping(final String fileOnServer,
			AsyncCallback<Map<Integer, String[]>> ac);
	void saveImagesFromZip(final String fileOnServer,
			AsyncCallback<Map<Integer, ValidationException>> ac);
}

package edu.rpi.metpetdb.client.service.bulk.upload;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;

public interface BulkUploadServiceAsync {
	void parser(final String fileOnServer, boolean save, AsyncCallback<BulkUploadResult> ac);

	void imageZipUpload(String spreadsheetFile, String imageFile,
			boolean save, AsyncCallback<BulkUploadResult> ac);
}

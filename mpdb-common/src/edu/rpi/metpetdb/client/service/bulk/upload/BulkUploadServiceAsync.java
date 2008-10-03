package edu.rpi.metpetdb.client.service.bulk.upload;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.BulkUploadResult;

public interface BulkUploadServiceAsync {
	void parser(final String fileOnServer, AsyncCallback<BulkUploadResult> ac);
	void commit(final String fileOnServer,
			AsyncCallback<Void> ac);
}

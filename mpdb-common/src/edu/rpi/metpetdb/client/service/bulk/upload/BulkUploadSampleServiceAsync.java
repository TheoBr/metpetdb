package edu.rpi.metpetdb.client.service.bulk.upload;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface BulkUploadSampleServiceAsync extends BulkUploadServiceAsync{
	void deleteOldFiles(AsyncCallback<Boolean> ac);
}

package edu.rpi.metpetdb.client.service.bulk.upload;

import java.io.FileNotFoundException;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;

/** Generic interface for bulk upload services */
public interface BulkUploadService extends RemoteService {
	BulkUploadResult parser(final String fileOnServer, boolean save)
			throws InvalidFormatException, LoginRequiredException, MpDbException;
	
	BulkUploadResult imageZipUpload(String spreadsheetFile, String imageFile, boolean save) throws InvalidFormatException, 
			LoginRequiredException, MpDbException;
}

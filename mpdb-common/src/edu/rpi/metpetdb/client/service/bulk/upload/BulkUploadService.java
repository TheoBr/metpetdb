package edu.rpi.metpetdb.client.service.bulk.upload;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;

/** Generic interface for bulk upload services */
public interface BulkUploadService extends RemoteService {
	BulkUploadResult parser(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException;
	void commit(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, ValidationException, DAOException;
}

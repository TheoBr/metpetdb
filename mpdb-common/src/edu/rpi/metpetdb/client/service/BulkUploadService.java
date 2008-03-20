package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;

public interface BulkUploadService extends RemoteService {
	String validate(final String fileOnServer) throws InvalidFormatException;

	String saveSamplesFromSpreadsheet(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException;
}

package edu.rpi.metpetdb.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;

public interface BulkUploadService extends RemoteService {
	Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException;
	Map<String, Integer[]> getAdditions(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException;
	Map<Integer, ValidationException> saveSamplesFromSpreadsheet(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, ValidationException, DAOException;
	Boolean deleteOldFiles();
}

package edu.rpi.metpetdb.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;

public interface BulkUploadService extends RemoteService {
	Map<Integer, ValidationException> saveSamplesFromSpreadsheet(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, SampleAlreadyExistsException,
			ValidationException;
}

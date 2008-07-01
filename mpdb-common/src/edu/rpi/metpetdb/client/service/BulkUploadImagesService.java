package edu.rpi.metpetdb.client.service;

import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;

public interface BulkUploadImagesService extends RemoteService {
	Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException;
	Map<Integer, ValidationException> saveImagesFromZip(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, ValidationException;
}

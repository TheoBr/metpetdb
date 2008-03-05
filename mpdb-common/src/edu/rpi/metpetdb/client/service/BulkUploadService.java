package edu.rpi.metpetdb.client.service;

import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.ui.Grid;

import edu.rpi.metpetdb.client.error.InvalidFormatException;

public interface BulkUploadService extends RemoteService {
	Grid validate(final String fileOnServer) throws IOException;

	String save(final String fileOnServer) throws IOException,
			InvalidFormatException;
}

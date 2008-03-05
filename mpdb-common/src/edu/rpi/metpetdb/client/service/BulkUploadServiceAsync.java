package edu.rpi.metpetdb.client.service;

import java.io.IOException;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

import edu.rpi.metpetdb.client.error.InvalidFormatException;

public interface BulkUploadServiceAsync {
	Grid validate(final String fileOnServer, AsyncCallback ac)
			throws IOException;

	String save(final String fileOnServer, AsyncCallback ac)
			throws IOException, InvalidFormatException;

}

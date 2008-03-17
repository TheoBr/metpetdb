package edu.rpi.metpetdb.client.service;

import java.util.Collection;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.SampleDTO;

public interface BulkUploadService extends RemoteService {
	Collection<SampleDTO> validate(final String fileOnServer);

	String save(final String fileOnServer) throws InvalidFormatException;
}

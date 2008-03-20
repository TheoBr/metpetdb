package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SubsampleService extends RemoteService {
	Results all(PaginationParameters parameters, final long sampleId);
	Results allWithImages(PaginationParameters parameters, final long sampleId);
	SubsampleDTO details(long id) throws NoSuchObjectException;
	SubsampleDTO save(final SubsampleDTO s) throws ValidationException,
			LoginRequiredException;
	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}

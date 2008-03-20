package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SampleService extends RemoteService {
	Results all(PaginationParameters parameters);
	Results allPublicSamples(final PaginationParameters p);
	Results allSamplesForUser(PaginationParameters parameters, long id);
	Results projectSamples(final PaginationParameters p, long id);
	SampleDTO details(long id) throws NoSuchObjectException;
	SampleDTO save(SampleDTO sample) throws SampleAlreadyExistsException,
			ValidationException, LoginRequiredException;
	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}

package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;

public interface SampleService extends RemoteService {
	Results all(PaginationParameters parameters);
	Results allPublicSamples(final PaginationParameters p);
	Results allSamplesForUser(PaginationParameters parameters, long id);
	Results projectSamples(final PaginationParameters p, long id);
	Sample details(long id) throws NoSuchObjectException;
	Sample saveSample(Sample sample) throws SampleAlreadyExistsException,
			ValidationException, LoginRequiredException;
	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}

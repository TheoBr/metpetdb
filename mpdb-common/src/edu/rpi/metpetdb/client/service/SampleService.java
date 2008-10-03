package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SampleService extends RemoteService {
	Results<Sample> all(PaginationParameters parameters);

	Results<Sample> allPublicSamples(final PaginationParameters p);

	Results<Sample> allSamplesForUser(PaginationParameters parameters, long id);

	Results<Sample> projectSamples(final PaginationParameters p, long id);

	Sample details(long id) throws DAOException;

	Sample save(Sample sample) throws DAOException, ValidationException,
			LoginRequiredException;
	
	Set<String> allCollectors();
	
	Set<String> allCountries();

	void delete(long id) throws DAOException, LoginRequiredException;
	
	void deleteAll(Collection<Long> ids) throws DAOException, LoginRequiredException;
}

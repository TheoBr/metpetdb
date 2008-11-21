package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SampleService extends RemoteService {
	Results<Sample> all(PaginationParameters parameters) throws DAOException;

	Results<Sample> allPublicSamples(final PaginationParameters p) throws DAOException;

	Results<Sample> allSamplesForUser(PaginationParameters parameters, long id) throws DAOException;

	Results<Sample> projectSamples(final PaginationParameters p, long id) throws DAOException;

	Sample details(long id) throws DAOException;

	Sample save(Sample sample) throws DAOException, ValidationException,
			LoginRequiredException;
	
	Set<String> allCollectors() throws DAOException;
	
	Set<String> allCountries() throws DAOException;
	
	List<Sample> allSamplesForUser(final long id) throws DAOException;

	void delete(long id) throws DAOException, LoginRequiredException;
	
	void deleteAll(Collection<Long> ids) throws DAOException, LoginRequiredException;
}

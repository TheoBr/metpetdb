package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SampleService extends RemoteService {
	Results<Sample> all(PaginationParameters parameters) throws MpDbException;

	Results<Sample> allPublicSamples(final PaginationParameters p) throws MpDbException;

	Results<Sample> allSamplesForUser(PaginationParameters parameters, long id) throws MpDbException;

	Results<Sample> projectSamples(final PaginationParameters p, long id) throws MpDbException;

	Sample details(long id) throws MpDbException;

	Sample save(Sample sample) throws MpDbException, ValidationException,
			LoginRequiredException;
	
	Set<String> allCollectors() throws MpDbException;
	
	Set<String> allCountries() throws MpDbException;
	
	List<Sample> allSamplesForUser(final long id) throws MpDbException;

	void delete(long id) throws MpDbException, LoginRequiredException;
	
	void deleteAll(Collection<Long> ids) throws MpDbException, LoginRequiredException;
}

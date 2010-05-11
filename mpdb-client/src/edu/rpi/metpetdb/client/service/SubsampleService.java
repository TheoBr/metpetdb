package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SubsampleService extends RemoteService {
	Results<Subsample> all(PaginationParameters parameters, final long sampleId) throws MpDbException;

	Results<Subsample> allWithImages(PaginationParameters parameters,
			final long sampleId) throws MpDbException;

	Subsample details(long id) throws MpDbException;
	
	Subsample detailsWithAnalyses(long id) throws MpDbException;
	
	List<Subsample> details(List<Long> ids) throws MpDbException;

	Subsample save(final Subsample s) throws MpDbException, ValidationException,
			LoginRequiredException;

	List<Subsample> all(final long sampleId) throws MpDbException;
	
	Map<Object,Boolean> allIdsForSample(long sampleId) throws MpDbException;

	MObject delete(long id) throws MpDbException, LoginRequiredException;
	
	void deleteAll(Collection<Long> ids) throws MpDbException, LoginRequiredException;
	
	void saveAll(Collection<Subsample> subsamples)
		throws ValidationException, LoginRequiredException, MpDbException;
	
	Map<Long, List<Subsample>> allFromManySamples(final Collection<Long> sampleIds) throws MpDbException;
}

package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SubsampleService extends RemoteService {
	Results<Subsample> all(PaginationParameters parameters, final long sampleId) throws MpDbException;

	Results<Subsample> allWithImages(PaginationParameters parameters,
			final long sampleId) throws MpDbException;

	Subsample details(long id) throws MpDbException;

	Subsample save(final Subsample s) throws MpDbException, ValidationException,
			LoginRequiredException;

	List<Subsample> all(final long sampleId) throws MpDbException;

	void delete(long id) throws MpDbException, LoginRequiredException;
}

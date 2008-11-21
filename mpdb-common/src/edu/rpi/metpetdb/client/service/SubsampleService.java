package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SubsampleService extends RemoteService {
	Results<Subsample> all(PaginationParameters parameters, final long sampleId) throws DAOException;

	Results<Subsample> allWithImages(PaginationParameters parameters,
			final long sampleId) throws DAOException;

	Subsample details(long id) throws DAOException;

	Subsample save(final Subsample s) throws DAOException, ValidationException,
			LoginRequiredException;

	List<Subsample> all(final long sampleId) throws DAOException;

	void delete(long id) throws DAOException, LoginRequiredException;
}

package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleComment;

public interface SampleCommentService extends RemoteService{
	SampleComment details(long id) throws DAOException;

	SampleComment save(final SampleComment s) throws DAOException, ValidationException,
			LoginRequiredException;

	List<SampleComment> all(final long sampleId);

	void delete(long id) throws DAOException, LoginRequiredException;
}

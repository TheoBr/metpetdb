package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.TimeExpiredException;
import edu.rpi.metpetdb.client.model.SampleComment;

public interface SampleCommentService extends RemoteService{
	SampleComment details(long id) throws MpDbException;

	SampleComment save(final SampleComment s) throws MpDbException, ValidationException,
			LoginRequiredException,TimeExpiredException;

	List<SampleComment> all(final long sampleId) throws MpDbException;

	void delete(long id) throws MpDbException, LoginRequiredException;
}

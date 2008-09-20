package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SampleService */
public interface SampleServiceAsync {
	void all(PaginationParameters parameters, AsyncCallback<Results<Sample>> ac);

	void allPublicSamples(PaginationParameters p,
			AsyncCallback<Results<Sample>> ac);

	void allSamplesForUser(PaginationParameters p, long id,
			AsyncCallback<Results<Sample>> ac);

	void projectSamples(PaginationParameters p, long id,
			AsyncCallback<Results<Sample>> ac);

	void details(long id, AsyncCallback<Sample> ac);

	void save(Sample sample, AsyncCallback<Sample> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

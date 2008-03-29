package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SampleService */
public interface SampleServiceAsync {
	void all(PaginationParameters parameters,
			AsyncCallback<Results<SampleDTO>> ac);

	void allPublicSamples(PaginationParameters p,
			AsyncCallback<Results<SampleDTO>> ac);

	void allSamplesForUser(PaginationParameters p, long id,
			AsyncCallback<Results<SampleDTO>> ac);

	void projectSamples(PaginationParameters p, long id,
			AsyncCallback<Results<SampleDTO>> ac);

	void details(long id, AsyncCallback<SampleDTO> ac);

	void save(SampleDTO sample, AsyncCallback<SampleDTO> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

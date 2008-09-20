package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SubsampleService */
public interface SubsampleServiceAsync {
	void all(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<Subsample>> ac);

	void allWithImages(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<Subsample>> ac);

	void details(long id, AsyncCallback<Subsample> ac);

	void all(long sampleId, AsyncCallback<List<Subsample>> ac);

	void save(final Subsample s, AsyncCallback<Subsample> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

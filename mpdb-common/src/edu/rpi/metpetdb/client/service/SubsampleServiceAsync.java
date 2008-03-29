package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SubsampleService */
public interface SubsampleServiceAsync {
	void all(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<SubsampleDTO>> ac);

	void allWithImages(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<SubsampleDTO>> ac);

	void details(long id, AsyncCallback<SubsampleDTO> ac);

	void save(final SubsampleDTO s, AsyncCallback<SubsampleDTO> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

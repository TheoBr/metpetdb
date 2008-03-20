package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

/** @see SubsampleService */
public interface SubsampleServiceAsync {
	void all(PaginationParameters pp, final long sampleId, AsyncCallback ac);
	void allWithImages(PaginationParameters pp, final long sampleId,
			AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void save(final SubsampleDTO s, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);
}

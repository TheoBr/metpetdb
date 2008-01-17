package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Subsample;

/** @see SubsampleService */
public interface SubsampleServiceAsync {
	void all(PaginationParameters pp, final long sampleId, AsyncCallback ac);
	void allWithImages(PaginationParameters pp, final long sampleId,
			AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveSubsample(final Subsample s, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);
}

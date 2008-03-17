package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SampleDTO;

/** @see SampleService */
public interface SampleServiceAsync {
	void all(PaginationParameters parameters, AsyncCallback<Results> ac);
	void allPublicSamples(PaginationParameters p, AsyncCallback<Results> ac);
	void allSamplesForUser(PaginationParameters p, long id, AsyncCallback<Results> ac);
	void projectSamples(PaginationParameters p, long id, AsyncCallback<Results> ac);
	void details(long id, AsyncCallback<SampleDTO> ac);
	void save(SampleDTO sample, AsyncCallback<SampleDTO> ac);
	void delete(long id, AsyncCallback<Object> ac);
}

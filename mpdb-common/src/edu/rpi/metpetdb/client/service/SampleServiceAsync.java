package edu.rpi.metpetdb.client.service;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;

/** @see SampleService */
public interface SampleServiceAsync {
	void all(PaginationParameters parameters, AsyncCallback ac);
	void allPublicSamples(PaginationParameters p, AsyncCallback ac);
	void allSamplesForUser(PaginationParameters p, long id, AsyncCallback ac);
	void projectSamples(PaginationParameters p, long id, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveSample(Sample sample, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);
}

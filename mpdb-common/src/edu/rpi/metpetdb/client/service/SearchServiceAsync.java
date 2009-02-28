package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SearchServiceAsync {

	void search(final PaginationParameters p, final SearchSample searchSamp, final User userSearching,
			AsyncCallback<Results<Sample>> ac);
	
	void rebuildSearchIndex(AsyncCallback<Void> ac);
	
	void setSessionSearchSample(final SearchSample searchSamp, AsyncCallback<Void> ac);
	
	void getSessionSearchSample(AsyncCallback<SearchSample> ac);
	
	void setSessionLastSearchedSearchSample(final SearchSample searchSamp, AsyncCallback<Void> ac);
	
	void getSessionLastSearchedSearchSample(AsyncCallback<SearchSample> ac);
	
	void setSessionLastSearchPagination(final PaginationParameters p, AsyncCallback<Void> ac);
	
	void getSessionLastSearchPagination(AsyncCallback<PaginationParameters> ac);
}

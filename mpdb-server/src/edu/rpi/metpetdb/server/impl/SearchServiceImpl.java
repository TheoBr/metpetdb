package edu.rpi.metpetdb.server.impl;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.security.NoPermissionsException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.server.search.SearchDb;
import edu.rpi.metpetdb.server.search.lucene.RegenerateIndices;

public class SearchServiceImpl extends SampleServiceImpl implements
		SearchService {
	private static final long serialVersionUID = 1L;

	public Results<Sample> search(final PaginationParameters p,
			SearchSample searchSamp, User userSearching) throws MpDbException {
		return (SearchDb.sampleSearch(p, searchSamp, userSearching));
	}

	public void rebuildSearchIndex() throws NoPermissionsException {
		RegenerateIndices.regenerate();
	}

	public void setSessionSearchSample(final SearchSample searchSamp) {
		setSearchSample(searchSamp);
	}

	public SearchSample getSessionSearchSample() {
		return getSearchSample();
	}

	public void setSessionLastSearchedSearchSample(final SearchSample searchSamp) {
		setLastSearchedSearchSample(searchSamp);
	}

	public SearchSample getSessionLastSearchedSearchSample() {
		return getLastSearchedSearchSample();
	}

	public void setSessionLastSearchPagination(final PaginationParameters p) {
		setLastSearchPagination(p);
	}

	public PaginationParameters getSessionLastSearchPagination() {
		return getLastSearchPagination();
	}

}

package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.security.NoPermissionsException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.server.search.SearchDb;
import edu.rpi.metpetdb.server.search.lucene.RegenerateIndices;

public class SearchServiceImpl extends SampleServiceImpl implements
		SearchService {
	private static final long serialVersionUID = 1L;

	public List<Sample> search(SearchSample searchSamp, User userSearching) throws NoPermissionsException {
		List<Sample> samples = (SearchDb
				.sampleSearch(searchSamp, userSearching));
		return samples;
	}

	public void rebuildSearchIndex() throws NoPermissionsException {
		RegenerateIndices.regenerate();
	}

}

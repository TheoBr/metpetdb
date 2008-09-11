package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.server.search.SearchDb;

public class SearchServiceImpl extends SampleServiceImpl implements
		SearchService {
	private static final long serialVersionUID = 1L;

	public List<SampleDTO> search(SearchSampleDTO searchSamp, UserDTO userSearching) {
		List<SampleDTO> samples = cloneBean(SearchDb.sampleSearch(searchSamp, userSearching));
		return samples;
	}

}

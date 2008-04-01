package edu.rpi.metpetdb.server.impl;

import java.util.LinkedList;
import java.util.List;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.search.SearchDb;

public class SearchServiceImpl extends SampleServiceImpl implements
		SearchService {
	private static final long serialVersionUID = 1L;

	public List<SampleDTO> search(SearchSampleDTO searchSamp) {
		List<Sample> samples = SearchDb.sampleSearch(searchSamp);
		List<SampleDTO> sampleDTOList = new LinkedList<SampleDTO>();
		for (Sample temp : samples) {
			sampleDTOList.add((SampleDTO) clone(temp));
		}
		return sampleDTOList;
	}

}

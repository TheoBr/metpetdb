package edu.rpi.metpetdb.client.service;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface SearchService extends RemoteService {
	
	Results<ChemicalAnalysis> chemicalAnalysisSearch(final PaginationParameters p, final SearchSample searchSamp, User userSearching) throws MpDbException;
	
	Results<Sample> sampleSearch(final PaginationParameters p, final SearchSample searchSamp, User userSearching) throws MpDbException;

	Map<Object,Boolean> sampleSearchIds(final SearchSample searchSamp, User userSearching) throws MpDbException;
	
	Map<Object,Boolean> chemicalAnalysisSearchIds(final SearchSample searchSamp, User userSearching) throws MpDbException;
	
	void rebuildSearchIndex() throws MpDbException;
	
	void setSessionSearchSample(final SearchSample searchSamp);
	
	SearchSample getSessionSearchSample();
	
	void setSessionLastSearchedSearchSample(final SearchSample searchSamp);
	
	SearchSample getSessionLastSearchedSearchSample();
	
	void setSessionLastSearchPagination(final PaginationParameters p);
	
	PaginationParameters getSessionLastSearchPagination();
	
	String createKML(final List<Sample> samples, final String baseURL);
	
	String createKMLMetamorphicRegions();
}

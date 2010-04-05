package edu.rpi.metpetdb.server.impl;

import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.security.NoPermissionsException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SearchService;
import edu.rpi.metpetdb.server.KMLCreater;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.search.SearchDb;
import edu.rpi.metpetdb.server.search.lucene.RegenerateIndices;

public class SearchServiceImpl extends MpDbServlet implements
		SearchService {
	private static final long serialVersionUID = 1L;

	public Results<ChemicalAnalysis> chemicalAnalysisSearch(
			final PaginationParameters p, SearchSample searchSamp,
			User userSearching) throws MpDbException {
		return (SearchDb.chemicalAnalysisSearch(p, searchSamp, userSearching, this.currentSession()));
	}

	public Results<Sample> sampleSearch(final PaginationParameters p,
			SearchSample searchSamp, User userSearching) throws MpDbException {
		return (SearchDb.sampleSearch(p, searchSamp, userSearching, this.currentSession()));
	}
	
	/**
	 * used for pagination tables to select all/public/private
	 */
	public Map<Object,Boolean> sampleSearchIds(SearchSample searchSamp, User userSearching) throws MpDbException {
		return (SearchDb.sampleSearchIds(searchSamp, userSearching, this.currentSession()));
	}
	/**
	 * used for pagination tables to select all/public/private
	 */
	public Map<Object,Boolean> chemicalAnalysisSearchIds(SearchSample searchSamp, User userSearching) throws MpDbException {
		return (SearchDb.chemicalAnalysisSearchIds(searchSamp, userSearching, this.currentSession()));
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

	public String createKML(final List<Sample> samples, final String baseURL) {
		return KMLCreater.createKML(samples, baseURL);
	}
	
	public String createKMLMetamorphicRegions(){
		return KMLCreater.createKMLMetamorphicRegions();
	}

}

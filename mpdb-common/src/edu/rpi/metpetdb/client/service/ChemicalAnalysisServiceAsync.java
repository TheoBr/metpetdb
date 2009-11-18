package edu.rpi.metpetdb.client.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId,
			AsyncCallback<Results<ChemicalAnalysis>> ac);

	void all(long subsampleId, AsyncCallback<List<ChemicalAnalysis>> ac);
	
	void allIdsForSubsample(long subsampleId, AsyncCallback<Map<Object,Boolean>> ac);

	void details(long id, AsyncCallback<ChemicalAnalysis> ac);
	
	void details(List<Integer> ids, AsyncCallback<List<ChemicalAnalysis>> ac);

	void save(ChemicalAnalysis chemicalAnalysis,
			AsyncCallback<ChemicalAnalysis> ac);
	
	void saveAll(Collection<ChemicalAnalysis> chemicalAnalyses, AsyncCallback<Void> ac);

	void delete(long id, AsyncCallback<Object> ac);
	
	void deleteAll(Collection<Integer> ids, AsyncCallback<Void> ac);
	
	/**
	 * Maps a subsample id to a list of chemical analyses
	 * @param subsampleIds
	 * @param ac
	 */
	void allFromManySubsamples(final Collection<Long> subsampleIds, AsyncCallback<Map<Long, List<ChemicalAnalysis>>> ac);

	void makePublic(ArrayList<ChemicalAnalysis> selectedChemicalAnalyses, AsyncCallback<Void> ac);
	
	void getPublicationCount(AsyncCallback<Long> ac);
	
	void getPublicCount(AsyncCallback<Long> ac);
	
	void getPrivateCount(AsyncCallback<Long> ac);
}

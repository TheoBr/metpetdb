package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId,
			AsyncCallback<Results<ChemicalAnalysis>> ac);

	void all(long subsampleId, AsyncCallback<List<ChemicalAnalysis>> ac);

	void details(long id, AsyncCallback<ChemicalAnalysis> ac);

	void save(ChemicalAnalysis chemicalAnalysis,
			AsyncCallback<ChemicalAnalysis> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

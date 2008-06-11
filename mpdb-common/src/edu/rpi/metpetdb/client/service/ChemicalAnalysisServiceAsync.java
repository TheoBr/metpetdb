package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId,
			AsyncCallback<Results<ChemicalAnalysisDTO>> ac);

	void all(long subsampleId, AsyncCallback<List<ChemicalAnalysisDTO>> ac);

	void details(long id, AsyncCallback<ChemicalAnalysisDTO> ac);

	void save(ChemicalAnalysisDTO chemicalAnalysis,
			AsyncCallback<ChemicalAnalysisDTO> ac);

	void delete(long id, AsyncCallback<Object> ac);
}

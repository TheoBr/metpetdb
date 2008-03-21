package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

public interface ChemicalAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId, AsyncCallback ac);

	void details(long id, AsyncCallback ac);

	void save(ChemicalAnalysisDTO chemicalAnalysis, AsyncCallback ac);

	void all(long subsampleId, AsyncCallback ac);

	void delete(long id, AsyncCallback ac);
}
package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

public interface MineralAnalysisServiceAsync {
	void all(PaginationParameters parameters, long subsampleId, AsyncCallback ac);
	void details(long id, AsyncCallback ac);
	void saveMineralAnalysis(ChemicalAnalysisDTO mineralAnalysis, AsyncCallback ac);
	void all(long subsampleId, AsyncCallback ac);
	void delete(long id, AsyncCallback ac);
}

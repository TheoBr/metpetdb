package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisService extends RemoteService {
	Results all(PaginationParameters parameters, final long subsampleId);

	ChemicalAnalysisDTO details(long id) throws NoSuchObjectException;

	ChemicalAnalysisDTO save(ChemicalAnalysisDTO chemicalAnalysis)
			throws ValidationException, LoginRequiredException;

	List<ChemicalAnalysisDTO> all(long subsampleId)
			throws NoSuchObjectException;

	void delete(long id) throws NoSuchObjectException, LoginRequiredException;
}

package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisService extends RemoteService {
	Results<ChemicalAnalysisDTO> all(PaginationParameters parameters,
			final long subsampleId);

	List<ChemicalAnalysisDTO> all(final long subsampleId);

	ChemicalAnalysisDTO details(long id) throws DAOException;

	ChemicalAnalysisDTO save(ChemicalAnalysisDTO chemicalAnalysis)
			throws ValidationException, LoginRequiredException, DAOException;

	void delete(long id) throws DAOException, LoginRequiredException;
}

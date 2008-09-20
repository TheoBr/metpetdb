package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisService extends RemoteService {
	Results<ChemicalAnalysis> all(PaginationParameters parameters,
			final long subsampleId);

	List<ChemicalAnalysis> all(final long subsampleId);

	ChemicalAnalysis details(long id) throws DAOException;

	ChemicalAnalysis save(ChemicalAnalysis chemicalAnalysis)
			throws ValidationException, LoginRequiredException, DAOException;

	void delete(long id) throws DAOException, LoginRequiredException;
}

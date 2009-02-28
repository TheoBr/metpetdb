package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public interface ChemicalAnalysisService extends RemoteService {
	Results<ChemicalAnalysis> all(PaginationParameters parameters,
			final long subsampleId) throws MpDbException;

	List<ChemicalAnalysis> all(final long subsampleId) throws MpDbException;

	ChemicalAnalysis details(long id) throws MpDbException;

	ChemicalAnalysis save(ChemicalAnalysis chemicalAnalysis)
			throws ValidationException, LoginRequiredException, MpDbException;

	void saveAll(Collection<ChemicalAnalysis> chemicalAnalyses)
			throws ValidationException, LoginRequiredException, MpDbException;

	void delete(long id) throws MpDbException, LoginRequiredException;
}

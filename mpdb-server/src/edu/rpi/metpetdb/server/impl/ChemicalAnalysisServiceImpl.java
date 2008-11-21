package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;

public class ChemicalAnalysisServiceImpl extends MpDbServlet implements
		ChemicalAnalysisService {
	private static final long serialVersionUID = 1L;

	public ChemicalAnalysis details(long id) throws DAOException {
		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId(new Long(id).intValue());
		ca = (new ChemicalAnalysisDAO(this.currentSession())).fill(ca);
		return (ca);
	}

	public Results<ChemicalAnalysis> all(PaginationParameters parameters,
			final long subsampleId) throws DAOException {
		return (new ChemicalAnalysisDAO(this.currentSession())).getAll(
				parameters, subsampleId);
	}

	public List<ChemicalAnalysis> all(long subsampleId) throws DAOException {
		List<ChemicalAnalysis> l = (new ChemicalAnalysisDAO(this
				.currentSession())).getAll(subsampleId);
		return l;
	}

	public ChemicalAnalysis save(ChemicalAnalysis ca)
			throws ValidationException, LoginRequiredException, DAOException {
		doc.validate(ca);
		ca = (new ChemicalAnalysisDAO(this.currentSession())).save(ca);
		commit();
		return (ca);
	}
	
	public void saveAll(Collection<ChemicalAnalysis> chemicalAnalyses)
	throws ValidationException, LoginRequiredException, DAOException {
			final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());
			for(ChemicalAnalysis ca : chemicalAnalyses) {
				doc.validate(ca);
				dao.save(ca);
			}
			commit();
	}

	public void delete(long id) throws DAOException, LoginRequiredException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());

		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId((new Long(id)).intValue());
		ca = dao.fill(ca);

		dao.delete(ca);
		commit();
	}
}

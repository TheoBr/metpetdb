package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class ChemicalAnalysisServiceImpl extends MpDbServlet implements
		ChemicalAnalysisService {
	private static final long serialVersionUID = 1L;

	public ChemicalAnalysis details(long id) throws MpDbException {
		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId(new Long(id).intValue());
		ca = (new ChemicalAnalysisDAO(this.currentSession())).fill(ca);
		return (ca);
	}

	public Results<ChemicalAnalysis> all(PaginationParameters parameters,
			final long subsampleId) throws MpDbException {
		return (new ChemicalAnalysisDAO(this.currentSession())).getAll(
				parameters, subsampleId, currentUserIdIfExists());
	}

	public List<ChemicalAnalysis> all(long subsampleId) throws MpDbException {
		List<ChemicalAnalysis> l = (new ChemicalAnalysisDAO(this
				.currentSession())).getAll(subsampleId);
		return l;
	}
	
	public List<ChemicalAnalysis> allFromManySubsamples(final Collection<Long> subsampleIds) throws MpDbException {
		final List<ChemicalAnalysis> l = new ArrayList<ChemicalAnalysis>();
		for (Long id : subsampleIds){
			l.addAll((new ChemicalAnalysisDAO(this
					.currentSession())).getAll(id));
		}
		return (l);
	}

	public ChemicalAnalysis save(ChemicalAnalysis ca)
			throws ValidationException, LoginRequiredException, MpDbException {
		doc.validate(ca);
		ca = (new ChemicalAnalysisDAO(this.currentSession())).save(ca);
		commit();
		return (ca);
	}
	
	public void saveAll(Collection<ChemicalAnalysis> chemicalAnalyses)
	throws ValidationException, LoginRequiredException, MpDbException {
			final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());
			for(ChemicalAnalysis ca : chemicalAnalyses) {
				doc.validate(ca);
				dao.save(ca);
			}
			commit();
	}

	public void delete(long id) throws MpDbException, LoginRequiredException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());

		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId((new Long(id)).intValue());
		ca = dao.fill(ca);

		dao.delete(ca);
		commit();
	}
}

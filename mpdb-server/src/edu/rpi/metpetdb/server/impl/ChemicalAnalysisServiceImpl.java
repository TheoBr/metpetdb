package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
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
	
	public List<ChemicalAnalysis> details(List<Integer> ids) throws MpDbException {
		ArrayList<ChemicalAnalysis> cas = new ArrayList<ChemicalAnalysis>();
		for (Integer id : ids){
			ChemicalAnalysis ca = new ChemicalAnalysis();
			ca.setId(id);
			ca = (new ChemicalAnalysisDAO(this.currentSession())).fill(ca);
			cas.add(ca);
			
		}
		return cas;
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
	
	/**
	 * used for pagination tables to select all/public/private
	 */
	public Map<Object,Boolean> allIdsForSubsample(long subsampleId) throws MpDbException {
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Object[] row : (new ChemicalAnalysisDAO(this.currentSession())).getAllIdsForSubsample(new Long(subsampleId).intValue())){
			ids.put(row[0],(Boolean) row[1]);
		}
		return ids;
	}

	public Map<Long, List<ChemicalAnalysis>> allFromManySubsamples(
			final Collection<Long> subsampleIds) throws MpDbException {
		final Map<Long, List<ChemicalAnalysis>> cas = new HashMap<Long, List<ChemicalAnalysis>>();
		for (Long id : subsampleIds) {
			cas.put(id, (new ChemicalAnalysisDAO(this.currentSession()))
					.getAll(id));
		}
		return cas;
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
		final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this
				.currentSession());
		for (ChemicalAnalysis ca : chemicalAnalyses) {
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

	public void makePublic(ArrayList<ChemicalAnalysis> chemicalAnalyses)
			throws ValidationException, LoginRequiredException, MpDbException {
		final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this
				.currentSession());
		for(ChemicalAnalysis ca : chemicalAnalyses) {
			doc.validate(ca);
			dao.makePublic(ca);
		}
		commit();
	}

	public void deleteAll(Collection<Integer> ids) throws MpDbException,
			LoginRequiredException {
		final Iterator<Integer> itr = ids.iterator();
		while(itr.hasNext()){
			deleteImpl(itr.next().intValue());
		}
		commit();
	}
	
	private void deleteImpl(int id) throws MpDbException, LoginRequiredException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());
		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId(id);
		ca = dao.fill(ca);
		dao.delete(ca);
	}
	
	public long getPublicCount() {
		return new ChemicalAnalysisDAO(this.currentSession()).getPublicCount();
	}
	
	public long getPrivateCount() {
		return new ChemicalAnalysisDAO(this.currentSession()).getPrivateCount();
	}
	
	public long getPublicationCount() {
		return new ChemicalAnalysisDAO(this.currentSession()).getPublicationCount();
	}
}

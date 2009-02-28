package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.ChemicalAnalysisNotFoundException;
import edu.rpi.metpetdb.client.error.dao.ReferenceNotFoundException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ChemicalAnalysisDAO extends MpDbDAO<ChemicalAnalysis> {

	public ChemicalAnalysisDAO(Session session) {
		super(session);
	}

	@Override
	public ChemicalAnalysis delete(ChemicalAnalysis inst) throws MpDbException {
		_delete(inst);
		return null;
	}

	@Override
	public ChemicalAnalysis fill(ChemicalAnalysis inst) throws MpDbException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("ChemicalAnalysis.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (ChemicalAnalysis) getResult(q);
		}
		
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("ChemicalAnalysis.bySubsampleIdbySpotId");
			q.setLong("id", inst.getSubsample().getId());
			q.setString("spotId", inst.getSpotId());
			if (getResult(q) != null)
				return (ChemicalAnalysis) getResult(q);
		}

		throw new ChemicalAnalysisNotFoundException();
	}

	public ChemicalAnalysis populate(ChemicalAnalysis inst) throws MpDbException {
		// If we can, let's try to fill the subsample
		if (inst.getSubsample() != null) {
			inst.setSubsample((new SubsampleDAO(sess))
					.fill(inst.getSubsample()));
		}

		return inst;
	}

	@Override
	public ChemicalAnalysis save(ChemicalAnalysis ca) throws MpDbException {
		if (ca.getReference() != null) {
			try {
			ca.setReference((new ReferenceDAO(sess)).fill(ca.getReference()));
			} catch (ReferenceNotFoundException e) {
				//ignore the reference if we don't find it, it will get automatically added by hibernate
			}
		}
		ca.setMineral((new MineralDAO(sess)).fill(ca.getMineral()));
		ca.setSubsample((new SubsampleDAO(sess)).fill(ca.getSubsample()));
		ca = _save(ca);
		return ca;
	}

	public List<ChemicalAnalysis> getAll(final long subsampleId, long userId) throws MpDbException {
		sess.enableFilter("chemicalAnalysisPublicOrUser").setParameter("userId", userId);
		final Query q = namedQuery("ChemicalAnalysis.bySubsampleId");
		q.setParameter("subsampleId", subsampleId);
		final List<ChemicalAnalysis> l = (List<ChemicalAnalysis>) getResults(q);

		return l;
	}

	public Results<ChemicalAnalysis> getAll(final PaginationParameters p,
			final long subsampleId, long userId) throws MpDbException{
		sess.enableFilter("chemicalAnalysisPublicOrUser").setParameter("userId", userId);
		final Query sizeQ = sizeQuery("ChemicalAnalysis.bySubsampleId",
				subsampleId);
		final Query pageQ = pageQuery("ChemicalAnalysis.bySubsampleId", p,
				subsampleId);
		return getChemicalAnalyses(sizeQ, pageQ);
	}

	private Results<ChemicalAnalysis> getChemicalAnalyses(Query sizeQuery,
			Query pageQuery) throws MpDbException {
		final List<ChemicalAnalysis> l = (List<ChemicalAnalysis>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();

		return new Results<ChemicalAnalysis>(size, l);
	}
}

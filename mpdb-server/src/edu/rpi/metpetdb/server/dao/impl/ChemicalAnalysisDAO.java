package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.ChemicalAnalysisNotFoundException;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;

public class ChemicalAnalysisDAO extends MpDbDAO<ChemicalAnalysis> {

	public ChemicalAnalysisDAO(Session session) {
		super(session);
	}

	@Override
	public ChemicalAnalysis delete(ChemicalAnalysis inst) throws DAOException {
		_delete(inst);
		return null;
	}

	@Override
	public ChemicalAnalysis fill(ChemicalAnalysis inst) throws DAOException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("ChemicalAnalysis.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (ChemicalAnalysis) q.uniqueResult();
		}

		throw new ChemicalAnalysisNotFoundException();
	}

	public ChemicalAnalysis populate(ChemicalAnalysis inst) throws DAOException {
		// If we can, let's try to fill the subsample
		if (inst.getSubsample() != null) {
			inst.setSubsample((new SubsampleDAO(sess))
					.fill(inst.getSubsample()));
		}

		return inst;
	}

	@Override
	public ChemicalAnalysis save(ChemicalAnalysis ca) throws DAOException {
		if (ca.getReference() != null)
			ca.setReference((new ReferenceDAO(sess)).fill(ca.getReference()));
		ca.setMineral((new MineralDAO(sess)).fill(ca.getMineral()));
		ca.setSubsample((new SubsampleDAO(sess)).fill(ca.getSubsample()));
		// TODO: This is where we can catch the exception is the subsample
		// doesn't exit, and then create one

		ca = _save(ca);
		return ca;
	}

	public List<ChemicalAnalysis> getAll(final long subsampleId) {
		final Query q = namedQuery("ChemicalAnalysis.bySubsampleId");
		q.setParameter("subsampleId", subsampleId);
		final List<ChemicalAnalysis> l = q.list();

		return l;
	}

	public ResultsFromDAO<ChemicalAnalysis> getAll(
			final PaginationParameters p, final long subsampleId) {

		final Query sizeQ = sizeQuery("ChemicalAnalysis.bySubsampleId",
				subsampleId);
		final Query pageQ = pageQuery("ChemicalAnalysis.bySubsampleId", p,
				subsampleId);
		return getChemicalAnalyses(sizeQ, pageQ);
	}

	private ResultsFromDAO<ChemicalAnalysis> getChemicalAnalyses(
			Query sizeQuery, Query pageQuery) {
		final List<ChemicalAnalysis> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();

		return new ResultsFromDAO<ChemicalAnalysis>(size, l);
	}
}

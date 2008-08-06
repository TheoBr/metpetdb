package edu.rpi.metpetdb.server.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.model.Subsample;

public class SubsampleDAO extends MpDbDAO<Subsample> {

	public SubsampleDAO(Session session) {
		super(session);
	}

	@Override
	public Subsample delete(Subsample inst) throws DAOException {
		_delete(inst);
		return null;
	}

	@Override
	public Subsample fill(Subsample inst) throws DAOException {
		// By ID
		if (inst.getId() > 0) {
			final Query q = namedQuery("Subsample.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (Subsample) q.uniqueResult();
		}

		// By Sample and Name
		if (inst.getSample() != null && inst.getName() != null) {
			inst.setSample((new SampleDAO(sess)).fill(inst.getSample()));
			final Query q = namedQuery("Subsample.bySample.byName");
			q.setParameter("id", inst.getSample().getId());
			q.setParameter("name", inst.getName());
			if (q.uniqueResult() != null)
				return (Subsample) q.uniqueResult();
		}

		throw new SubsampleNotFoundException();
	}

	@Override
	public Subsample save(Subsample inst) throws DAOException {
		return _save(inst);
	}

	public int countBySampleId(long sampleId) {
		Query q = sizeQuery("Subsample.bySampleId", sampleId);
		return ((Number) q.uniqueResult()).intValue();
	}

	public List<Subsample> getAllBySampleID(final long sampleId) {
		final Query q = namedQuery("Subsample.bySampleId");
		q.setParameter("sampleId", sampleId);
		final List<Subsample> l = q.list();

		if (l != null && l.size() > 0) {
			final Iterator<Subsample> itr = l.iterator();
			while (itr.hasNext()) {
				final Subsample s = itr.next();

				s.setImageCount((new ImageDAO(sess).countBySubsampleId(s
						.getId())));
				s.setAnalysisCount((new ChemicalAnalysisDAO(sess)
						.countBySubsampleId(s.getId())));
			}
		}
		return l;
	}

	public ResultsFromDAO<Subsample> getAllWithImagesBySampleID(
			final PaginationParameters p, final long sampleId) {
		final Query sizeQ = sizeQuery("Subsample.allWithImages", sampleId);
		final Query pageQ = pageQuery("Subsample.allWithImages", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	public ResultsFromDAO<Subsample> getAllBySampleID(
			final PaginationParameters p, final long sampleId) {
		final Query sizeQ = sizeQuery("Subsample.all", sampleId);
		final Query pageQ = pageQuery("Subsample.all", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	private ResultsFromDAO<Subsample> getSubsamples(Query sizeQuery,
			Query pageQuery) {
		final List<Subsample> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();

		if (size > 0) {
			final Iterator<Subsample> itr = l.iterator();
			while (itr.hasNext()) {
				final Subsample s = itr.next();

				s.setImageCount((new ImageDAO(sess).countBySubsampleId(s
						.getId())));
				s.setAnalysisCount((new ChemicalAnalysisDAO(sess)
						.countBySubsampleId(s.getId())));
			}
		}
		return new ResultsFromDAO<Subsample>(size, l);
	}
}

package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

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
		inst.setSubsampleType(new SubsampleTypeDAO(sess).fill(inst
				.getSubsampleType()));
		return _save(inst);
	}

	public List<Subsample> getAllBySampleID(final long sampleId) {
		final Query q = namedQuery("Subsample.bySampleId");
		q.setParameter("sampleId", sampleId);
		final List<Subsample> l = q.list();
		return l;
	}

	public Results<Subsample> getAllWithImagesBySampleID(
			final PaginationParameters p, final long sampleId) {
		final Query sizeQ = sizeQuery("Subsample.allWithImages", sampleId);
		final Query pageQ = pageQuery("Subsample.allWithImages", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	public Results<Subsample> getAllBySampleID(final PaginationParameters p,
			final long sampleId) {
		final Query sizeQ = sizeQuery("Subsample.all", sampleId);
		final Query pageQ = pageQuery("Subsample.all", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	private Results<Subsample> getSubsamples(Query sizeQuery, Query pageQuery) {
		final List<Subsample> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();
		return new Results<Subsample>(size, l);
	}
}

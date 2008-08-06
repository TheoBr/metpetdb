package edu.rpi.metpetdb.server.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SampleNotFoundException;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.model.Sample;

public class SampleDAO extends MpDbDAO<Sample> {

	public SampleDAO(Session session) {
		super(session);
	}

	@Override
	public Sample delete(Sample inst) throws DAOException {
		_delete(inst);
		return null;
	}

	@Override
	public Sample fill(Sample inst) throws DAOException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("Sample.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (Sample) q.uniqueResult();
		}

		// Use User,Alias
		if (inst.getOwner() != null && inst.getOwner().getId() > 0
				&& inst.getAlias() != null) {
			final Query q = namedQuery("Sample.byUser.byAlias");
			q.setLong("id", inst.getOwner().getId());
			q.setString("alias", inst.getAlias());
			if (q.uniqueResult() != null)
				return (Sample) q.uniqueResult();
		}

		throw new SampleNotFoundException();
	}

	@Override
	public Sample save(Sample s) throws DAOException {
		// Fill subcomponents
		s.setRegions((new RegionDAO(sess)).fill(s.getRegions()));
		s.setMetamorphicGrades((new MetamorphicGradeDAO(sess)).fill(s
				.getMetamorphicGrades()));
		s.setReferences((new ReferenceDAO(sess)).fill(s.getReferences()));
		s.setMinerals(((new SampleMineralDAO(sess))).fill(s.getMinerals()));

		s = _save(s);
		return s;
	}

	public ResultsFromDAO<Sample> getProjectSamples(
			final PaginationParameters p, long id) {
		final Query sizeQ = sizeQuery("sample.forProject", id);
		final Query pageQ = pageQuery("sample.forProject", p, id);
		return getSamples(sizeQ, pageQ);
	}

	public ResultsFromDAO<Sample> getAllPublicSamples(
			final PaginationParameters p) {
		final Query sizeQ = sizeQuery("Sample.allPublic");
		final Query pageQ = pageQuery("Sample.allPublic", p);
		return getSamples(sizeQ, pageQ);
	}

	public ResultsFromDAO<Sample> getForUser(final PaginationParameters p,
			long id) {
		final Query sizeQ = sizeQuery("Sample.forUser", id);
		final Query pageQ = pageQuery("Sample.forUser", p, id);
		return getSamples(sizeQ, pageQ);
	}

	public ResultsFromDAO<Sample> getAll(final PaginationParameters p) {
		final Query sizeQ = sizeQuery("Sample.all");
		final Query pageQ = pageQuery("Sample.all", p);
		return getSamples(sizeQ, pageQ);
	}

	private ResultsFromDAO<Sample> getSamples(Query sizeQuery, Query pageQuery) {
		final List<Sample> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();

		if (size > 0) {
			final Iterator<Sample> itr = l.iterator();
			while (itr.hasNext()) {
				final Sample s = itr.next();
				s.setSubsampleCount((new SubsampleDAO(sess)).countBySampleId(s
						.getId()));
			}
		}
		return new ResultsFromDAO<Sample>(size, l);
	}
}

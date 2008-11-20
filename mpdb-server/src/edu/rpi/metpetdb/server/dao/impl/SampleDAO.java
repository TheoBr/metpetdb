package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SampleNotFoundException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

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

	public Set<SampleMineral> replaceMinerals(final Set<SampleMineral> s) {
		final Set<SampleMineral> m = (new SampleMineralDAO(sess)).fill(s);
		return m;
	}

	public void replaceTransientObjects(Sample s) throws DAOException {
		// Fill subcomponents
		s.setRegions((new RegionDAO(sess)).fill(s.getRegions()));
		s.setMetamorphicGrades((new MetamorphicGradeDAO(sess)).fill(s
				.getMetamorphicGrades()));
		s.setReferences((new ReferenceDAO(sess)).fill(s.getReferences()));
		s.setMinerals(((new SampleMineralDAO(sess))).fill(s.getMinerals()));
		s.setRockType(new RockTypeDAO(sess).fill(s.getRockType()));
	}

	@Override
	public Sample save(Sample s) throws DAOException {
		replaceTransientObjects(s);

		s = _save(s);
		return s;
	}

	public Results<Sample> getProjectSamples(final PaginationParameters p,
			long id) {
		final Query sizeQ = sizeQuery("Sample.forProject", id);
		final Query pageQ = pageQuery("Sample.forProject", p, id);
		return getSamples(sizeQ, pageQ);
	}

	public Results<Sample> getAllPublicSamples(final PaginationParameters p) {
		final Query sizeQ = sizeQuery("Sample.allPublic");
		final Query pageQ = pageQuery("Sample.allPublic", p);
		return getSamples(sizeQ, pageQ);
	}

	public Results<Sample> getAll(final PaginationParameters p) {
		final Query sizeQ = sizeQuery("Sample.all");
		final Query pageQ = pageQuery("Sample.all", p);
		return getSamples(sizeQ, pageQ);
	}
	
	public List<Sample> getAll() {
		final Query q = namedQuery("Sample.all/id");
		return q.list();
	}

	private Results<Sample> getSamples(Query sizeQuery, Query pageQuery) {
		final List<Sample> l = pageQuery.list();
		final int size = ((Number) sizeQuery.uniqueResult()).intValue();
		return new Results<Sample>(size, l);
	}
	
	public Object[] allCollectors() {
		final Query q = namedQuery("Sample.Collectors/Collector");
			return	q.list().toArray();
	}
	
	public Object[] allCountries() {
		final Query q = namedQuery("Sample.Countries/Countries");
		return	q.list().toArray();
	}
}

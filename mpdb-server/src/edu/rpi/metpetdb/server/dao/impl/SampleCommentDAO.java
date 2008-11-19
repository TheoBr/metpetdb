package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class SampleCommentDAO extends MpDbDAO<SampleComment>{
	public SampleCommentDAO(Session session) {
		super(session);
	}

	@Override
	public SampleComment delete(SampleComment inst) throws DAOException {
		_delete(inst);
		return null;
	}

	@Override
	public SampleComment fill(SampleComment inst) throws DAOException {
		// By ID
		if (inst.getId() > 0) {
			final Query q = namedQuery("SampleComment.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (SampleComment) q.uniqueResult();
		}

		// By Sample and Name
		if (inst.getSample() != null) {
			inst.setSample((new SampleDAO(sess)).fill(inst.getSample()));
			final Query q = namedQuery("SampleComment.bySampleId");
			q.setParameter("id", inst.getSample().getId());
			if (q.uniqueResult() != null)
				return (SampleComment) q.uniqueResult();
		}

		throw new SubsampleNotFoundException();
	}

	@Override
	public SampleComment save(SampleComment inst) throws DAOException {
		return _save(inst);
	}

	public List<SampleComment> getAllBySampleID(final long sampleId) {
		final Query q = namedQuery("Subsample.bySampleId");
		q.setParameter("sampleId", sampleId);
		final List<SampleComment> l = q.list();
		return l;
	}
}

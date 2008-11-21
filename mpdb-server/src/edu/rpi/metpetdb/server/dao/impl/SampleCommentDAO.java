package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.SampleCommentNotFoundException;
import edu.rpi.metpetdb.client.model.SampleComment;
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
		// Use Region Name
		final org.hibernate.Query sampleComments = namedQuery("SampleComment.byId");
		sampleComments.setString("id", String.valueOf(inst.getId()));
		if (sampleComments.uniqueResult() != null)
			return (SampleComment) sampleComments.uniqueResult();

		throw new SampleCommentNotFoundException();
	}

	@Override
	public SampleComment save(SampleComment inst) throws DAOException {
		return _save(inst);
	}

	public List<SampleComment> getAllBySampleID(final long sampleId) throws DAOException{
		final Query q = namedQuery("SampleComment.bySampleId");
		q.setParameter("sampleId", sampleId);
		final List<SampleComment> l = (List<SampleComment>) getResults(q);
		return l;
	}
}

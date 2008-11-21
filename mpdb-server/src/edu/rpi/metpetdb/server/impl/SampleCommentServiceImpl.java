package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.service.SampleCommentService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SampleCommentDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class SampleCommentServiceImpl  extends MpDbServlet implements SampleCommentService {
	private static final long serialVersionUID = 1L;

	public List<SampleComment> all(final long sampleId) throws DAOException {
		final List<SampleComment> l = (new SampleCommentDAO(this.currentSession())
				.getAllBySampleID(sampleId));
		return (l);
	}
	
	public SampleComment details(final long id) throws DAOException {
		SampleComment s = new SampleComment();
		s.setId(id);

		s = (new SampleCommentDAO(this.currentSession())).fill(s);
		return s;
	}
	
	public SampleComment save(SampleComment sampleComment) throws DAOException,
			ValidationException, LoginRequiredException {
		doc.validate(sampleComment);
		sampleComment = (new SampleCommentDAO(this.currentSession())).save(sampleComment);

		commit();
		return (sampleComment);
	}

	public void delete(long id) throws DAOException, LoginRequiredException {
		final SampleCommentDAO dao = new SampleCommentDAO(this.currentSession());
		SampleComment s = new SampleComment();
		s.setId(id);
		s = dao.fill(s);
		dao.delete(s);
		commit();
	}
}

package edu.rpi.metpetdb.server.impl;

import java.util.Date;
import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.validation.TimeExpiredException;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.service.SampleCommentService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SampleCommentDAO;

public class SampleCommentServiceImpl  extends MpDbServlet implements SampleCommentService {
	private static final long serialVersionUID = 1L;

	public List<SampleComment> all(final long sampleId) throws MpDbException {
		final List<SampleComment> l = (new SampleCommentDAO(this.currentSession())
				.getAllBySampleID(sampleId));
		return (l);
	}
	
	public SampleComment details(final long id) throws MpDbException {
		SampleComment s = new SampleComment();
		s.setId(id);

		s = (new SampleCommentDAO(this.currentSession())).fill(s);
		return s;
	}
	
	public SampleComment save(SampleComment sampleComment) throws MpDbException,
			ValidationException, LoginRequiredException, TimeExpiredException {
		doc.validate(sampleComment);
		final Date now = new Date();
		if (now.getTime()>=(sampleComment.getDateAdded().getTime()+SampleComment.EDIT_TIMER)){
			throw new TimeExpiredException();
		}
		sampleComment = (new SampleCommentDAO(this.currentSession())).save(sampleComment);
		commit();
		return (sampleComment);
	}

	public MObject delete(long id) throws MpDbException, LoginRequiredException {
		final SampleCommentDAO dao = new SampleCommentDAO(this.currentSession());
		SampleComment s = new SampleComment();
		s.setId(id);
		s = dao.fill(s);
		dao.delete(s);
		commit();
		
		return null;
	}
}

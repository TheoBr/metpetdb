package edu.rpi.metpetdb.server.impl;

import java.util.Collection;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class SampleServiceImpl extends MpDbServlet implements SampleService {
	private static final long serialVersionUID = 1L;

	public Results<Sample> all(final PaginationParameters p) {
		return (new SampleDAO(this.currentSession())).getAll(p);
	}

	public Results<Sample> allSamplesForUser(final PaginationParameters p,
			long id) {
		return (new SampleDAO(this.currentSession())).getForUser(p, id);
	}
	public Results<Sample> allPublicSamples(final PaginationParameters p) {
		return (new SampleDAO(this.currentSession())).getAllPublicSamples(p);
	}

	public Results<Sample> projectSamples(final PaginationParameters p, long id) {
		return (new SampleDAO(this.currentSession()).getProjectSamples(p, id));
	}

	public Sample details(final long id) throws DAOException {
		Sample s = new Sample();
		s.setId(id);

		s = (new SampleDAO(this.currentSession())).fill(s);
		return s;
	}

	protected void save(final Collection<Sample> samples)
			throws ValidationException, LoginRequiredException, DAOException {
		for (Sample sample : samples) {
			doc.validate(sample);
			Sample s = (sample);
			s = (new SampleDAO(this.currentSession())).save(s);
		}
		commit();
	}

	public Sample save(Sample sample) throws DAOException, ValidationException,
			LoginRequiredException {
		doc.validate(sample);
		if (sample.getOwner() == null)
			throw new LoginRequiredException();
		if (sample.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");
		Sample s = (sample);

		s = (new SampleDAO(this.currentSession())).save(s);

		commit();
		return (s);

	}

	public void delete(long id) throws DAOException, LoginRequiredException {
		SampleDAO dao = new SampleDAO(this.currentSession());
		Sample s = new Sample();
		s.setId(id);
		s = dao.fill(s);

		if (s.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");
		else if (s.isPublicData())
			throw new SecurityException("Cannot modify public samples");
		dao.delete(s);
		commit();
	}
}

package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.model.Sample;

public class SampleServiceImpl extends MpDbServlet implements SampleService {
	private static final long serialVersionUID = 1L;

	public Results<SampleDTO> all(final PaginationParameters p) {
		final ResultsFromDAO<Sample> l = (new SampleDAO(this.currentSession()))
				.getAll(p);
		final List<SampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SampleDTO>(l.getCount(), lDTO);
	}

	public Results<SampleDTO> allSamplesForUser(final PaginationParameters p,
			long id) {
		final ResultsFromDAO<Sample> l = (new SampleDAO(this.currentSession()))
				.getForUser(p, id);
		final List<SampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SampleDTO>(l.getCount(), lDTO);

	}
	public Results<SampleDTO> allPublicSamples(final PaginationParameters p) {
		final ResultsFromDAO<Sample> l = (new SampleDAO(this.currentSession()))
				.getAllPublicSamples(p);
		final List<SampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SampleDTO>(l.getCount(), lDTO);
	}

	public Results<SampleDTO> projectSamples(final PaginationParameters p,
			long id) {
		final ResultsFromDAO<Sample> l = (new SampleDAO(this.currentSession())
				.getProjectSamples(p, id));
		final List<SampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SampleDTO>(l.getCount(), lDTO);
	}

	public SampleDTO details(final long id) throws DAOException {
		Sample s = new Sample();
		s.setId(id);

		s = (new SampleDAO(this.currentSession())).fill(s);
		return (SampleDTO) clone(s);
	}

	protected void save(final Collection<SampleDTO> samples)
			throws ValidationException, LoginRequiredException, DAOException {
		for (SampleDTO sample : samples) {
			doc.validate(sample);
			Sample s = mergeBean(sample);
			s = (new SampleDAO(this.currentSession())).save(s);
		}
		commit();
	}

	public SampleDTO save(SampleDTO sample) throws DAOException,
			ValidationException, LoginRequiredException {
		doc.validate(sample);
		if (sample.getOwner() == null)
			throw new LoginRequiredException();
		if (sample.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");
		Sample s = mergeBean(sample);

		s = (new SampleDAO(this.currentSession())).save(s);

		commit();
		return cloneBean(s);

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

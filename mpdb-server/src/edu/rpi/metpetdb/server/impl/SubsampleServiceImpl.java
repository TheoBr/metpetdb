package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class SubsampleServiceImpl extends MpDbServlet implements
		SubsampleService {
	private static final long serialVersionUID = 1L;

	public List<Subsample> all(final long sampleId) {
		final List<Subsample> l = (new SubsampleDAO(this.currentSession())
				.getAllBySampleID(sampleId));
		return (l);
	}

	public Results<Subsample> all(final PaginationParameters p,
			final long sampleId) {
		return (new SubsampleDAO(this.currentSession()).getAllBySampleID(p,
				sampleId));
	}

	public Results<Subsample> allWithImages(final PaginationParameters p,
			final long sampleId) {
		return (new SubsampleDAO(this.currentSession())
				.getAllWithImagesBySampleID(p, sampleId));
	}

	public Subsample details(final long id) throws DAOException {
		Subsample s = new Subsample();
		s.setId(id);

		s = (new SubsampleDAO(this.currentSession())).fill(s);
		return s;
	}
	public Subsample save(Subsample subsample) throws DAOException,
			ValidationException, LoginRequiredException {
		doc.validate(subsample);
		if (subsample.getSample() == null
				|| subsample.getSample().getOwner() == null
				|| subsample.getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");
		subsample = (new SubsampleDAO(this.currentSession())).save(subsample);

		commit();
		return (subsample);
	}

	public void delete(long id) throws DAOException, LoginRequiredException {
		final SubsampleDAO dao = new SubsampleDAO(this.currentSession());
		Subsample s = new Subsample();
		s.setId(id);
		s = dao.fill(s);

		if (s.getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");

		dao.delete(s);
		commit();
	}
}

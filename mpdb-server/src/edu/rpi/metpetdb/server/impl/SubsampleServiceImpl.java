package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;
import edu.rpi.metpetdb.server.model.Subsample;

public class SubsampleServiceImpl extends MpDbServlet implements
		SubsampleService {
	private static final long serialVersionUID = 1L;

	public List<SubsampleDTO> all(final long sampleId) {
		final List<Subsample> l = (new SubsampleDAO(this.currentSession())
				.getAllBySampleID(sampleId));
		return cloneBean(l);
	}

	public Results<SubsampleDTO> all(final PaginationParameters p,
			final long sampleId) {
		final ResultsFromDAO<Subsample> l = (new SubsampleDAO(this
				.currentSession()).getAllBySampleID(p, sampleId));
		final List<SubsampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SubsampleDTO>(l.getCount(), lDTO);
	}

	public Results<SubsampleDTO> allWithImages(final PaginationParameters p,
			final long sampleId) {
		final ResultsFromDAO<Subsample> l = (new SubsampleDAO(this
				.currentSession()).getAllWithImagesBySampleID(p, sampleId));
		final List<SubsampleDTO> lDTO = cloneBean(l.getList());
		return new Results<SubsampleDTO>(l.getCount(), lDTO);
	}

	public SubsampleDTO details(final long id) throws DAOException {
		Subsample s = new Subsample();
		s.setId(id);

		s = (new SubsampleDAO(this.currentSession())).fill(s);
		return (SubsampleDTO) clone(s);
	}
	public SubsampleDTO save(SubsampleDTO subsampleDTO) throws DAOException,
			ValidationException, LoginRequiredException {
		doc.validate(subsampleDTO);
		if (subsampleDTO.getSample() == null
				|| subsampleDTO.getSample().getOwner() == null
				|| subsampleDTO.getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");
		Subsample subsample = mergeBean(subsampleDTO);

		subsample = (new SubsampleDAO(this.currentSession())).save(subsample);

		commit();
		return cloneBean(subsample);
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

package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
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

	public List<Subsample> all(final long sampleId) throws MpDbException {
		final List<Subsample> l = (new SubsampleDAO(this.currentSession())
				.getAllBySampleID(sampleId, currentUserIdIfExists()));
		return (l);
	}
	
	public List<Subsample> allFromManySamples(final Collection<Long> sampleIds) throws MpDbException {
		final List<Subsample> l = new ArrayList<Subsample>();
		for (Long id : sampleIds){
			l.addAll((new SubsampleDAO(this.currentSession())
			.getAllBySampleID(id, currentUserIdIfExists())));
		}
		return (l);
	}

	public Results<Subsample> all(final PaginationParameters p,
			final long sampleId) throws MpDbException {
		return (new SubsampleDAO(this.currentSession()).getAllBySampleID(p,
				sampleId, currentUserIdIfExists()));
	}

	public Results<Subsample> allWithImages(final PaginationParameters p,
			final long sampleId) throws MpDbException {
		return (new SubsampleDAO(this.currentSession())
				.getAllWithImagesBySampleID(p, sampleId));
	}

	public Subsample details(final long id) throws MpDbException {
		Subsample s = new Subsample();
		s.setId(id);
		s = (new SubsampleDAO(this.currentSession())).fill(s);
		//FIXME: force a lazy load of the images so they can be added/removed from the UI
		//in the future images should be made its own page, similar to chemical analyses
		s.getImages().size();
		return s;
	}
	public Subsample save(Subsample subsample) throws MpDbException,
			ValidationException, LoginRequiredException {
		doc.validate(subsample);
		subsample = (new SubsampleDAO(this.currentSession())).save(subsample);

		commit();
		return (subsample);
	}
	public void saveAll(Collection<Subsample> subsamples)
		throws ValidationException, LoginRequiredException, MpDbException {
			final SubsampleDAO dao = new SubsampleDAO(this.currentSession());
			for(Subsample s : subsamples) {
				doc.validate(s);
				dao.save(s);
			}
			commit();
	}

	public void delete(long id) throws MpDbException, LoginRequiredException {
		final SubsampleDAO dao = new SubsampleDAO(this.currentSession());
		Subsample s = new Subsample();
		s.setId(id);
		s = dao.fill(s);
		dao.delete(s);
		commit();
	}
}

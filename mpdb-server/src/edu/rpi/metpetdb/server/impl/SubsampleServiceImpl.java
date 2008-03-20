package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.Subsample;

public class SubsampleServiceImpl extends MpDbServlet
		implements
			SubsampleService {
	private static final long serialVersionUID = 1L;

	public Results all(final PaginationParameters p, final long sampleId) {
		final String name = "Subsample.all";
		final Query sizeQuery = sizeQuery(name, sampleId);
		final List<Subsample> l = pageList(name, p, sampleId);
		final Number sz = (Number) sizeQuery.uniqueResult();
		if (sz.intValue() > 0) {
			final Iterator<Subsample> itr = l.iterator();
			while (itr.hasNext()) {
				final Subsample s = (Subsample) itr.next();
				s.setImageCount(((Number) sizeQuery("Image.bySubsampleId",
						s.getId()).uniqueResult()).intValue());
				s.setAnalysisCount(((Number) sizeQuery(
						"MineralAnalysis.bySubsampleId", s.getId())
						.uniqueResult()).intValue());
			}
			
			return new Results(sz.intValue(),  cloneBean(l));
		} else
			return new Results(sz.intValue(), new ArrayList<Subsample>());
	}
	public Results allWithImages(final PaginationParameters p,
			final long sampleId) {
		final String name = "Subsample.allWithImages";
		return toResults(sizeQuery(name, sampleId),
				pageQuery(name, p, sampleId));
	}
	
	public SubsampleDTO details(final long id) throws NoSuchObjectException {
		final Subsample s = (Subsample) byId("Subsample", id);
		s.setImageCount(((Number) sizeQuery("Image.bySubsampleId", s.getId())
				.uniqueResult()).intValue());
		s.setAnalysisCount(((Number) sizeQuery("MineralAnalysis.bySubsampleId",
				s.getId()).uniqueResult()).intValue());
		return (SubsampleDTO) clone(s);
	}

	public SubsampleDTO save(SubsampleDTO subsampleDTO)
			throws ValidationException, LoginRequiredException {
		doc.validate(subsampleDTO);
		if (subsampleDTO.getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");
		Subsample subsample = mergeBean(subsampleDTO);
		try {
			if (subsampleDTO.mIsNew())
				insert(subsample);
			else
				subsample = update(merge(subsample));
			commit();
			return cloneBean(subsample);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	public void delete(long id) throws NoSuchObjectException,
			LoginRequiredException {
		try {
			final Subsample s = byId("Subsample", id);
			if (s.getSample().getOwner().getId() != currentUser())
				throw new SecurityException("Cannot modify subsamples you don't own.");
			delete(s);
			commit();
		} catch (ConstraintViolationException cve) {

		}
	}
}

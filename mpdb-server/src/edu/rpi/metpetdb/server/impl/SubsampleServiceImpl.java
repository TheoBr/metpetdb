package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.service.SubsampleService;
import edu.rpi.metpetdb.server.MpDbServlet;

public class SubsampleServiceImpl extends MpDbServlet
		implements
			SubsampleService {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Results all(final PaginationParameters p, final long sampleId) {
		final String name = "Subsample.all";
		final Query sizeQuery = sizeQuery(name, sampleId);
		final Query pageQuery = pageQuery(name, p, sampleId);
		final Number sz = (Number) sizeQuery.uniqueResult();
		if (sz.intValue() > 0) {
			final List l = pageQuery.list();
			final Iterator itr = l.iterator();
			while (itr.hasNext()) {
				final Subsample s = (Subsample) itr.next();
				s.setImageCount(((Number) sizeQuery("Image.bySubsampleId",
						s.getId()).uniqueResult()).intValue());
				s.setAnalysisCount(((Number) sizeQuery(
						"MineralAnalysis.bySubsampleId", s.getId())
						.uniqueResult()).intValue());
			}
			return new Results(sz.intValue(), l);
		} else
			return new Results(sz.intValue(), new ArrayList());
	}
	public Results allWithImages(final PaginationParameters p,
			final long sampleId) {
		final String name = "Subsample.allWithImages";
		return toResults(sizeQuery(name, sampleId),
				pageQuery(name, p, sampleId));
	}

	@SuppressWarnings("unchecked")
	public Subsample details(final long id) throws NoSuchObjectException {
		final Subsample s = (Subsample) byId("Subsample", id);
		s.setImageCount(((Number) sizeQuery("Image.bySubsampleId", s.getId())
				.uniqueResult()).intValue());
		s.setAnalysisCount(((Number) sizeQuery("MineralAnalysis.bySubsampleId",
				s.getId()).uniqueResult()).intValue());
		s.setImages(load(s.getImages()));
		s.setMineralAnalyses(load(s.getMineralAnalyses()));
		forgetChanges();
		return s;
	}

	@SuppressWarnings("unchecked")
	public Subsample saveSubsample(Subsample subsample)
			throws ValidationException, LoginRequiredException {
		doc.validate(subsample);
		if (subsample.getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");

		try {
			if (subsample.mIsNew())
				insert(subsample);
			else
				subsample = (Subsample) update(merge(subsample));
			commit();
			subsample.setImages(load(subsample.getImages()));
			subsample.setMineralAnalyses(null);
			ImageServiceImpl.resetImage(subsample.getImages());
			SampleServiceImpl.resetSample(subsample.getSample());
			forgetChanges();
			return subsample;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	public void delete(long id) throws NoSuchObjectException,
			LoginRequiredException {
		try {
			Subsample s = (Subsample) byId("Subsample", id);
			if (s.getSample().getOwner().getId() != currentUser())
				throw new SecurityException("Cannot modify subsamples you don't own.");
			delete((Object)s);
			s = null;
			commit();
		} catch (ConstraintViolationException cve) {

		}
	}

	public static final void resetSubsample(final Subsample s) {
		s.setImages(null);
		s.setMineralAnalyses(null);
		if (s.getGrid() != null)
			ImageBrowserServiceImpl.resetGrid(s.getGrid());
	}
}

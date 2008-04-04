package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.MetamorphicGrade;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Reference;
import edu.rpi.metpetdb.server.model.Region;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.SampleMineral;

public class SampleServiceImpl extends MpDbServlet implements SampleService {
	private static final long serialVersionUID = 1L;

	public Results<SampleDTO> all(final PaginationParameters p) {
		final String name = "Sample.all";
		return toResults(sizeQuery(name), pageQuery(name, p));
	}

	public Results<SampleDTO> allSamplesForUser(final PaginationParameters p,
			long id) {
		final String name = "Sample.forUser";
		return toResults(sizeQuery(name, id), pageQuery(name, p, id));
	}

	public Results<SampleDTO> allPublicSamples(final PaginationParameters p) {
		final String name = "Sample.allPublic";
		return toResults(sizeQuery(name), pageQuery(name, p));
	}

	public Results<SampleDTO> projectSamples(final PaginationParameters p,
			long id) {
		final String name = "Sample.forProject";
		return toResults(sizeQuery(name, id), pageQuery(name, p, id));
	}

	public SampleDTO details(final long id) throws NoSuchObjectException {
		final Sample s = (Sample) byId("Sample", id);
		s.setSubsampleCount(((Number) sizeQuery("Subsample.bySampleId",
				s.getId()).uniqueResult()).intValue());
		return (SampleDTO) clone(s);
	}

	protected void save(final Collection<SampleDTO> samples)
			throws SampleAlreadyExistsException, ValidationException,
			LoginRequiredException {
		for (SampleDTO sample : samples) {
			doc.validate(sample);
			Sample s = mergeBean(sample);
			save(s);
		}
		commit();
	}

	public Sample save(Sample s) throws SampleAlreadyExistsException {
		replaceRegion(s);
		replaceMetamorphicGrade(s);
		replaceReferences(s);
		replaceSampleMinerals(s);
		try {

			if (s.mIsNew())
				insert(s);
			else {
				try {
					s = update(merge(s));
				} catch (TransientObjectException toe) {
					throw (toe);
				}
			}
			return s;
		} catch (ConstraintViolationException cve) {
			if ("samples_nk".equals(cve.getConstraintName()))
				throw new SampleAlreadyExistsException();
			throw cve;
		}
	}

	public SampleDTO save(SampleDTO sample)
			throws SampleAlreadyExistsException, ValidationException,
			LoginRequiredException {
		doc.validate(sample);
		if (sample.getOwner() == null)
			throw new LoginRequiredException();
		if (sample.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");
		Sample s = mergeBean(sample);
		s = save(s);
		commit();
		return cloneBean(s);

	}

	private void replaceSampleMinerals(final Sample s) {
		if (s.getMinerals() != null) {
			final Set<SampleMineral> mineralsToAdd = new HashSet<SampleMineral>();
			for (SampleMineral sm : s.getMinerals()) {
				final Query minerals = namedQuery("Mineral.byName");
				minerals.setString("name", sm.getMineral().getName());
				if (minerals.uniqueResult() != null) {
					s.getMinerals().remove(sm);
					final SampleMineral newsm = new SampleMineral();
					newsm.setMineral((Mineral) minerals.uniqueResult());
					mineralsToAdd.add(newsm);
				}
			}
			s.getMinerals().addAll(mineralsToAdd);
		}
	}

	private void replaceRegion(final Sample s) {
		if (s.getRegions() != null) {
			final Iterator<Region> itr = s.getRegions().iterator();
			final HashSet<Region> regionsToAdd = new HashSet<Region>();
			while (itr.hasNext()) {
				final Region r = (Region) itr.next();
				final Query regions = namedQuery("edu.rpi.metpetdb.server.model.Region.Region.byName");
				regions.setString("name", r.getName());
				if (regions.uniqueResult() != null) {
					itr.remove();
					regionsToAdd.add((Region) regions.uniqueResult());
				}
			}
			s.getRegions().addAll(regionsToAdd);
		}
	}

	private void replaceMetamorphicGrade(final Sample s) {
		if (s.getMetamorphicGrades() != null) {
			final Iterator<MetamorphicGrade> itr = s.getMetamorphicGrades()
					.iterator();
			final HashSet<MetamorphicGrade> metamorphicToAdd = new HashSet<MetamorphicGrade>();
			while (itr.hasNext()) {
				final MetamorphicGrade mg = (MetamorphicGrade) itr.next();
				final Query grades = namedQuery("edu.rpi.metpetdb.server.model.MetamorphicGrade.MetamorphicGrade.byName");
				grades.setString("name", mg.getName());
				if (grades.uniqueResult() != null) {
					itr.remove();
					metamorphicToAdd.add((MetamorphicGrade) grades
							.uniqueResult());
				}
			}
			s.getMetamorphicGrades().addAll(metamorphicToAdd);
		}
	}

	private void replaceReferences(final Sample s) {
		if (s.getReferences() != null) {
			final Iterator<Reference> itr = s.getReferences().iterator();
			final HashSet<Reference> referencesToAdd = new HashSet<Reference>();

			while (itr.hasNext()) {
				final Reference r = (Reference) itr.next();
				final Query references = namedQuery("edu.rpi.metpetdb.server.model.Reference.Reference.byName");

				references.setString("name", r.getName());

				if (references.uniqueResult() != null) {
					itr.remove();
					referencesToAdd.add((Reference) references.uniqueResult());
				}
			}
			s.getReferences().addAll(referencesToAdd);
		}
	}

	public void delete(long id) throws NoSuchObjectException,
			LoginRequiredException {
		try {
			Sample s = (Sample) byId("Sample", id);
			if (s.getOwner().getId() != currentUser())
				throw new SecurityException(
						"Cannot modify samples you don't own.");
			delete(s);
			s = null;
			commit();
		} catch (ConstraintViolationException cve) {

		}
	}

	@Deprecated
	public static final void resetSample(final Sample s) {
		s.setImages(null);
		s.setMetamorphicGrades(null);
		s.setMinerals(null);
		s.setProjects(null);
		s.setReferences(null);
		s.setRegions(null);
		s.setSubsamples(null);
	}
}

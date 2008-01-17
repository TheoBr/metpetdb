package edu.rpi.metpetdb.server;

import java.util.HashSet;
import java.util.Iterator;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.TransientObjectException;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.service.SampleService;

public class SampleServiceImpl extends MpDbServlet implements SampleService {
	private static final long serialVersionUID = 1L;

	public Results all(final PaginationParameters p) {
		final String name = "Sample.all";
		return toResults(sizeQuery(name), pageQuery(name, p));
	}

	public Results allSamplesForUser(final PaginationParameters p, long id) {
		final String name = "Sample.forUser";
		return toResults(sizeQuery(name, id), pageQuery(name, p, id));
	}
	
	public Results allPublicSamples(final PaginationParameters p){
		final String name = "Sample.allPublic";
		return toResults(sizeQuery(name), pageQuery(name,p));
	}

	public Results projectSamples(final PaginationParameters p, long id) {
		final String name = "Sample.forProject";
		return toResults(sizeQuery(name, id), pageQuery(name, p, id));
	}

	public Sample details(final long id) throws NoSuchObjectException {
		final Sample s = (Sample) byId("Sample", id);
		s.setSubsampleCount(((Number) sizeQuery(
				"Subsample.bySampleId", s.getId()).uniqueResult()).intValue());
		s.setProjects(load(s.getProjects()));
		s.setMinerals(load(s.getMinerals()));
		s.setSubsamples(load(s.getSubsamples()));
		s.setImages(null);
		s.setRegions(load(s.getRegions()));
		s.setMetamorphicGrades(load(s.getMetamorphicGrades()));
		s.setReferences(load(s.getReferences()));
		forgetChanges();
		return s;
	}

	public Sample saveSample(Sample sample)
			throws SampleAlreadyExistsException, ValidationException,
			LoginRequiredException {
		doc.validate(sample);
		if (sample.getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");

		try {
			replaceRegion(sample);
			replaceMetamorphicGrade(sample);
			replaceReferences(sample);
			
			
			if (sample.mIsNew())
				insert(sample);
			else {
				try {
					sample = (Sample) update(merge(sample));
				} catch (TransientObjectException toe) {
					throw (toe);
				}
			}
			commit();
			if (sample.getProjects() != null)
				sample.setProjects(load(sample.getProjects()));
			if (sample.getMinerals() != null)
				sample.setMinerals(load(sample.getMinerals()));
			if (sample.getRegions() != null)
				sample.setRegions(load(sample.getRegions()));
			if (sample.getMetamorphicGrades() != null)
				sample.setMetamorphicGrades(load(sample.getMetamorphicGrades()));
			if (sample.getReferences() != null)
				sample.setReferences(load(sample.getReferences()));
			sample.setImages(null);
			sample.setSubsamples(null);
			forgetChanges();
			return sample;
		} catch (ConstraintViolationException cve) {
			if ("samples_nk".equals(cve.getConstraintName()))
				throw new SampleAlreadyExistsException();
			throw cve;
		}
	}
	
	//TODO for some reason it does not get the named query
	private void replaceRegion(final Sample s) {
		final Iterator itr = s.getRegions().iterator();
		final HashSet regionsToAdd = new HashSet();
		while(itr.hasNext()) {
			final Region r = (Region) itr.next();
			final Query regions = namedQuery("edu.rpi.metpetdb.client.model.Region.Region.byName");
			regions.setString("name", r.getName());
			if (regions.uniqueResult() != null) {
				itr.remove();
				regionsToAdd.add(regions.uniqueResult());
			}
		}
		s.getRegions().addAll(regionsToAdd);
	}
	
	private void replaceMetamorphicGrade(final Sample s) {
		final Iterator itr = s.getMetamorphicGrades().iterator();
		final HashSet metamorphicToAdd = new HashSet();
		
		while(itr.hasNext()) {
			final MetamorphicGrade mg = (MetamorphicGrade) itr.next();
			final Query grades = namedQuery("edu.rpi.metpetdb.client.model.MetamorphicGrade.MetamorphicGrade.byName");
			
			grades.setString("name", mg.getName());
			
			if (grades.uniqueResult() != null) {
				itr.remove();
				metamorphicToAdd.add(grades.uniqueResult());
			}
		}
		s.getMetamorphicGrades().addAll(metamorphicToAdd);
	}
	
	private void replaceReferences(final Sample s) {
		final Iterator itr = s.getReferences().iterator();
		final HashSet referencesToAdd = new HashSet();
		
		while(itr.hasNext()) {
			final Reference r = (Reference) itr.next();
			final Query references = namedQuery("edu.rpi.metpetdb.client.model.Reference.Reference.byName");
			
			references.setString("name", r.getName());
			
			if (references.uniqueResult() != null) {
				itr.remove();
				referencesToAdd.add(references.uniqueResult());
			}
		}
		s.getReferences().addAll(referencesToAdd);
	}
	
	public void delete(long id) throws NoSuchObjectException, LoginRequiredException {
		try {
			Sample s = (Sample) byId("Sample", id);
			if (s.getOwner().getId() != currentUser())
				throw new SecurityException("Cannot modify samples you don't own.");
			delete((Object)s);
			s = null;
			commit();
		} catch (ConstraintViolationException cve) {
			
		}
	}
	
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
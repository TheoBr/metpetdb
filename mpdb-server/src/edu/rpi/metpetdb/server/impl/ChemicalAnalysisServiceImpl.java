package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Reference;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.Subsample;

public class ChemicalAnalysisServiceImpl extends MpDbServlet implements
		ChemicalAnalysisService {
	private static final long serialVersionUID = 1L;

	public ChemicalAnalysisDTO details(long id) throws NoSuchObjectException {
		final ChemicalAnalysisDTO ma = cloneBean(byId("ChemicalAnalysis", id));
		return ma;
	}

	public Results<ChemicalAnalysisDTO> all(PaginationParameters parameters,
			final long subsampleId) {
		final String name = "ChemicalAnalysis.bySubsampleId";
		final Query sizeQuery = sizeQuery(name);
		final Query pageQuery = pageQuery(name, parameters);
		sizeQuery.setLong("id", subsampleId);
		pageQuery.setLong("id", subsampleId);
		return toResults(sizeQuery, pageQuery);
	}

	public List<ChemicalAnalysisDTO> all(long subsampleId)
			throws NoSuchObjectException {
		return cloneBean(byKey("ChemicalAnalysis", "subsampleId", subsampleId));
	}

	protected void save(final Collection<ChemicalAnalysisDTO> analyses)
			throws ValidationException, LoginRequiredException {
		try {
			for (ChemicalAnalysisDTO analysis : analyses) {
				doc.validate(analysis);
				save((ChemicalAnalysis) mergeBean(analysis));
			}
		} catch (ValidationException e) {
			forgetChanges();
			throw e;
		}
		commit();
	}

	public ChemicalAnalysisDTO save(ChemicalAnalysisDTO caDTO)
			throws ValidationException, LoginRequiredException {
		doc.validate(caDTO);
		ChemicalAnalysis ca = mergeBean(caDTO);
		ca = save(ca);
		commit();
		return cloneBean(ca);
	}

	public ChemicalAnalysis save(ChemicalAnalysis ma)
			throws ValidationException, LoginRequiredException {
		replaceSample(ma);
		replaceReferences(ma);
		replaceMineral(ma);

		if (ma.getSubsample().getSample().getOwner() == null)
			throw new LoginRequiredException();
		if (ma.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");

		replaceReferences(ma);
		try {
			if (ma.getImage() != null && ma.getImage().mIsNew()) {
				ma.setImage(update(merge(ma.getImage())));
			}
			if (ma.mIsNew()) {
				insert(ma);
			} else
				ma = update(merge(ma));
			return ma;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	public void delete(long id) throws NoSuchObjectException,
			LoginRequiredException {
		try {
			final ChemicalAnalysis ma = byId("ChemicalAnalysis", id);
			delete(ma);
			commit();
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}

	private void replaceMineral(final ChemicalAnalysis ca) {
		if (ca.getMineral() != null) {
			final Query minerals = namedQuery("Mineral.byName");

			minerals.setString("name", ca.getMineral().getName());

			if (minerals.uniqueResult() != null)
				ca.setMineral((Mineral) minerals.uniqueResult());
		}
	}

	private void replaceSample(final ChemicalAnalysis ca)
			throws ValidationException, LoginRequiredException {
		// TODO: once this is debugged, Null Pointer exceptions should indicate
		// when samples or subsamples are not in the database, and should be
		// handled accordingly
		final Query sample = namedQuery("Sample.byUser.byAlias");
		final Query subsample = namedQuery("Subsample.bySample.byName");

		sample.setParameter("id", currentUser());
		sample.setParameter("alias", ca.getSubsample().getSample().getAlias());

		subsample.setParameter("id", ((Sample) sample.uniqueResult()).getId());
		subsample.setParameter("name", ca.getSubsample().getName());

		ca.setSubsample((Subsample) subsample.uniqueResult());
	}

	// FIXME this is a copy of the method in SampleServiceImpl we should
	// implemement some sort of utility class to take care of this (maybe?)
	private void replaceReferences(final ChemicalAnalysis ca) {
		if (ca.getReference() != null) {

			final Reference r = ca.getReference();
			final Query references = namedQuery("edu.rpi.metpetdb.server.model.Reference.Reference.byName");

			references.setString("name", r.getName());

			if (references.uniqueResult() != null) {
				ca.setReference((Reference) references.uniqueResult());
			}
		}
	}

	protected boolean isNewCA(final ChemicalAnalysis ca) {
		if (ca.getSubsample() == null)
			return true;

		final Query q = namedQuery("ChemicalAnalysis.bySubsampleId.byspotId");
		q.setLong("id", ca.getSubsample().getId());
		q.setString("spotId", ca.getSpotId());

		if (q.uniqueResult() != null)
			return false;
		else
			return true;
	}
}

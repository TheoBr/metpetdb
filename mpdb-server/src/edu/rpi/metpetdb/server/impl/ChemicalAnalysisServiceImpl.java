package edu.rpi.metpetdb.server.impl;

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
import edu.rpi.metpetdb.server.model.Reference;

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

	public ChemicalAnalysisDTO save(ChemicalAnalysisDTO maDTO)
			throws ValidationException, LoginRequiredException {
		doc.validate(maDTO);
		if (maDTO.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");
		ChemicalAnalysis ma = mergeBean(maDTO);
		replaceReferences(ma);
		try {
			if (ma.getImage() != null && ma.getImage().mIsNew()) {
				ma.setImage(update(merge(ma.getImage())));
			}
			if (ma.mIsNew()) {
				insert(ma);
			} else
				ma = update(merge(ma));
			commit();
			return cloneBean(ma);
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

}

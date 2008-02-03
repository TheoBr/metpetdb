package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.MineralAnalysis;
import edu.rpi.metpetdb.client.service.MineralAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;

public class MineralAnalysisServiceImpl extends MpDbServlet
		implements
			MineralAnalysisService {
	private static final long serialVersionUID = 1L;

	public MineralAnalysis details(long id) throws NoSuchObjectException {
		final MineralAnalysis ma = (MineralAnalysis) byId("MineralAnalysis", id);
		return ma;
	}

	public Results all(PaginationParameters parameters, final long subsampleId) {
		final String name = "MineralAnalysis.bySubsampleId";
		final Query sizeQuery = sizeQuery(name);
		final Query pageQuery = pageQuery(name, parameters);
		sizeQuery.setLong("id", subsampleId);
		pageQuery.setLong("id", subsampleId);
		return toResults(sizeQuery, pageQuery);
	}

	@SuppressWarnings("unchecked")
	public ArrayList all(long subsampleId) throws NoSuchObjectException {
		return (ArrayList) byKey(MineralAnalysis.class, "subsampleId",
				subsampleId);
	}

	public MineralAnalysis saveMineralAnalysis(MineralAnalysis mineralAnalysis)
			throws ValidationException, LoginRequiredException {
		doc.validate(mineralAnalysis);
		if (mineralAnalysis.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");

		try {
			if (mineralAnalysis.getImage() != null
					&& mineralAnalysis.getImage().mIsNew()) {
				mineralAnalysis
						.setImage((Image) update(merge(mineralAnalysis
								.getImage())));
			}
			if (mineralAnalysis.mIsNew()) {
				insert(mineralAnalysis);
			} else
				mineralAnalysis = (MineralAnalysis) update(merge(mineralAnalysis));
			commit();
			SubsampleServiceImpl.resetSubsample(mineralAnalysis.getSubsample());
			SampleServiceImpl.resetSample(mineralAnalysis.getSubsample().getSample());
			ImageServiceImpl.resetImage(mineralAnalysis.getImage());
			forgetChanges();
			return mineralAnalysis;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
	
	public void delete(long id) throws NoSuchObjectException, LoginRequiredException {
		try {
			MineralAnalysis m = (MineralAnalysis) byId("MineralAnalysis",id);
			delete(m);
			m = null;
			commit();
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
}
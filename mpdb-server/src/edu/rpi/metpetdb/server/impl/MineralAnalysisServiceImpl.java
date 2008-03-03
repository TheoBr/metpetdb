package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;
import edu.rpi.metpetdb.client.service.MineralAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.MineralAnalysis;

public class MineralAnalysisServiceImpl extends MpDbServlet
		implements
			MineralAnalysisService {
	private static final long serialVersionUID = 1L;

	public MineralAnalysisDTO details(long id) throws NoSuchObjectException {
		final MineralAnalysisDTO ma = clone(byId("MineralAnalysis", id));
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

	public ArrayList<MineralAnalysisDTO> all(long subsampleId) throws NoSuchObjectException {
		return clone(byKey("MineralAnalysis", "subsampleId",
				subsampleId));
	}

	public MineralAnalysisDTO saveMineralAnalysis(MineralAnalysisDTO maDTO)
			throws ValidationException, LoginRequiredException {
		doc.validate(maDTO);
		if (maDTO.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify subsamples you don't own.");
		MineralAnalysis ma = merge(maDTO);
		try {
			if (ma.getImage() != null
					&& ma.getImage().mIsNew()) {
				ma
						.setImage(update(merge(ma
								.getImage())));
			}
			if (ma.mIsNew()) {
				insert(ma);
			} else
				ma = update(merge(ma));
			commit();
			return clone(ma);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
	
	public void delete(long id) throws NoSuchObjectException, LoginRequiredException {
		try {
			final MineralAnalysis ma = byId("MineralAnalysis",id);
			delete(ma);
			commit();
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
}
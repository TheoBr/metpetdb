package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;

public class ChemicalAnalysisServiceImpl extends MpDbServlet implements
		ChemicalAnalysisService {
	private static final long serialVersionUID = 1L;

	public ChemicalAnalysis details(long id) throws DAOException {
		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId(new Long(id).intValue());
		ca = (new ChemicalAnalysisDAO(this.currentSession())).fill(ca);
		return (ca);
	}

	public Results<ChemicalAnalysis> all(PaginationParameters parameters,
			final long subsampleId) {
		return (new ChemicalAnalysisDAO(this.currentSession())).getAll(
				parameters, subsampleId);
	}

	public List<ChemicalAnalysis> all(long subsampleId) {
		List<ChemicalAnalysis> l = (new ChemicalAnalysisDAO(this
				.currentSession())).getAll(subsampleId);
		// List<ChemicalAnalysis> l = (l);
		return l;
	}

	protected void save(final Collection<ChemicalAnalysis> analyses)
			throws ValidationException, LoginRequiredException, DAOException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());

		User user = new User();
		user.setId(currentUser());

		try {
			for (ChemicalAnalysis analysis : analyses) {
				doc.validate(analysis);
				ChemicalAnalysis ca = (analysis);
				ca.getSubsample().getSample().setOwner(user);
				ca = dao.save(ca);
			}
		} catch (ValidationException e) {
			forgetChanges();
			throw e;
		}
		commit();
	}

	public ChemicalAnalysis save(ChemicalAnalysis ca)
			throws ValidationException, LoginRequiredException, DAOException {
		doc.validate(ca);

		if (ca.getSubsample().getSample().getOwner() == null)
			throw new LoginRequiredException();
		if (ca.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");

		ca = (new ChemicalAnalysisDAO(this.currentSession())).save(ca);
		commit();
		return (ca);
	}

	public void delete(long id) throws DAOException, LoginRequiredException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());

		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId((new Long(id)).intValue());
		ca = dao.fill(ca);

		if (ca.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException(
					"Cannot modify ChemicalAnlaysis you don't own.");

		dao.delete(ca);
		commit();
	}
}

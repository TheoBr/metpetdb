package edu.rpi.metpetdb.server.impl;

import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.ChemicalAnalysisService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.ResultsFromDAO;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.model.User;

public class ChemicalAnalysisServiceImpl extends MpDbServlet implements
		ChemicalAnalysisService {
	private static final long serialVersionUID = 1L;

	public ChemicalAnalysisDTO details(long id) throws DAOException {
		ChemicalAnalysis ca = new ChemicalAnalysis();
		ca.setId(new Long(id).intValue());
		ca = (new ChemicalAnalysisDAO(this.currentSession())).fill(ca);
		return cloneBean(ca);
	}

	public Results<ChemicalAnalysisDTO> all(PaginationParameters parameters,
			final long subsampleId) {
		ResultsFromDAO<ChemicalAnalysis> l = (new ChemicalAnalysisDAO(this
				.currentSession())).getAll(parameters, subsampleId);
		List<ChemicalAnalysisDTO> lDTO = cloneBean(l.getList());
		return new Results<ChemicalAnalysisDTO>(l.getCount(), lDTO);
	}

	public List<ChemicalAnalysisDTO> all(long subsampleId) {
		List<ChemicalAnalysis> l = (new ChemicalAnalysisDAO(this
				.currentSession())).getAll(subsampleId);
		List<ChemicalAnalysisDTO> lDTO = cloneBean(l);
		return lDTO;
	}

	protected void save(final Collection<ChemicalAnalysisDTO> analyses)
			throws ValidationException, LoginRequiredException, DAOException {
		ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this.currentSession());

		User user = new User();
		user.setId(currentUser());

		try {
			for (ChemicalAnalysisDTO analysis : analyses) {
				doc.validate(analysis);
				ChemicalAnalysis ca = mergeBean(analysis);
				ca.getSubsample().getSample().setOwner(user);
				ca = dao.save(ca);
			}
		} catch (ValidationException e) {
			forgetChanges();
			throw e;
		}
		commit();
	}

	public ChemicalAnalysisDTO save(ChemicalAnalysisDTO caDTO)
			throws ValidationException, LoginRequiredException, DAOException {
		doc.validate(caDTO);

		if (caDTO.getSubsample().getSample().getOwner() == null)
			throw new LoginRequiredException();
		if (caDTO.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify samples you don't own.");

		ChemicalAnalysis ca = mergeBean(caDTO);
		ca = (new ChemicalAnalysisDAO(this.currentSession())).save(ca);
		commit();
		return cloneBean(ca);
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

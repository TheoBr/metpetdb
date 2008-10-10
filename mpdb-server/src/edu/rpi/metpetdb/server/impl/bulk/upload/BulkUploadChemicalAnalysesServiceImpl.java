package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadChemicalAnalysesServiceImpl extends BulkUploadService
		implements BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;

	public void commit(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException,
			ValidationException, DAOException {
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					MpDbServlet.getFileUploadPath() + fileOnServer));
			ap.parse();
			final List<ChemicalAnalysis> analyses = ap.getAnalyses();
			User u = new User();
			u.setId(currentUser());

			// Insert new Subsamples as required
			SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			for (ChemicalAnalysis ca : analyses) {
				Subsample ss = (ca.getSubsample());
				ss.getSample().setOwner(u);
				try {
					ssDAO.fill(ss);
				} catch (SubsampleNotFoundException daoe) {
					ssDAO.save(ss);
				}
			}
			// Save chemical analyses

			save(analyses);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	public BulkUploadResult parser(String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					MpDbServlet.getFileUploadPath() + fileOnServer));
			ap.parse();
			final List<ChemicalAnalysis> analyses = ap.getAnalyses();
			int row = 2;
			// Keeps track of existing/new subsample names for each sample
			final Map<String, Collection<String>> subsampleNames = new HashMap<String, Collection<String>>();
			final BulkUploadResultCount caResultCount = new BulkUploadResultCount();
			final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
			final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this
					.currentSession());
			final SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			for (ChemicalAnalysis ca : analyses) {
				try {
					doc.validate(ca);
					if (dao.isNew(ca))
						caResultCount.incrementFresh();
					else
						caResultCount.incrementOld();
				} catch (ValidationException e) {
					caResultCount.incrementInvalid();
					errors.put(row, e);
				}
				Subsample ss = (ca.getSubsample());
				if (ss != null) {
					if (!subsampleNames.containsKey(ca.getSubsample()
							.getSample().getAlias())
							|| !subsampleNames.get(
									ca.getSubsample().getSample().getAlias())
									.contains(ss.getName())) {
						if (!subsampleNames.containsKey(ca.getSubsample()
								.getSample().getAlias()))
							subsampleNames.put(ca.getSubsample().getSample()
									.getAlias(), new HashSet<String>());
						try {
							ssDAO.fill(ss);
							ssResultCount.incrementOld();
						} catch (DAOException e) {
							// Means it is new because we could not find
							// it
							ssResultCount.incrementFresh();
						}
						subsampleNames.get(
								ca.getSubsample().getSample().getAlias()).add(
								ca.getSubsample().getName());
					}
					++row;
				} else {
					// Every Chemical Analysis needs a subsample so add an error
					errors.put(row, new PropertyRequiredException("Subsample"));
				}
			}
			results.addResultCount("Chemical Analysis", caResultCount);
			results.addResultCount("Subsamples", ssResultCount);
			results.setHeaders(ap.getHeaders());
			results.setErrors(errors);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
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
}

package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
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
			// maps a sample alias to an actual sample
			final Map<String, Sample> samples = new HashMap<String, Sample>();
			// maps a sample alias + subsample name to an actual subsample
			final Map<String, Subsample> subsamples = new HashMap<String, Subsample>();
			final BulkUploadResultCount caResultCount = new BulkUploadResultCount();
			final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
			final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(this
					.currentSession());
			final SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
			final SampleDAO sDAO = new SampleDAO(this.currentSession());
			User u = new User();
			u.setId(currentUser());
			for (ChemicalAnalysis ca : analyses) {
				try {
					// see if our sample exists
					Sample s = ca.getSubsample().getSample();
					s.setOwner(u);
					try {
						// if we don't have this sample already loaded check
						// for it in the database
						if (!samples.containsKey(s.getAlias())) {
							s = sDAO.fill(s);
							samples.put(s.getAlias(), s);
							subsampleNames.put(s.getAlias(),
									new HashSet<String>());
						} else {
							s = samples.get(s.getAlias());
						}
					} catch (DAOException e) {
						// There is no sample we have to add an error
						// Every Image needs a sample so add an error
						errors
								.put(row, new PropertyRequiredException(
										"Sample"));
						++row;
						continue;
					}
					Subsample ss = (ca.getSubsample());
					ss.setSample(s);
					if (ss != null) {
						// if we don't have the name stored already we need
						// to load the subsample
						if (!subsampleNames.get(s.getAlias()).contains(
								ss.getName())) {
							try {
								doc.validate(ss);								
								ss = ssDAO.fill(ss);
								subsamples.put(s.getAlias() + ss.getName(), ss);
								ca.setSubsample(ss);
								ssResultCount.incrementOld();
							} catch (DAOException e) {
								// Means it is new because we could not find
								// it
								ssResultCount.incrementFresh();
							}
							subsampleNames.get(s.getAlias()).add(
									ca.getSubsample().getName());
						} else {
							ca.setSubsample(subsamples.get(s.getAlias()
									+ ss.getName()));
						}
					} else {
						// Every Image needs a subsample so add an error
						errors.put(row, new PropertyRequiredException(
								"Subsample"));
					}
					doc.validate(ca);
					if (dao.isNew(ca))
						caResultCount.incrementFresh();
					else
						caResultCount.incrementOld();
				} catch (ValidationException e) {
					errors.put(row, e);
					caResultCount.incrementInvalid();
				}
				++row;
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

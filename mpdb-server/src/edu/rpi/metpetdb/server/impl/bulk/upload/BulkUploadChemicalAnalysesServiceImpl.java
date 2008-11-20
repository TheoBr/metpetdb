package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
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

	public BulkUploadResult parser(String fileOnServer, boolean save)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			if (save) {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			}
			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					MpDbServlet.getFileUploadPath() + fileOnServer));
			ap.parse();
			final Map<Integer, ChemicalAnalysis> analyses = ap.getAnalyses();
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
			final Iterator<Integer> rows = analyses.keySet().iterator();
			while(rows.hasNext()) {
				int row = rows.next();
				final ChemicalAnalysis ca = analyses.get(row);
				try {
					// see if our subsample exists
					if (ca.getSubsample() == null) {
						results.addError(row, new PropertyRequiredException(
						"Subsample"));
						continue;
					}
					// see if our sample exists
					if (ca.getSubsample().getSample() == null) {
						results.addError(row, new PropertyRequiredException(
						"Sample"));
						continue;
					}
					Sample s = ca.getSubsample().getSample();
					s.setOwner(u);
					ca.getSubsample().setOwner(u);
					ca.setOwner(u);
					ca.setPublicData(false);
					ca.getSubsample().setPublicData(false);
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
						results.addError(row, new PropertyRequiredException(
								"Sample"));
						continue;
					}
					Subsample ss = (ca.getSubsample());
					ss.setSample(s);
					if (ss != null && s != null) {
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
								if (save) {
									try {
										ss = ssDAO.save(ss);
									} catch (DAOException e1) {
										results.addError(row, e1);
									}
									subsamples.put(s.getAlias()
											+ ss.getName(), ss);
									ca.setSubsample(ss);
								}
							}
							subsampleNames.get(s.getAlias()).add(
									ca.getSubsample().getName());
						} else {
							ca.setSubsample(subsamples.get(s.getAlias()
									+ ss.getName()));
						}
					} else {
						// Every Image needs a subsample so add an error
						results.addError(row, new PropertyRequiredException(
								"Subsample"));
					}
					doc.validate(ca);
					if (dao.isNew(ca))
						caResultCount.incrementFresh();
					else
						caResultCount.incrementOld();
					if (save) {
						try {
							dao.save(ca);
						} catch (MpDbException e) {
							results.addError(row, e);
						} catch (HibernateException e) {
							results.addError(row, handleHibernateException(e));
						}
					}
				} catch (ValidationException e) {
					results.addError(row, e);
					caResultCount.incrementInvalid();
				} catch (HibernateException e) {
					results.addError(row, handleHibernateException(e));
				}
				++row;
			}
			results.addResultCount("Chemical Analysis", caResultCount);
			results.addResultCount("Subsamples", ssResultCount);
			results.setHeaders(ap.getHeaders());
			if (save && results.getErrors().isEmpty()) {
				try {
					commit();
				} catch (DAOException e) {
					results.addError(0, e);
				}
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
	}
}

package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.exception.GenericJDBCException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadError;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.Parser;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

/**
 * Helper methods for all of the implementing bulk upload services
 * 
 * @author anthony
 * 
 */
public abstract class BulkUploadService extends MpDbServlet {

	private static final long serialVersionUID = 1L;

	public BulkUploadResult parser(String fileOnServer, boolean save)
			throws LoginRequiredException, MpDbException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			if (save) {
				updateFile(fileOnServer);
			}
			final SampleDAO sampleDao = new SampleDAO(this.currentSession());
			final SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());

			// Keeps track of existing/new subsample names for each sample
			final Map<String, Collection<String>> subsampleNames = new HashMap<String, Collection<String>>();
			// maps a sample number to an actual sample
			final Map<String, Sample> samples = new HashMap<String, Sample>();
			// maps a sample number + subsample name to an actual subsample
			final Map<String, Subsample> subsamples = new HashMap<String, Subsample>();

			parserImpl(fileOnServer, save, results, sampleDao, ssDAO,
					subsampleNames, samples, subsamples);

			if (save && results.getErrors().isEmpty()) {
				try {
					commit();
				} catch (Exception e) {
					results.addError(0, getNiceException(e));
				}
			}
		} catch (LoginRequiredException e) {
			throw e; // so that it does not get caught below
		} catch (final Exception e) {
			results.addError(-1, getNiceException(e));
		}
		return results;
	}

	public abstract void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			final Map<String, Collection<String>> subsampleNames,
			final Map<String, Sample> samples,
			final Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException;

	/**
	 * updates the file in the datbase and specifies which user saved the
	 * results of the file
	 * 
	 * @param fileOnServer
	 * @throws HibernateException
	 * @throws LoginRequiredException
	 */
	protected void updateFile(final String fileOnServer)
			throws HibernateException, LoginRequiredException {
		currentSession()
				.createSQLQuery(
						"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
				.setParameter("user_id", currentUserId()).setParameter("hash",
						fileOnServer).executeUpdate();
	}

	protected void addErrors(final Parser<?> parser,
			final BulkUploadResult results) {
		final Map<Integer, BulkUploadError> existingErrors = parser.getErrors();
		final Set<Integer> keys = existingErrors.keySet();
		final Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			final Integer i = itr.next();
			results.addError(i.intValue(), existingErrors.get(i).getColumn(),
					existingErrors.get(i).getCellData(),
					getNiceException(existingErrors.get(i).getException()));
		}
	}
	
	protected void addWarnings(final Parser<?> parser,
			final BulkUploadResult results) {
		final Map<Integer, BulkUploadError> existingWarnings = parser.getWarnings();
		final Set<Integer> keys = existingWarnings.keySet();
		final Iterator<Integer> itr = keys.iterator();
		while (itr.hasNext()) {
			final Integer i = itr.next();
			results.addWarning(i.intValue(), existingWarnings.get(i).getColumn(),
					existingWarnings.get(i).getCellData(),
					getNiceException(existingWarnings.get(i).getException()));
		}
	}

	protected MpDbException getNiceException(final Exception e) {
		return getNiceException(e, null);
	}

	protected MpDbException getNiceException(final Exception e,
			final MObject obj) {
		e.printStackTrace();
		if (e instanceof MpDbException) {
			return (MpDbException) e;
		} else if (e instanceof GenericJDBCException) {
			if (((GenericJDBCException) e).getSQLException().getSQLState()
					.equals("25P02")) {
				return new GenericDAOException(
						"Bulk Upload stopped due to fatal error");
			} else {
				return new GenericDAOException(e.getMessage());
			}
		} else if (e instanceof HibernateException) {
			return handleHibernateException((HibernateException) e, obj);
		} else {
			return new GenericDAOException(e.getMessage());
		}

	}
	protected <T extends MObject> void initObject(T obj)
			throws LoginRequiredException, MpDbException {
		if (obj instanceof HasOwner)
			((HasOwner) obj).setOwner(currentUser());
		if (obj instanceof PublicData)
			((PublicData) obj).setPublicData(false);
		if (obj instanceof HasSubsample) {
			if (((HasSubsample) obj).getSubsample() != null) {
				((HasSubsample) obj).getSubsample().setOwner(currentUser());
				((HasSubsample) obj).getSubsample().setPublicData(false);
			}
		}
		if (obj instanceof HasSample) {
			if (((HasSample) obj).getSample() != null)
			((HasSample) obj).getSample().setOwner(currentUser());
		}
	}

	protected Sample checkForSample(Sample s,
			final Map<String, Sample> samples, final SampleDAO sampleDao,
			final BulkUploadResult results,
			final Map<String, Collection<String>> subsampleNames, int row) {
		try {
			// if we don't have this sample already loaded check
			// for it in the database
			if (!samples.containsKey(s.getNumber().toLowerCase())) {
				s = sampleDao.fill(s);
				samples.put(s.getNumber().toLowerCase(), s);
				subsampleNames.put(s.getNumber().toLowerCase(),
						new HashSet<String>());
				for (Subsample subsample : s.getSubsamples())
					subsampleNames.get(s.getNumber().toLowerCase()).add(subsample.getName());
			} else {
				s = samples.get(s.getNumber().toLowerCase());
			}
		} catch (MpDbException e) {
			// There is no sample we have to add an error
			// Every Image needs a sample so add an error
			results.addError(row, e);
		}
		return s;
	}

	protected Subsample checkForSubsample(Sample s, Subsample ss,
			final Map<String, Sample> samples, final SubsampleDAO ssDao,
			final BulkUploadResult results,
			final Map<String, Collection<String>> subsampleNames, int row,
			final Map<String, Subsample> subsamples,
			BulkUploadResultCount ssResultCount, boolean save)
			throws ValidationException {
		if (subsampleNames.get(s.getNumber().toLowerCase()) != null
				&& !subsampleNames.get(s.getNumber().toLowerCase()).contains(
						ss.getName().toLowerCase())) {
			try {
				ss = ssDao.fill(ss);
				subsamples.put(s.getNumber().toLowerCase()
						+ ss.getName().toLowerCase(), ss);
				ssResultCount.incrementOld();
			} catch (MpDbException e) {
				// Means it is new because we could not find
				// it
				doc.validate(ss);
				ssResultCount.incrementFresh();
				if (save) {
					try {
						ss = ssDao.save(ss);
					} catch (MpDbException e1) {
						results.addError(row, e1);
					}
					subsamples.put(s.getNumber().toLowerCase()
							+ ss.getName().toLowerCase(), ss);
				}
			}
			subsampleNames.get(s.getNumber().toLowerCase()).add(
					ss.getName().toLowerCase());
			return ss;
		} else {
			return subsamples.get(s.getNumber().toLowerCase()
					+ ss.getName().toLowerCase());
		}
	}
	protected <T extends MObject> void analyzeResults(
			final Map<Integer, T> results,
			final BulkUploadResultCount resultCount,
			final BulkUploadResult parseResults, final boolean save,
			final MpDbDAO<T> dao) throws LoginRequiredException {
		final User user = new User();
		user.setId(currentUserId());
		final Iterator<Integer> rows = results.keySet().iterator();
		while (rows.hasNext()) {
			final int row = rows.next();
			final T result = results.get(row);

			try {
				// validate(result);
				if (dao.isNew(result))
					resultCount.incrementFresh();
				else
					resultCount.incrementOld();
			} catch (HibernateException e) {
				resultCount.incrementInvalid();
				parseResults.addError(row, handleHibernateException(e));
			}
			if (save) {
				try {
					dao.save(result);
				} catch (HibernateException he) {
					parseResults.addError(row, handleHibernateException(he));
				} catch (MpDbException e) {
					parseResults.addError(row, e);
				}
			}
		}
	}

}

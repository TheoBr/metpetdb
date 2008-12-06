package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.HibernateException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.HasOwner;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.interfaces.PublicData;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

/**
 * Helper methods for all of the implementing bulk upload services
 * 
 * @author anthony
 * 
 */
public abstract class BulkUploadService extends MpDbServlet {

	private static final long serialVersionUID = 1L;

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
				.setParameter("user_id", currentUser()).setParameter("hash",
						fileOnServer).executeUpdate();
	}

	protected <T extends MObject> void analyzeResults(
			final Map<Integer, T> results,
			final BulkUploadResultCount resultCount,
			final BulkUploadResult parseResults, final boolean save,
			final MpDbDAO<T> dao) throws LoginRequiredException {
		final User user = new User();
		user.setId(currentUser());
		final Iterator<Integer> rows = results.keySet().iterator();
		while (rows.hasNext()) {
			final int row = rows.next();
			final T result = results.get(row);
			if (result instanceof HasOwner)
				((HasOwner) result).setOwner(user);
			if (result instanceof PublicData)
				((PublicData) result).setPublicData(false);
			try {
				//validate(result);
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
				} catch (DAOException e) {
					parseResults.addError(row, e);
				}
			}
		}
	}

}

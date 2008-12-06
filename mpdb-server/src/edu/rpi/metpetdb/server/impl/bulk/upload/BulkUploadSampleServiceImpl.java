package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.BulkUploadResultCount;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadSampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class BulkUploadSampleServiceImpl extends BulkUploadService implements
		BulkUploadSampleService {
	private static final long serialVersionUID = 1L;

	public BulkUploadResult parser(String fileOnServer, boolean save)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			if (save) {
				updateFile(fileOnServer);
			}
			final SampleParser sp = new SampleParser(new FileInputStream(
					MpDbServlet.getFileUploadPath() + fileOnServer));
			sp.parse();
			final Map<Integer, Sample> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			final Map<Integer, MpDbException> existingErrors = sp
					.getErrors();
			final Set<Integer> keys = existingErrors.keySet();
			final Iterator<Integer> itr = keys.iterator();
			while (itr.hasNext()) {
				final Integer i = itr.next();
				results.addError(i.intValue(), existingErrors.get(i));
			}
			User user = new User();
			user.setId(currentUser());
			final BulkUploadResultCount resultCount = new BulkUploadResultCount();
			final Iterator<Integer> rows = samples.keySet().iterator();
			while(rows.hasNext()) {
				final int row = rows.next();
				final Sample s = samples.get(row);
				s.setOwner(user);
				s.setPublicData(false);
				try {
					doc.validate(s);
					if (dao.isNew(s))
						resultCount.incrementFresh();
					else
						resultCount.incrementOld();
				} catch (MpDbException e) {
					resultCount.incrementInvalid();
					results.addError(row, e);
				} catch (HibernateException e) {
					resultCount.incrementInvalid();
					results.addError(row, handleHibernateException(e));
				}
				if (save) {
					try {
						dao.save(s);
					} catch (HibernateException he) {
						results.addError(row, handleHibernateException(he));
					} catch (DAOException e) {
						results.addError(row, e);
					}
				}
			} 
			results.addResultCount("Sample", resultCount);
			results.setHeaders(sp.getHeaders());
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

package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
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

	public void commit(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException, DAOException {
		currentSession()
				.createSQLQuery(
						"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
				.setParameter("user_id", currentUser()).setParameter("hash",
						fileOnServer).executeUpdate();
		SampleParser sp;
		try {
			sp = new SampleParser(new FileInputStream(MpDbServlet
					.getFileUploadPath()
					+ fileOnServer));
			sp.parse();
			final List<Sample> samples = sp.getSamples();
			User user = new User();
			user.setId(currentUser());
			for (Sample s : samples) {
				s.setOwner(user);
			}
			save(samples);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BulkUploadResult parser(String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();
			final User u = new User();
			u.setId(currentUser());
			final SampleParser sp = new SampleParser(new FileInputStream(
					MpDbServlet.getFileUploadPath() + fileOnServer));
			sp.parse();
			final List<Sample> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			errors.putAll(sp.getErrors());
			int row = 2;
			final BulkUploadResultCount resultCount = new BulkUploadResultCount();
			for (Sample s : samples) {
				s.setOwner(u);
				try {
					doc.validate(s);
					if (dao.isNew(s))
						resultCount.incrementFresh();
					else
						resultCount.incrementOld();
				} catch (ValidationException e) {
					resultCount.incrementInvalid();
					errors.put(row, e);
				}
				row = row + 1;
			}
			results.addResultCount("Sample", resultCount);
			results.setHeaders(sp.getHeaders());
			results.setErrors(errors);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
	}

	protected void save(final Collection<Sample> samples)
			throws ValidationException, LoginRequiredException, DAOException {
		for (Sample sample : samples) {
			doc.validate(sample);
			Sample s = (sample);
			s = (new SampleDAO(this.currentSession())).save(s);
		}
		commit();
	}

}

package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.server.bulk.upload.sample.SampleParser;

public class BulkUploadServiceImpl extends SampleServiceImpl implements
		BulkUploadService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder;

	public Map<Integer, ValidationException> saveSamplesFromSpreadsheet(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, SampleAlreadyExistsException {
		final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			sp.parse();
			final UserDTO u = (UserDTO) cloneBean(byId("User", currentUser()));
			final List<SampleDTO> samples = sp.getSamples();
			Integer i = 2;
			for (SampleDTO s : samples) {
				s.setOwner(u);
				try {
					doc.validate(s);
				} catch (ValidationException e) {
					errors.put(i, e);
				}
				++i;
			}

			if (errors.isEmpty())
				try {
					save(samples);
					return null;
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation.");
				}
		} catch (final NoSuchObjectException nsoe) {
			throw new LoginRequiredException();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return errors;
	}
	// TODO: implement this. It might need some real Hibernate objects.
	public Boolean deleteOldFiles() {
		List oldfiles = currentSession()
				.createSQLQuery(
						"SELECT * FROM uploaded_files WHERE AGE(time) > interval '7 days'")
				.list();
		return true;
	}
	public static void setBaseFolder(String baseFolder) {
		BulkUploadServiceImpl.baseFolder = baseFolder;
	}

}

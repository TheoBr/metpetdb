package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;

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

	public String saveSamplesFromSpreadsheet(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException,
			SampleAlreadyExistsException, ValidationException {
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer);
			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IOException e) {
				throw new InvalidFormatException();
			}

			sp.parse();
			final UserDTO u = (UserDTO) cloneBean(byId("User", currentUser()));
			final Set<SampleDTO> samples = sp.getSamples();
			// TODO maybe have sp.getSamples() take in the owner??
			for (SampleDTO s : samples)
				s.setOwner(u);

			try {
				save(samples);
			} catch (final SampleAlreadyExistsException saee) {
				throw saee;
			} catch (final ValidationException ve) {
				throw ve;
			} catch (final LoginRequiredException lre) {
				throw lre;
			}
		} catch (final NoSuchObjectException nsoe) {
			throw new LoginRequiredException();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return "Samples saved.";
	}

	public static void setBaseFolder(String baseFolder) {
		BulkUploadServiceImpl.baseFolder = baseFolder;
	}

}

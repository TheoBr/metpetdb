package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.BulkUploadResult;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadSampleService;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;

public class BulkUploadServiceImpl extends SampleServiceImpl implements
		BulkUploadSampleService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder;
	public void commit(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, DAOException {
		final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();

			// Locate and set Valid Potential Minerals for the parser
			List<Mineral> minerals = (new MineralDAO(this.currentSession())
					.getAll());
			SampleParser.setMinerals(minerals);

			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			sp.parse();
			errors.putAll(sp.getErrors());
			User user = new User();
			user.setId(currentUser());
			try {
				user = (new UserDAO(this.currentSession())).fill(user);
			} catch (DAOException daoe) {
				throw new LoginRequiredException();
			}
			final User u = (User) (user);
			final List<Sample> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			final List<Sample> replaceSamples = new ArrayList<Sample>();
			for (Sample s : samples) {
				final Sample s2 = (s);
				try {
					dao.replaceTransientObjects(s2);
					final Sample s3 = (s2);
					replaceSamples.add(s3);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			Integer i = 2;
			for (Sample s : replaceSamples) {
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
					save(replaceSamples);
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation.");
				}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

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
	
	public BulkUploadResult parser(String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final BulkUploadResult results = new BulkUploadResult();
		try {
			final Map<String, Integer[]> newAdditions = new HashMap<String, Integer[]>();
			final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();
			User user = new User();
			user.setId(currentUser());
			try {
				user = (new UserDAO(this.currentSession())).fill(user);
			} catch (DAOException daoe) {
				throw new LoginRequiredException();
			}
			final User u = (user);

			// Locate and set Valid Potential Minerals for the parser
			List<Mineral> minerals = (new MineralDAO(this.currentSession())
					.getAll());
			SampleParser.setMinerals(minerals);

			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			sp.parse();

			final List<Sample> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			final List<Sample> replaceSamples = new ArrayList<Sample>();
			for (Sample s : samples) {
				final Sample s2 = (s);
				try {
					dao.replaceTransientObjects(s2);
					final Sample s3 = (s2);
					replaceSamples.add(s3);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			// Find Valid, new Samples
			Integer[] sample_breakdown = {
					0, 0, 0
			};
			
			errors.putAll(sp.getErrors());
			
			Integer row = 2;

			for (Sample s : replaceSamples) {
				s.setOwner(u);
				try {
					doc.validate(s);
					Sample smpl = (s);
					if (dao.isNew(smpl))
						sample_breakdown[1]++;
					else
						sample_breakdown[2]++;
				} catch (ValidationException e) {
					sample_breakdown[0]++;
					errors.put(row, e);
				}
				row = row +1;
			}
			newAdditions.put("Sample", sample_breakdown);
			results.setHeaders(sp.getHeaders());
			results.setAdditions(newAdditions);
			results.setErrors(errors);
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
		return results;
	}

}

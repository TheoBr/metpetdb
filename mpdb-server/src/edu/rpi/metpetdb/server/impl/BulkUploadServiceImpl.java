package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.BulkUploadService;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.UserDAO;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.User;

public class BulkUploadServiceImpl extends SampleServiceImpl implements
		BulkUploadService {
	private static final long serialVersionUID = 1L;
	private static String baseFolder;

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			// Locate and set Valid Potential Minerals for the parser
			List<MineralDTO> minerals = cloneBean(new MineralDAO(this
					.currentSession()).getAll());
			SampleParser.setMinerals(minerals);

			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			return sp.getHeaders();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
	}
	public Map<String, Integer[]> getAdditions(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		try {
			final Map<String, Integer[]> newAdditions = new TreeMap<String, Integer[]>();
			User user = new User();
			user.setId(currentUser());
			try {
				user = (new UserDAO(this.currentSession())).fill(user);
			} catch (DAOException daoe) {
				throw new LoginRequiredException();
			}
			final UserDTO u = cloneBean(user);

			// Locate and set Valid Potential Minerals for the parser
			List<MineralDTO> minerals = cloneBean(new MineralDAO(this
					.currentSession()).getAll());
			SampleParser.setMinerals(minerals);

			final SampleParser sp = new SampleParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				sp.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			sp.parse();

			final List<SampleDTO> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			final List<SampleDTO> replaceSamples = new ArrayList<SampleDTO>();
			for(SampleDTO s : samples) {
				final Sample s2 = mergeBean(s);
				try {
					dao.replaceTransientObjects(s2);
					final SampleDTO s3 = cloneBean(s2);
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
			
			for (SampleDTO s : replaceSamples) {
				s.setOwner(u);
				try {
					doc.validate(s);
					Sample smpl = mergeBean(s);
					if (dao.isNew(smpl))
						sample_breakdown[1]++;
					else
						sample_breakdown[2]++;
				} catch (ValidationException e) {
					sample_breakdown[0]++;
				}
			}
			newAdditions.put("Sample", sample_breakdown);

			return newAdditions;
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	public Map<Integer, ValidationException> saveSamplesFromSpreadsheet(
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
			List<MineralDTO> minerals = cloneBean(new MineralDAO(this
					.currentSession()).getAll());
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
			final UserDTO u = (UserDTO) cloneBean(user);
			final List<SampleDTO> samples = sp.getSamples();
			final SampleDAO dao = new SampleDAO(this.currentSession());
			final List<SampleDTO> replaceSamples = new ArrayList<SampleDTO>();
			for(SampleDTO s : samples) {
				final Sample s2 = mergeBean(s);
				try {
					dao.replaceTransientObjects(s2);
					final SampleDTO s3 = cloneBean(s2);
					replaceSamples.add(s3);
				} catch (DAOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			Integer i = 2;
			for (SampleDTO s : replaceSamples) {
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
					return null;
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation.");
				}
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

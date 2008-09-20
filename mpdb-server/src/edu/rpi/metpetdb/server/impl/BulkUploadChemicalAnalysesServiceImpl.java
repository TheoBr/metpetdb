package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.ChemicalAnalysisNotFoundException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadChemicalAnalysesServiceImpl extends
		ChemicalAnalysisServiceImpl implements
		BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;
	public static String baseFolder;

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			if (AnalysisParser.areElementsAndOxidesSet()) {
				List<Element> elements = ((new ElementDAO(this.currentSession()))
						.getAll());
				List<Oxide> oxides = ((new OxideDAO(this.currentSession()))
						.getAll());
				AnalysisParser.setElementsAndOxides(elements, oxides);
			}

			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				ap.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			return ap.getHeaders();
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}
	}

	public Map<String, Integer[]> getAdditions(final String fileOnServer)
			throws InvalidFormatException, LoginRequiredException {
		final Map<String, Integer[]> newAdditions = new TreeMap<String, Integer[]>();

		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();

			if (AnalysisParser.areElementsAndOxidesSet()) {
				List<Element> elements = ((new ElementDAO(this.currentSession()))
						.getAll());
				List<Oxide> oxides = ((new OxideDAO(this.currentSession()))
						.getAll());
				AnalysisParser.setElementsAndOxides(elements, oxides);
			}

			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				ap.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ap.parse();
			final List<ChemicalAnalysis> analyses = ap.getAnalyses();

			Integer[] ca_breakdown = {
					0, 0, 0
			};
			Integer[] ss_breakdown = {
					0, 0, 0
			};
			Integer i = 2;

			Set<String> subsampleNames = new HashSet<String>();
			for (ChemicalAnalysis s : analyses) {

				// Minerals need id's so equality can be checked
				Mineral m = (s.getMineral());
				try {
					if (m != null)
						s.setMineral((Mineral) ((new MineralDAO(this
								.currentSession())).fill(m)));
				} catch (DAOException daoe) {
					// If something is wrong with getting the mineral from db,
					// force a validation error
					s.setMineral(new Mineral());
				}

				User u = new User();
				u.setId(currentUser());

				try {
					doc.validate(s);
					ChemicalAnalysis ca = (s);
					ca.getSubsample().getSample().setOwner(u);
					ca = (new ChemicalAnalysisDAO(this.currentSession()))
							.fill(ca);
					ca_breakdown[2]++;
				} catch (ChemicalAnalysisNotFoundException canfe) {
					ca_breakdown[1]++;
				} catch (ValidationException e) {
					ca_breakdown[0]++;
				} catch (DAOException daoe) {
					ca_breakdown[0]++;
				}

				try {
					ChemicalAnalysis ca = (s);
					ca.getSubsample().getSample().setOwner(u);
					ca = (new ChemicalAnalysisDAO(this.currentSession()))
							.populate(ca);

					Subsample ss = (ca.getSubsample());
					(new SubsampleDAO(this.currentSession())).fill(ss);
					if (!subsampleNames.contains(ss.getName())) {
						ss_breakdown[2]++;
						subsampleNames.add(ss.getName());
					}
				} catch (SubsampleNotFoundException snfe) {
					// If we couldn't find the subsample, then we'll add it
					ChemicalAnalysis ca = (s);
					if (!subsampleNames.contains(ca.getSubsample().getName())) {
						ss_breakdown[1]++;
						subsampleNames.add(ca.getSubsample().getName());
					}
				} catch (DAOException daoe) {
					ss_breakdown[0]++;
				}
				++i;
			}
			// TODO: These need to be gleaned from the Locale
			newAdditions.put("ChemicalAnalysis", ca_breakdown);
			newAdditions.put("Subsample", ss_breakdown);

		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return newAdditions;
	}
	public Map<Integer, ValidationException> saveAnalysesFromSpreadsheet(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, ValidationException, DAOException {
		final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			if (AnalysisParser.areElementsAndOxidesSet()) {
				List<Element> elements = ((new ElementDAO(this.currentSession()))
						.getAll());
				List<Oxide> oxides = ((new OxideDAO(this.currentSession()))
						.getAll());
				AnalysisParser.setElementsAndOxides(elements, oxides);
			}

			final AnalysisParser ap = new AnalysisParser(new FileInputStream(
					baseFolder + "/" + fileOnServer));
			try {
				ap.initialize();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IllegalStateException(
						"Programmer Error: No Such Method");
			}

			ap.parse();
			final List<ChemicalAnalysis> analyses = ap.getAnalyses();
			Integer i = 2; // if the spreadsheet had blank lines at the top,
			// the line numbers will be wrong accordingly.
			for (ChemicalAnalysis s : analyses) {

				// Minerals need id's so equality can be checked
				Mineral m = (s.getMineral());
				if (m != null)
					s.setMineral((Mineral) ((new MineralDAO(this
							.currentSession())).fill(m)));

				try {
					doc.validate(s);
				} catch (ValidationException e) {
					errors.put(i, e);
				}
				++i;
			}

			User u = new User();
			u.setId(currentUser());

			if (errors.isEmpty()) {
				// Insert new Subsamples as required
				SubsampleDAO ssDAO = new SubsampleDAO(this.currentSession());
				for (ChemicalAnalysis ca : analyses) {
					Subsample ss = (ca.getSubsample());
					ss.getSample().setOwner(u);

					try {
						ssDAO.fill(ss);
					} catch (SubsampleNotFoundException daoe) {
						doc.validate(ss);
						ssDAO.save(ss);
					}
				}

				// Save chemical analyses
				try {
					save(analyses);
					return null;
				} catch (final ValidationException e) {
					throw new IllegalStateException(
							"Objects passed and subsequently failed validation.");
				}
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return errors;
	}
	public static void setBaseFolder(String baseFolder) {
		BulkUploadChemicalAnalysesServiceImpl.baseFolder = baseFolder;
	}
}

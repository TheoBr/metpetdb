package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.ChemicalAnalysisNotFoundException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.bulk.upload.sample.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Subsample;
import edu.rpi.metpetdb.server.model.User;

public class BulkUploadChemicalAnalysesServiceImpl extends
		ChemicalAnalysisServiceImpl implements
		BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;
	public static String baseFolder;

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			if (AnalysisParser.areElementsAndOxidesSet()) {
				List<ElementDTO> elements = cloneBean((new ElementDAO(this
						.currentSession())).getAll());
				List<OxideDTO> oxides = cloneBean((new OxideDAO(this
						.currentSession())).getAll());
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
				List<ElementDTO> elements = cloneBean((new ElementDAO(this
						.currentSession())).getAll());
				List<OxideDTO> oxides = cloneBean((new OxideDAO(this
						.currentSession())).getAll());
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
			final List<ChemicalAnalysisDTO> analyses = ap.getAnalyses();

			Integer[] ca_breakdown = {
					0, 0, 0
			};
			Integer[] ss_breakdown = {
					0, 0, 0
			};
			Integer i = 2;

			for (ChemicalAnalysisDTO s : analyses) {

				// Minerals need id's so equality can be checked
				Mineral m = mergeBean(s.getMineral());
				try {
					if (m != null)
						s.setMineral((MineralDTO) cloneBean((new MineralDAO(
								this.currentSession())).fill(m)));
				} catch (DAOException daoe) {
					// If something is wrong with getting the mineral from db,
					// force a validation error
					s.setMineral(new MineralDTO());
				}

				User u = new User();
				u.setId(currentUser());

				try {
					doc.validate(s);
					ChemicalAnalysis ca = mergeBean(s);
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
					ChemicalAnalysis ca = mergeBean(s);
					ca.getSubsample().getSample().setOwner(u);
					ca = (new ChemicalAnalysisDAO(this.currentSession()))
							.populate(ca);

					Subsample ss = mergeBean(ca.getSubsample());
					(new SubsampleDAO(this.currentSession())).fill(ss);
					ss_breakdown[2]++;
				} catch (SubsampleNotFoundException snfe) {
					// If we couldn't find the subsample, then we'll add it
					ss_breakdown[1]++;
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
				List<ElementDTO> elements = cloneBean((new ElementDAO(this
						.currentSession())).getAll());
				List<OxideDTO> oxides = cloneBean((new OxideDAO(this
						.currentSession())).getAll());
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
			final List<ChemicalAnalysisDTO> analyses = ap.getAnalyses();
			Integer i = 2; // if the spreadsheet had blank lines at the top,
			// the line numbers will be wrong accordingly.
			for (ChemicalAnalysisDTO s : analyses) {

				// Minerals need id's so equality can be checked
				Mineral m = mergeBean(s.getMineral());
				if (m != null)
					s.setMineral((MineralDTO) cloneBean((new MineralDAO(this
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
				for (ChemicalAnalysisDTO caDTO : analyses) {
					Subsample ss = mergeBean(caDTO.getSubsample());
					ss.getSample().setOwner(u);

					try {
						ssDAO.fill(ss);
					} catch (SubsampleNotFoundException daoe) {
						SubsampleDTO ssDTO = cloneBean(ss);
						doc.validate(ssDTO);
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

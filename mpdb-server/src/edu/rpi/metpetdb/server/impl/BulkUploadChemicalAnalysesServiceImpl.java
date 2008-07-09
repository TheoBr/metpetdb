package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.bulk.upload.sample.AnalysisParser;
import edu.rpi.metpetdb.server.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Subsample;

public class BulkUploadChemicalAnalysesServiceImpl extends
		ChemicalAnalysisServiceImpl implements
		BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;
	public static String baseFolder;

	public Map<Integer, String[]> getHeaderMapping(final String fileOnServer)
			throws InvalidFormatException {
		try {
			AnalysisParser.setElementsAndOxides(cloneBean(currentSession()
					.getNamedQuery("Element.all").list()),
					cloneBean(currentSession().getNamedQuery("Oxide.all")
							.list()));
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
			// if (!AnalysisParser.areElementsAndOxidesSet())
			AnalysisParser.setElementsAndOxides(cloneBean(currentSession()
					.getNamedQuery("Element.all").list()),
					cloneBean(currentSession().getNamedQuery("Oxide.all")
							.list()));
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
				final Query minerals = namedQuery("Mineral.byName");
				minerals.setString("name", s.getMineral().getName());
				if (minerals.uniqueResult() != null) {
					Mineral m = (Mineral) minerals.uniqueResult();
					s.getMineral().setParentId(m.getParentId());
					s.getMineral().setId(m.getId());
				}

				try {
					doc.validate(s);
					ChemicalAnalysis ca = mergeBean(s);
					replaceSample(ca);

					if (isNewCA(ca))
						ca_breakdown[1]++;
					else
						ca_breakdown[2]++;
				} catch (ValidationException e) {
					ca_breakdown[0]++;
				}

				try {
					ChemicalAnalysis ca = mergeBean(s);
					replaceSample(ca);
					Subsample ss = mergeBean(ca.getSubsample());
					if (ss == null || ss.mIsNew())
						ss_breakdown[1]++;
					else
						ss_breakdown[2]++;
				} catch (ValidationException e) {
				}
				++i;
			}
			newAdditions.put("ChemicalAnalysis", ca_breakdown);
			newAdditions.put("Subsample", ss_breakdown);

		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		}

		return newAdditions;
	}
	public Map<Integer, ValidationException> saveAnalysesFromSpreadsheet(
			final String fileOnServer) throws InvalidFormatException,
			LoginRequiredException, ValidationException {
		final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();
		try {
			currentSession()
					.createSQLQuery(
							"UPDATE uploaded_files SET user_id = :user_id WHERE hash = :hash")
					.setParameter("user_id", currentUser()).setParameter(
							"hash", fileOnServer).executeUpdate();
			// if (!AnalysisParser.areElementsAndOxidesSet())
			AnalysisParser.setElementsAndOxides(cloneBean(currentSession()
					.getNamedQuery("Element.all").list()),
					cloneBean(currentSession().getNamedQuery("Oxide.all")
							.list()));
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
				final Query minerals = namedQuery("Mineral.byName");
				minerals.setString("name", s.getMineral().getName());
				if (minerals.uniqueResult() != null) {
					Mineral m = (Mineral) minerals.uniqueResult();
					s.getMineral().setParentId(m.getParentId());
					s.getMineral().setId(m.getId());
				}

				try {
					doc.validate(s);
				} catch (ValidationException e) {
					errors.put(i, e);
				}
				++i;
			}

			if (errors.isEmpty())
				try {
					save(analyses);
					return null;
				} catch (final ValidationException e) {
					// throw new IllegalStateException(
					// "Objects passed and subsequently failed validation.");
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

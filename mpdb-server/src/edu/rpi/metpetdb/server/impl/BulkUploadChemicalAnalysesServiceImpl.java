package edu.rpi.metpetdb.server.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.service.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.bulk.upload.sample.AnalysisParser;

public class BulkUploadChemicalAnalysesServiceImpl extends
		ChemicalAnalysisServiceImpl implements
		BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;
	public static String baseFolder;

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

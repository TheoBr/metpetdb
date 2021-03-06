package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadChemicalAnalysesService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadChemicalAnalysesServiceImpl extends BulkUploadService
		implements BulkUploadChemicalAnalysesService {

	public static final long serialVersionUID = 1L;

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			GeoReferenceDAO geoDao,
			final Map<String, Collection<String>> subsampleNames,
			final Map<String, Sample> samples,
			final Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		final AnalysisParser ap = new AnalysisParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + fileOnServer));
		ap.parse();
		final Map<Integer, ChemicalAnalysis> analyses = ap
				.getChemicalAnalyses();
		// Handle any existing errors found
		addErrors(ap, results);
		addWarnings(ap, results);
		final BulkUploadResultCount caResultCount = new BulkUploadResultCount();
		final BulkUploadResultCount ssResultCount = new BulkUploadResultCount();
		final Iterator<Integer> rows = analyses.keySet().iterator();
		final ChemicalAnalysisDAO caDao = new ChemicalAnalysisDAO(
				currentSession());
		final ImageDAO imageDao = new ImageDAO(currentSession());
		results.setHeaders(ap.getHeaders());
		while (rows.hasNext()) {
			int row = rows.next();
			final ChemicalAnalysis ca = analyses.get(row);
			initObject(ca);
			try {
				// see if our subsample exists
				if (ca.getSubsample() == null) {
					results.addError(row, new PropertyRequiredException(
							"Subsample"));
					continue;
				}
				// see if our sample exists
				if (ca.getSubsample().getSample() == null) {
					results.addError(row, new PropertyRequiredException(
							"Sample"));
					continue;
				}
				Sample s = ca.getSubsample().getSample();
				checkForSample(s, samples, sampleDao, results, subsampleNames,
						ssDao, subsamples,row);
				Subsample ss = (ca.getSubsample());
				ss.setSample(s);
				if (ss != null && s != null  && ss.getName() != null) {
					// if we don't have the name stored already we need
					// to load the subsample
					ca.setSubsample(checkForSubsample(s, ss, samples, ssDao,
							results, subsampleNames, row, subsamples,
							ssResultCount, save));
				} else {
					// Every ChemicalAnalysis needs a subsample so add an error
					results.addError(row, new PropertyRequiredException(
							"Subsample"));
				}
				if (ca.getImage() != null){
					ca.setImage(checkForImage(ss.getSample(),ca.getSubsample(),ca.getImage(),imageDao,results,row));
				}
				doc.validate(ca);
				if (caDao.isNew(ca))
					caResultCount.incrementFresh();
				else
					caResultCount.incrementOld();
				if (save)
					caDao.save(ca);
			} catch (Exception e) {
				results.addError(row, getNiceException(e));
				caResultCount.incrementInvalid();
			}
			++row;
		}
		results.addResultCount("Chemical Analysis", caResultCount);
		results.addResultCount("Subsamples", ssResultCount);
	}

	public BulkUploadResult imageZipUploadImpl(String spreadsheetFile, String imageFile, boolean save) 
		throws InvalidFormatException, LoginRequiredException, MpDbException {
		return null;
	}
}

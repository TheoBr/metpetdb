package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.validation.PropertyRequiredException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadReferencesService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.ReferenceParser;
import edu.rpi.metpetdb.server.bulk.upload.SampleParser;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadReferencesServiceImpl extends BulkUploadService implements
	BulkUploadReferencesService {

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			Map<String, Collection<String>> subsampleNames,
			Map<String, Sample> cachedSamples, Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		
		final ReferenceParser rp = new ReferenceParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + fileOnServer));
		rp.parse();
		results.setHeaders(rp.getHeaders());
		final Map<Integer, GeoReference> references = rp.getReferences();
		addErrors(rp, results);
		addWarnings(rp, results);
		final BulkUploadResultCount resultCount = new BulkUploadResultCount();
		final Iterator<Integer> rows = references.keySet().iterator();
		while (rows.hasNext()) {
			final int row = rows.next();
			final GeoReference gr = references.get(row);
			initObject(gr);
			/*try {
				//GeoReferenceDAO geoDao = new GeoReferenceDAO()
				if (geoDao.isNew(gr))
					resultCount.incrementFresh();
				else
					resultCount.incrementOld();
				if (save)
					sampleDao.save(s);
			} catch (Exception e) {
				resultCount.incrementInvalid();
				results.addError(row, getNiceException(e, s));
			}*/
		}
		results.addResultCount("Reference", resultCount);
	}

}

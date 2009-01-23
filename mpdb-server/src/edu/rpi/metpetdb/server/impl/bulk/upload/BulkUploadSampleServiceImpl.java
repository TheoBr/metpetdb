package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResultCount;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadSampleService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.NewSampleParser;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadSampleServiceImpl extends BulkUploadService implements
		BulkUploadSampleService {
	private static final long serialVersionUID = 1L;

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			final Map<String, Collection<String>> subsampleNames,
			final Map<String, Sample> cachedSamples,
			final Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		final NewSampleParser sp = new NewSampleParser(new FileInputStream(
				MpDbServlet.getFileUploadPath() + fileOnServer));
		sp.parse();
		results.setHeaders(sp.getHeaders());
		final Map<Integer, Sample> samples = sp.getSamples();
		addErrors(sp, results);
		final BulkUploadResultCount resultCount = new BulkUploadResultCount();
		final Iterator<Integer> rows = samples.keySet().iterator();
		while (rows.hasNext()) {
			final int row = rows.next();
			final Sample s = samples.get(row);
			initObject(s);
			try {
				if (sampleDao.isNew(s))
					resultCount.incrementFresh();
				else
					resultCount.incrementOld();
				if (save)
					sampleDao.save(s);
			} catch (Exception e) {
				resultCount.incrementInvalid();
				results.addError(row, getNiceException(e, s));
			}
		}
		results.addResultCount("Sample", resultCount);
	}
}

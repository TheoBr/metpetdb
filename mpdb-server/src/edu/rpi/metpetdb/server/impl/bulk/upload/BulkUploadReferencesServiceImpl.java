package edu.rpi.metpetdb.server.impl.bulk.upload;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Map;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadResult;
import edu.rpi.metpetdb.client.service.bulk.upload.BulkUploadReferencesService;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;
import edu.rpi.metpetdb.server.dao.impl.SubsampleDAO;

public class BulkUploadReferencesServiceImpl extends BulkUploadService implements
	BulkUploadReferencesService{

	@Override
	public void parserImpl(String fileOnServer, boolean save,
			BulkUploadResult results, SampleDAO sampleDao, SubsampleDAO ssDao,
			Map<String, Collection<String>> subsampleNames,
			Map<String, Sample> samples, Map<String, Subsample> subsamples)
			throws FileNotFoundException, MpDbException, LoginRequiredException {
		// TODO Auto-generated method stub
		
	}

}

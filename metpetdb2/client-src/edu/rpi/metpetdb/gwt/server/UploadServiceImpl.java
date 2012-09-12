package edu.rpi.metpetdb.gwt.server;

import edu.rpi.metpetdb.gwt.client.UploadService;
import edu.rpi.metpetdb.gwt.client.dto.UploadRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;
import edu.rpi.metpetdb.server.ReferenceUploadZipHelper;

public class UploadServiceImpl implements UploadService {

	private ReferenceUploadZipHelper referenceUploadZipHelper;

	@Override
	public String upload(UploadRequestDTO uploadRequest, UserDTO user) {
	
		return referenceUploadZipHelper.upload(uploadRequest.getFile(), uploadRequest.getFileName());
		
	}

	public ReferenceUploadZipHelper getReferenceUploadZipHelper() {
		return referenceUploadZipHelper;
	}

	public void setReferenceUploadZipHelper(
			ReferenceUploadZipHelper referenceUploadZipHelper) {
		this.referenceUploadZipHelper = referenceUploadZipHelper;
	}



}

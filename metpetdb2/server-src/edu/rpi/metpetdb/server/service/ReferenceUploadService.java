package edu.rpi.metpetdb.server.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.server.ServerValidator;

import edu.rpi.metpetdb.server.dao.ReferenceDAO;
import edu.rpi.metpetdb.server.model.Georeference;
import edu.rpi.metpetdb.server.model.Reference;
import edu.rpi.metpetdb.server.model.Sample;

public class ReferenceUploadService {

	private ReferenceDAO refDAO = null;

	@Transactional
	public void upload(String referenceString) {
		
		
		Georeference geoRef = new Georeference(referenceString);
		Reference ref = refDAO.lookupReference(geoRef);
		

		if (ref == null)
		{
		ref = new Reference(null, geoRef.getReferenceNumber());
		}
		
		ref.setGeoreference(geoRef);
		geoRef.setReference(ref);
		
		ServerValidator<Georeference> sampleValidator = new ServerValidator<Georeference>();
		Set<InvalidConstraint<Georeference>> geoConstraints = sampleValidator
				.validate(ref.getGeoreference());

		if (geoConstraints.size() > 0) 
		{
			StringBuffer buff = new StringBuffer();
			
			buff.append("geoRef: " + referenceString);
			for (InvalidConstraint<Georeference> currConstraint : geoConstraints)
			{
				buff.append(currConstraint.getItemName() + " " + currConstraint.getMessage() + "\r\n");
			}
			throw new IllegalArgumentException(buff.toString());
		}
		
		
		refDAO.saveGeoreference(ref);
	}

	/**
	 * Currently unused, but it might be handy for testing
	 * 
	 * @param currFile
	 * @return
	 */
	public String loadReferenceFile(File currFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try {

			fis = new FileInputStream(currFile);
			baos = new ByteArrayOutputStream();
			byte[] chunk = new byte[2048];

			int ready = 0;
			while ((ready = fis.read(chunk)) > 0) {
				baos.write(chunk, 0, chunk.length);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				baos.flush();
				baos.close();
				fis.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new String(baos.toByteArray());
	}

	public ReferenceDAO getRefDAO() {
		return refDAO;
	}

	public void setRefDAO(ReferenceDAO refDAO) {
		this.refDAO = refDAO;
	}

}

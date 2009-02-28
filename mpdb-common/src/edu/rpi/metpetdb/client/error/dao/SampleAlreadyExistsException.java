package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public class SampleAlreadyExistsException extends DAOException {
	private static final long serialVersionUID = 1L;
	private String additionalInformation;

	public void handleObject(final MObject object) {
		if (object instanceof Sample) {
			additionalInformation = " duplicate number of "
					+ ((Sample) object).getNumber();
		}
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_SampleAlreadyExists()
				+ additionalInformation == null ? "" : additionalInformation;
	}

}

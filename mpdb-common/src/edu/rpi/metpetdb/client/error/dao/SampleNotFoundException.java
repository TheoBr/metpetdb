package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;

public class SampleNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;
	private String number;
	private long id;
	
	public SampleNotFoundException() {
		number = "";
		id = 0;
	}
	
	public SampleNotFoundException(final Sample tryToLoad) {
		number = tryToLoad.getNumber();
		id = tryToLoad.getId();
	}

	@Override
	public String format() {
		if (number != null && number.length() > 0)
			return LocaleHandler.lc_text.errorDesc_SampleNotFoundNumber(number);
		else if (id != 0)
			return LocaleHandler.lc_text.errorDesc_SampleNotFoundId(id);
		else
			return LocaleHandler.lc_text.errorDesc_SampleNotFound();
	}

}

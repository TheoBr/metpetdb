package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class ElementNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_ElementNotFound();
	}

}

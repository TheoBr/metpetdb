package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class GenericDAOException extends DAOException {
	private static final long serialVersionUID = 1L;

	public GenericDAOException() {
		super();
	}

	public GenericDAOException(final String message) {
		super(message);
	}

	@Override
	public String format() {
		return LocaleHandler.lc_text.errorDesc_GenericDAO(msg);
	}

}

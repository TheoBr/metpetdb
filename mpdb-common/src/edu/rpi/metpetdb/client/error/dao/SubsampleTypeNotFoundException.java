package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;

public class SubsampleTypeNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		// return LocaleHandler.lc_text.errorDesc_MineralNotFound();
		return "subsample type not found needs an error message";
	}

}

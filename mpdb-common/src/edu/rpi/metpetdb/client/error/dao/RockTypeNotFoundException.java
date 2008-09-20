package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;

public class RockTypeNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		// return LocaleHandler.lc_text.errorDesc_MineralNotFound();
		return "rock type not found needs an error message";
	}

}

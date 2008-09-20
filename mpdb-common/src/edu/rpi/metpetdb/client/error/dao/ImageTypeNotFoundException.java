package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;

public class ImageTypeNotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		// return LocaleHandler.lc_text.errorDesc_MineralNotFound();
		return "image type error needs an error message";
	}

}

package edu.rpi.metpetdb.client.error.security;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class CannotLoadOtherUsersInformationException extends DAOException implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		return LocaleHandler.lc_text.Cannot_Load_Other_Users();
	}

}

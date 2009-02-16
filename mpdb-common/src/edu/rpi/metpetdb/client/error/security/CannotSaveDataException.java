package edu.rpi.metpetdb.client.error.security;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class CannotSaveDataException extends DAOException implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		return LocaleHandler.lc_text.Cannot_Save_Data();
	}
	

}

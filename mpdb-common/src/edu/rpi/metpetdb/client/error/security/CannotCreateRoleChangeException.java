package edu.rpi.metpetdb.client.error.security;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.DAOException;

public class CannotCreateRoleChangeException extends DAOException implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		return "CannotCreateRoleChange";
	}

}

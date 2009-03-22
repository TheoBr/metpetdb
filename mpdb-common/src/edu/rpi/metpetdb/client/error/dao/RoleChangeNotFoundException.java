package edu.rpi.metpetdb.client.error.dao;

import edu.rpi.metpetdb.client.error.DAOException;

public class RoleChangeNotFoundException extends DAOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String format() {
		//FIXME need an error message here
		return "";
	}

}

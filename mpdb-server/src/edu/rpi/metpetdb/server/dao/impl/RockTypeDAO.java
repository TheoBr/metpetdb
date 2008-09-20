package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.RockTypeNotFoundException;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class RockTypeDAO extends MpDbDAO<RockType> {

	public RockTypeDAO(Session session) {
		super(session);
	}

	@Override
	public RockType delete(RockType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public RockType fill(RockType m) throws DAOException {
		// Use Mineral Name
		final Query rockTypes = namedQuery("RockType.byRockType");
		rockTypes.setString("rockType", m.getRockType());
		if (rockTypes.uniqueResult() != null)
			return (RockType) rockTypes.uniqueResult();

		throw new RockTypeNotFoundException();
	}

	@Override
	public RockType save(RockType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
}

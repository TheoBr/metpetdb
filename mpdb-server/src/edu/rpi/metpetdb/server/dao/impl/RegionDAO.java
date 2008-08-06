package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.RegionNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.Region;

public class RegionDAO extends MpDbDAO<Region> {

	public RegionDAO(Session session) {
		super(session);
	}

	@Override
	public Region delete(Region inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Region fill(Region reg) throws DAOException {
		// Use Region Name
		final org.hibernate.Query regions = namedQuery("Region.byName");
		regions.setString("name", reg.getName());
		if (regions.uniqueResult() != null)
			return (Region) regions.uniqueResult();

		throw new RegionNotFoundException();
	}

	@Override
	public Region save(Region inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

}

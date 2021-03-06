package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.RegionNotFoundException;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class RegionDAO extends MpDbDAO<Region> {

	public RegionDAO(Session session) {
		super(session);
	}

	@Override
	public Region delete(Region inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Region fill(Region reg) throws MpDbException {
		// Use Region Name
		final org.hibernate.Query regions = namedQuery("Region.byName");
		regions.setString("name", reg.getName());
		if (regions.uniqueResult() != null)
			return (Region) regions.uniqueResult();

		throw new RegionNotFoundException();
	}

	@Override
	public Region save(Region inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allNames() throws MpDbException{
		final Query q = namedQuery("Region.all/name");
		List<Region> allRegions = (List<Region>)getResults(q);
		return	allRegions.toArray();
	}

}

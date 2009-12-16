package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.MetamorphicRegionNotFoundException;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class MetamorphicRegionDAO extends MpDbDAO<MetamorphicRegion> {

	public MetamorphicRegionDAO(Session session) {
		super(session);
	}

	@Override
	public MetamorphicRegion delete(MetamorphicRegion inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public MetamorphicRegion fill(MetamorphicRegion mr) throws MpDbException {
		// Use Name
		final org.hibernate.Query mregions = namedQuery("MetamorphicRegion.byName");
		mregions.setString("name", mr.getName());
		if (mregions.uniqueResult() != null)
			return (MetamorphicRegion) mregions.uniqueResult();

		throw new MetamorphicRegionNotFoundException();
	}

	@Override
	public MetamorphicRegion save(MetamorphicRegion inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allMetamorphicRegions() throws MpDbException{
		final Query q = namedQuery("MetamorphicRegion.all/name");
		return	((List<MetamorphicRegion>)getResults(q)).toArray();
	}

}

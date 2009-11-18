package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ReferenceNotFoundException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class GeoReferenceDAO extends MpDbDAO<GeoReference> {

	public GeoReferenceDAO(Session session) {
		super(session);
	}

	@Override
	public GeoReference delete(GeoReference inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public GeoReference fill(GeoReference ref) throws MpDbException {
		// Use Reference Name
		final org.hibernate.Query geoRefrences = namedQuery("GeoReference.byId");
		geoRefrences.setShort("id", ref.getId());
		if (geoRefrences.uniqueResult() != null)
			return (GeoReference) geoRefrences.uniqueResult();

		throw new ReferenceNotFoundException();
	}

	@Override
	public GeoReference save(GeoReference inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allReferences()throws MpDbException {
		final Query q = namedQuery("GeoReference.all/name");
		return ((List<GeoReference>)getResults(q)).toArray();
	}
}
package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ReferenceNotFoundException;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ReferenceDAO extends MpDbDAO<Reference> {

	public ReferenceDAO(Session session) {
		super(session);
	}

	@Override
	public Reference delete(Reference inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Reference fill(Reference ref) throws MpDbException {
		// Use Reference Name
		final org.hibernate.Query refrences = namedQuery("Reference.byName");
		refrences.setString("name", ref.getName());
		if (refrences.uniqueResult() != null)
			return (Reference) refrences.uniqueResult();

		throw new ReferenceNotFoundException();
	}

	@Override
	public Reference save(Reference inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allReferences()throws MpDbException {
		final Query q = namedQuery("Reference.all/name");
		return ((List<Reference>)getResults(q)).toArray();
	}

}

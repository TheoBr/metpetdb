package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ReferenceNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.Reference;

public class ReferenceDAO extends MpDbDAO<Reference> {

	public ReferenceDAO(Session session) {
		super(session);
	}

	@Override
	public Reference delete(Reference inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Reference fill(Reference ref) throws DAOException {
		// Use Reference Name
		final org.hibernate.Query refrences = namedQuery("Reference.byName");
		refrences.setString("name", ref.getName());
		if (refrences.uniqueResult() != null)
			return (Reference) refrences.uniqueResult();

		throw new ReferenceNotFoundException();
	}

	@Override
	public Reference save(Reference inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

}

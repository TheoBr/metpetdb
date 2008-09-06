package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.MineralNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.SubsampleType;

public class SubsampleTypeDAO extends MpDbDAO<SubsampleType> {

	public SubsampleTypeDAO(Session session) {
		super(session);
	}

	@Override
	public SubsampleType delete(SubsampleType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public SubsampleType fill(SubsampleType m) throws DAOException {
		// Use Mineral Name
		final Query subsampleTypes = namedQuery("SubsampleType.bySubsampleType");
		subsampleTypes.setString("subsampleType", m.getSubsampleType());
		if (subsampleTypes.uniqueResult() != null)
			return (SubsampleType) subsampleTypes.uniqueResult();

		// FIXME
		throw new MineralNotFoundException();
	}

	@Override
	public SubsampleType save(SubsampleType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
}

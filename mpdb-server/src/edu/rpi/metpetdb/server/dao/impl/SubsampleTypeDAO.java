package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class SubsampleTypeDAO extends MpDbDAO<SubsampleType> {

	public SubsampleTypeDAO(Session session) {
		super(session);
	}

	@Override
	public SubsampleType delete(SubsampleType inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public SubsampleType fill(SubsampleType m) throws MpDbException {
		// Use Mineral Name
		final Query subsampleTypes = namedQuery("SubsampleType.bySubsampleType");
		subsampleTypes.setString("subsampleType", m.getSubsampleType());
		if (subsampleTypes.uniqueResult() != null)
			return (SubsampleType) subsampleTypes.uniqueResult();

		throw new SubsampleNotFoundException();
	}

	@Override
	public SubsampleType save(SubsampleType inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
}

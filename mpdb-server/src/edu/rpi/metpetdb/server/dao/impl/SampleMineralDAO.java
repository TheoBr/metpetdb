package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class SampleMineralDAO extends MpDbDAO<SampleMineral> {

	public SampleMineralDAO(Session session) {
		super(session);
	}

	@Override
	public SampleMineral delete(SampleMineral inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public SampleMineral fill(SampleMineral sm) throws MpDbException {
		// Fill Mineral
		sm.setMineral((new MineralDAO(sess)).fill(sm.getMineral()));

		return sm;
	}

	@Override
	public SampleMineral save(SampleMineral inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

}

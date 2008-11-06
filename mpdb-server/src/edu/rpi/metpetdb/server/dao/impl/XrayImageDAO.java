package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class XrayImageDAO extends MpDbDAO<XrayImage> {

	public XrayImageDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public XrayImage delete(XrayImage inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public XrayImage fill(XrayImage inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public XrayImage save(XrayImage inst) throws DAOException {
		inst.setSample((new SampleDAO(sess)).fill(inst.getSample()));
		inst.setSubsample((new SubsampleDAO(sess)).fill(inst.getSubsample()));
		inst.setImageType(new ImageTypeDAO(sess).fill(inst.getImageType()));
		inst = _save(inst);
		return inst;
	}

}

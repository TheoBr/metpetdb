package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class InviteDAO extends MpDbDAO<Invite> {

	public InviteDAO(Session session) {
		super(session);
	}

	@Override
	public Invite delete(Invite inst) throws MpDbException{
		throw new FunctionNotImplementedException();
	}

	@Override
	public Invite fill(Invite inst) throws MpDbException{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Invite save(Invite inst) throws MpDbException{
		inst = _save(inst);
		return inst;
	}
	
	
}
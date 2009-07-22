package edu.rpi.metpetdb.server.dao.impl;

import java.sql.Timestamp;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.Invite;
import edu.rpi.metpetdb.client.model.Project;
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
		Query q = namedQuery("Invite.byProjectIdMemberId");
		q.setInteger("project_id", inst.getProject_id());
		q.setInteger("user_id", inst.getUser_id());
		return (Invite) getResult(q);
	}

	@Override
	public Invite save(Invite inst) throws MpDbException{
		inst = _save(inst);
		return inst;
	}
}
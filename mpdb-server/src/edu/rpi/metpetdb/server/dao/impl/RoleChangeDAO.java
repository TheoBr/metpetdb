package edu.rpi.metpetdb.server.dao.impl;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.RoleChangeNotFoundException;
import edu.rpi.metpetdb.client.model.RoleChange;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class RoleChangeDAO extends MpDbDAO<RoleChange> {

	public RoleChangeDAO(Session session) {
		super(session);
	}

	@Override
	public RoleChange delete(RoleChange inst) throws MpDbException,
			HibernateException {
		_delete(inst);
		return null;
	}

	@Override
	public RoleChange fill(RoleChange inst) throws MpDbException,
			HibernateException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("RoleChange.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (RoleChange) getResult(q);
		}
		throw new RoleChangeNotFoundException();
	}
	
	public RoleChange forUser(User u) throws MpDbException {
		final Query q = namedQuery("RoleChange.byUserId");
		q.setLong("id", u.getId());
		if (getResult(q) != null)
			return (RoleChange) getResult(q);
		throw new RoleChangeNotFoundException();
	}
	
	public Collection<RoleChange> forSponsor(User u) throws MpDbException {
		final Query q = namedQuery("RoleChange.bySponsorId");
		q.setLong("id", u.getId());
		if (getResults(q) != null)
			return getResults(q);
		throw new RoleChangeNotFoundException();
	}

	@Override
	public RoleChange save(RoleChange inst) throws MpDbException,
			HibernateException {
		return _save(inst);
	}

}

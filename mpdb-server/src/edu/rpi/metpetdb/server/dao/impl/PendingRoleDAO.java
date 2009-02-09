package edu.rpi.metpetdb.server.dao.impl;

import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.PendingRoleNotFoundException;
import edu.rpi.metpetdb.client.model.PendingRole;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class PendingRoleDAO extends MpDbDAO<PendingRole> {

	public PendingRoleDAO(Session session) {
		super(session);
	}

	@Override
	public PendingRole delete(PendingRole inst) throws MpDbException,
			HibernateException {
		_delete(inst);
		return null;
	}

	@Override
	public PendingRole fill(PendingRole inst) throws MpDbException,
			HibernateException {
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("PendingRole.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (PendingRole) getResult(q);
		}
		throw new PendingRoleNotFoundException();
	}
	
	public PendingRole forUser(User u) throws MpDbException {
		final Query q = namedQuery("PendingRole.byUserId");
		q.setLong("id", u.getId());
		if (getResult(q) != null)
			return (PendingRole) getResult(q);
		throw new PendingRoleNotFoundException();
	}
	
	public Collection<PendingRole> forSponsor(User u) throws MpDbException {
		final Query q = namedQuery("PendingRole.bySponsorId");
		q.setLong("id", u.getId());
		if (getResults(q) != null)
			return getResults(q);
		throw new PendingRoleNotFoundException();
	}

	@Override
	public PendingRole save(PendingRole inst) throws MpDbException,
			HibernateException {
		return _save(inst);
	}

}

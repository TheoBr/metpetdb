package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.UserNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.User;

public class UserDAO extends MpDbDAO<User> {

	public UserDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public User delete(User inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public User fill(User inst) throws DAOException {
		// Use Id
		if (inst.getId() > 0) {
			Query q = namedQuery("User.byId");
			q.setParameter("id", inst.getId());
			if (q.uniqueResult() != null)
				return (User) q.uniqueResult();
		}

		// Use Name
		if (inst.getUsername() != null) {
			Query q = namedQuery("User.byUsername");
			q.setParameter("username", inst.getUsername());
			if (q.uniqueResult() != null)
				return (User) q.uniqueResult();
		}

		throw new UserNotFoundException();
	}

	@Override
	public User save(User inst) throws DAOException {
		inst = _save(inst);
		return inst;
	}

}

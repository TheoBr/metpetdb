package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.UserNotFoundException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

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
			if (getResult(q) != null)
				return (User) getResult(q);
		}

		// Use Name
		if (inst.getEmailAddress() != null) {
			Query q = namedQuery("User.byEmailAddress");
			q.setParameter("emailAddress", inst.getEmailAddress());
			if (getResult(q) != null)
				return (User) getResult(q);
		}

		throw new UserNotFoundException();
	}

	public Object[] allNames() throws DAOException{
		final Query q = namedQuery("User.all/name");
		return	((List<String>)getResults(q)).toArray();
	}
	@Override
	public User save(User inst) throws DAOException {
		inst = _save(inst);
		return inst;
	}

}

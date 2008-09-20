package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class OxideDAO extends MpDbDAO<Oxide> {

	public OxideDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Oxide delete(Oxide inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Oxide fill(Oxide inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Oxide save(Oxide inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	public List<Oxide> getAll() {
		final Query q = namedQuery("Oxide.all");
		return q.list();
	}
}

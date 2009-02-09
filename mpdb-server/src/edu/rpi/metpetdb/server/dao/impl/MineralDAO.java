package edu.rpi.metpetdb.server.dao.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.MineralNotFoundException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class MineralDAO extends MpDbDAO<Mineral> {

	public MineralDAO(Session session) {
		super(session);
	}

	@Override
	public Mineral delete(Mineral inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Mineral fill(final Mineral m) throws MpDbException {
		if (m != null) {
			// Use Mineral Name
			final Query minerals = namedQuery("Mineral.byName");
			minerals.setString("name", m.getName());
			if (minerals.uniqueResult() != null)
				return (Mineral) minerals.uniqueResult();
	
			throw new MineralNotFoundException();
		} else {
			return null;
		}
	}

	@Override
	public Mineral save(Mineral inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	public List<Mineral> getAll() throws MpDbException {
		final List<Mineral> l = new LinkedList<Mineral>();
		final Query parents = namedQuery("Mineral.all");
		final Query children = namedQuery("Mineral.children");
		l.addAll((Collection<? extends Mineral>) getResults(parents));
		l.addAll((Collection<? extends Mineral>) getResults(children));
		return l;
	}

}

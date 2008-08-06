package edu.rpi.metpetdb.server.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.GridNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.Grid;

public class GridDAO extends MpDbDAO<Grid> {

	public GridDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Grid delete(Grid inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Grid fill(Grid inst) throws DAOException {
		// By ID
		if (inst.getId() > 0) {
			final Query q = namedQuery("Grid.byId");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (Grid) q.uniqueResult();
		}

		// By Subsample Id
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("Grid.bySubsampleId");
			q.setParameter("id", inst.getSubsample().getId());
			if (q.uniqueResult() != null)
				return (Grid) q.uniqueResult();
		}

		throw new GridNotFoundException();
	}

	@Override
	public Grid save(Grid inst) throws DAOException {
		// Right now there is just one grid per subsample, so if a grid already
		// exists for the subsample, we're going to overwrite it
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("Grid.bySubsampleId");
			q.setParameter("id", inst.getSubsample().getId());
			if (q.uniqueResult() != null) {
				inst.setId(((Grid) q.uniqueResult()).getId());
			}
		}

		inst = _save(inst);
		return inst;
	}

}

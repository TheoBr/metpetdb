package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.GridNotFoundException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class GridDAO extends MpDbDAO<Grid> {

	public GridDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Grid delete(Grid inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Grid fill(Grid inst) throws MpDbException {
		// By ID
		if (inst.getId() > 0) {
			final Query q = namedQuery("Grid.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null) {
				Grid g = (Grid) getResult(q);
				g.setSubsample(new SubsampleDAO(sess).fill(g.getSubsample()));
				for (ChemicalAnalysis ca : g.getSubsample().getChemicalAnalyses()){
					ca.getId();
				}
				return g;
			}
		}

		// By Subsample Id
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("Grid.bySubsampleId");
			q.setParameter("id", inst.getSubsample().getId());
			if (getResult(q) != null) {
				Grid g = (Grid) getResult(q);
				g.setSubsample(new SubsampleDAO(sess).fill(g.getSubsample()));
				for (ChemicalAnalysis ca : g.getSubsample().getChemicalAnalyses()){
					ca.getId();
				}
				return g;
			}
		}

		throw new GridNotFoundException();
	}
	
	public Grid get(final long subsampleId)
			throws MpDbException {
		final Query q = namedQuery("Grid.bySubsampleId");
		q.setParameter("id", subsampleId);
		if (getResult(q) != null)
			return (Grid) getResult(q);
		return null;
	}

	@Override
	public Grid save(Grid inst) throws MpDbException {
		// Right now there is just one grid per subsample, so if a grid already
		// exists for the subsample, we're going to overwrite it
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("Grid.bySubsampleId");
			q.setParameter("id", inst.getSubsample().getId());
			if (getResult(q) != null) {
				inst.setId(((Grid) getResult(q)).getId());
			}
		}

		inst = _save(inst);
		return fill(inst);
	}

}

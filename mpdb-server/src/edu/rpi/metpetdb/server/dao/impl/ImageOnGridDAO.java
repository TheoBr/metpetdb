package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ImageOnGridDAO extends MpDbDAO<ImageOnGrid> {

	public ImageOnGridDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImageOnGrid delete(ImageOnGrid inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public ImageOnGrid fill(ImageOnGrid inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public ImageOnGrid save(ImageOnGrid inst) throws MpDbException {
		inst = _save(inst);
		return inst;
	}

	public List<ImageOnGrid> getImagesByGrid(long id) throws MpDbException{
		final Query q = namedQuery("ImageOnGrid.byGridId");
		q.setParameter("gridId", id);
		return (List<ImageOnGrid>) getResults(q);
	}
}

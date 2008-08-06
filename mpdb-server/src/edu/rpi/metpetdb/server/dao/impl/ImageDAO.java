package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ImageNotFoundException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.Image;

public class ImageDAO extends MpDbDAO<Image> {

	public ImageDAO(Session session) {
		super(session);
	}

	@Override
	public Image delete(Image inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Image fill(Image inst) throws DAOException {
		if (inst.getId() > 0) {
			final Query q = namedQuery("Image.id");
			q.setLong("id", inst.getId());
			if (q.uniqueResult() != null)
				return (Image) q.uniqueResult();
		}

		throw new ImageNotFoundException();
	}

	@Override
	public Image save(Image inst) throws DAOException {
		inst.setSubsample((new SubsampleDAO(sess)).fill(inst.getSubsample()));

		inst = _save(inst);
		return inst;
	}

	public int countBySubsampleId(long subsampleId) {
		Query q = sizeQuery("Image.bySubsampleId", subsampleId);
		return ((Number) q.uniqueResult()).intValue();
	}

	public List<Image> getBySubsampleId(long subsampleId) {
		Query q = namedQuery("Image.bySubsampleId");
		q.setLong("subsampleId", subsampleId);
		return q.list();
	}
}

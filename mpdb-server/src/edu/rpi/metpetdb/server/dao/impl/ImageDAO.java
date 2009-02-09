package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ImageNotFoundException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ImageDAO extends MpDbDAO<Image> {

	public ImageDAO(Session session) {
		super(session);
	}

	@Override
	public Image delete(Image inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Image fill(Image inst) throws MpDbException {
		if (inst.getId() > 0) {
			final Query q = namedQuery("Image.id");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (Image) getResult(q);
		}
		
		if (inst.getSample() != null && inst.getSample().getId() > 0) {
			final Query q = namedQuery("Image.bySampleIdbyFilename");
			q.setLong("id", inst.getSample().getId());
			q.setString("filename", inst.getFilename());
			if (getResult(q) != null)
				return (Image) getResult(q);
		}

		throw new ImageNotFoundException();
	}

	@Override
	public Image save(Image inst) throws MpDbException {
		if (inst.getSample() != null)
			inst.setSample((new SampleDAO(sess)).fill(inst.getSample()));
		inst.setSubsample((new SubsampleDAO(sess)).fill(inst.getSubsample()));
		inst.setImageType(new ImageTypeDAO(sess).fill(inst.getImageType()));
		inst = _save(inst);
		return inst;
	}

	public List<Image> getBySubsampleId(long subsampleId) throws MpDbException{
		Query q = namedQuery("Image.bySubsampleId");
		q.setLong("subsampleId", subsampleId);
		return (List<Image>) getResults(q);
	}
}

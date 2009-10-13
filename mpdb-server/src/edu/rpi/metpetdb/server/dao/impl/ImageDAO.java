package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ImageNotFoundException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.util.ImageUtil;

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
			final Query q = namedQuery("Image.byId");
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
		if (inst.getSubsample() != null && inst.getSubsample().getId() > 0) {
			final Query q = namedQuery("Image.bySubsampleIdbyFilename");
			q.setLong("id", inst.getSubsample().getId());
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
		if (inst.getSubsample() != null)
			inst.setSubsample((new SubsampleDAO(sess)).fill(inst.getSubsample()));
		inst.setImageType(new ImageTypeDAO(sess).fill(inst.getImageType()));
		inst.setFilename(ImageUtil.stripFilename(inst.getFilename()));
		inst = _save(inst);
		return inst;
	}
	

	public List<Image> getBySampleId(long sampleId) throws MpDbException{
		Query q = namedQuery("Image.bySampleId");
		q.setLong("sampleId", sampleId);
		return (List<Image>) getResults(q);
	}
	public List<Image> getBySubsampleId(long subsampleId) throws MpDbException{
		Query q = namedQuery("Image.bySubsampleId");
		q.setLong("subsampleId", subsampleId);
		return (List<Image>) getResults(q);
	}
	
	public List<Object[]> getIdsBySubsampleId(long subsampleId) throws MpDbException{
		Query q= namedQuery("Image-ids-publicData.bySubsampleId");
		q.setLong("subsampleId", subsampleId);
		return (List<Object[]>) getResults(q);
	}
	public Results<Image> getAllBySubsampleId(final PaginationParameters p,
			final long subsampleId) throws MpDbException {
		final Query sizeQ = sizeQuery("Image.bySubsampleId", subsampleId);
		final Query pageQ = pageQuery("Image.bySubsampleId", p, subsampleId);
		return getImages(sizeQ, pageQ);
	}
	
	private Results<Image> getImages(Query sizeQuery, Query pageQuery) throws MpDbException {
		final List<Image> l = (List<Image>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();
		return new Results<Image>(size, l);
	}

	public List<Image> getImagesWithoutMobile() throws MpDbException{
		Query q = sess.createQuery("from Image i where i.checksumMobile is null order by i.filename");
		q.setMaxResults(50);
		this.sess.disableFilter("imagePublicOrUser");
		this.sess.disableFilter("samplePublicOrUser");
		this.sess.disableFilter("subsamplePublicOrUser");
		this.sess.disableFilter("chemicalAnalysisPublicOrUser");
		return (List<Image>) getResults(q);
	}
}
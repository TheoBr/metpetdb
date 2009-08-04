package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.SubsampleNotFoundException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.util.ImageUtil;

public class SubsampleDAO extends MpDbDAO<Subsample> {

	public SubsampleDAO(Session session) {
		super(session);
	}

	@Override
	public Subsample delete(Subsample inst) throws MpDbException {
		_delete(inst);
		return null;
	}

	@Override
	public Subsample fill(Subsample inst) throws MpDbException {
		// By ID
		if (inst.getId() > 0) {
			final Query q = namedQuery("Subsample.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (Subsample) getResult(q);
		}

		// By Sample and Name
		if (inst.getSample() != null && inst.getName() != null) {
			inst.setSample((new SampleDAO(sess)).fill(inst.getSample()));
			final Query q = namedQuery("Subsample.bySample.byName");
			q.setParameter("id", inst.getSample().getId());
			q.setParameter("name", inst.getName());
			if (getResult(q) != null)
				return (Subsample) getResult(q);
		}
		
		//Those may have failed because of a filter even if it should be visible
		if(inst.getId() > 0 && (new ProjectDAO(sess)).isSampleVisibleToUser(MpDbServlet.currentReq().user.getId(), inst.getSample().getId())){
			//don't worry about infinite loops because if isSampleVisible succeeds, fill will definitely succeed.
			sess.disableFilter("subsamplePublicOrUser");
			Subsample result = fill(inst);
			sess.enableFilter("subsamplePublicOrUser").setParameter(
					"userId", MpDbServlet.currentReq().user.getId());
			return result;
		}

		throw new SubsampleNotFoundException();
	}

	@Override
	public Subsample save(Subsample inst) throws MpDbException {
		inst.setSubsampleType(new SubsampleTypeDAO(sess).fill(inst
				.getSubsampleType()));
		inst.setImages(ImageUtil.stripFilename(inst.getImages()));
		return _save(inst);
	}

	public List<Subsample> getAllBySampleID(final long sampleId)throws MpDbException {
		final Query q = namedQuery("Subsample.bySampleId");
		q.setParameter("sampleId", sampleId);
		final List<Subsample> l = (List<Subsample>) getResults(q);
		for (Subsample ss : l){
			ss.getImages().size();
		}
		return l;
	}
	
	public List<Object[]> getAllIds(final long sampleId) throws MpDbException{
		final Query q = namedQuery("Subsample-ids,publicData.bySampleId");
		q.setParameter("sampleId", sampleId);
		return (List<Object[]>) getResults(q);
	}

	public Results<Subsample> getAllWithImagesBySampleID(
			final PaginationParameters p, final long sampleId) throws MpDbException {
		final Query sizeQ = sizeQuery("Subsample.allWithImages", sampleId);
		final Query pageQ = pageQuery("Subsample.allWithImages", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	public Results<Subsample> getAllBySampleID(final PaginationParameters p,
			final long sampleId, long userId) throws MpDbException {
		//sess.enableFilter("subsamplePublicOrUser").setParameter("userId", userId);
		final Query sizeQ = sizeQuery("Subsample.all", sampleId);
		final Query pageQ = pageQuery("Subsample.all", p, sampleId);
		return getSubsamples(sizeQ, pageQ);
	}

	private Results<Subsample> getSubsamples(Query sizeQuery, Query pageQuery) throws MpDbException {
		final List<Subsample> l = (List<Subsample>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();
		return new Results<Subsample>(size, l);
	}
}

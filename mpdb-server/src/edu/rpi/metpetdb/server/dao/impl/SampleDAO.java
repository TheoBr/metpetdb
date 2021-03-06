package edu.rpi.metpetdb.server.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.SampleNotFoundException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.util.ImageUtil;

public class SampleDAO extends MpDbDAO<Sample> {

	public SampleDAO(Session session) {
		super(session);
	}

	@Override
	public Sample delete(Sample inst) throws MpDbException {
		_delete(inst);
		return null;
	}

	@Override
	public Sample fill(Sample inst) throws MpDbException {
		System.out.println("LOGOUT DEBUGGING - PRINTING SESSION OPEN" + sess.isOpen());
		System.out.println("LOGOUT DEBUGGING - PRINTING CURRENTREQ NULL" + MpDbServlet.currentReq() == null);
//		System.out.println("LOGOUT DEBUGGING - PRINTING USER ID" + MpDbServlet.currentReq().user.getId());
		// Use Id
		if (inst.getId() > 0) {
			final Query q = namedQuery("Sample.byId");
			q.setLong("id", inst.getId());
			if (getResult(q) != null)
				return (Sample) getResult(q);
		}

		// Use User,Number
		if (inst.getOwner() != null && inst.getOwner().getId() > 0
				&& inst.getNumber() != null) {
			final Query q = namedQuery("Sample.byUser.byNumber");
			q.setLong("id", inst.getOwner().getId());
			q.setString("number", inst.getNumber());
			if (getResult(q) != null)
				return (Sample) getResult(q);
		}
		
		//Those may have failed because of a filter even if it should be visible
		if(inst.getId() > 0 && (new ProjectDAO(sess)).isSampleVisibleToUser(MpDbServlet.currentReq().user.getId(), inst.getId())){
			//don't worry about infinite loops because if isSampleVisible succeeds, fill will definitely succeed.
			sess.disableFilter("samplePublicOrUser");
			Sample result = fill(inst);
			sess.enableFilter("samplePublicOrUser").setParameter(
					"userId", MpDbServlet.currentReq().user.getId());
			return result;
		}

		throw new SampleNotFoundException(inst);
	}

	public Set<SampleMineral> replaceMinerals(final Set<SampleMineral> s) {
		final Set<SampleMineral> m = (new SampleMineralDAO(sess)).fill(s);
		return m;
	}

	public void replaceTransientObjects(Sample s) throws MpDbException {
		// Fill subcomponents
		s.setRegions((new RegionDAO(sess)).fill(s.getRegions()));
		s.setMetamorphicGrades((new MetamorphicGradeDAO(sess)).fill(s
				.getMetamorphicGrades()));
		s.setReferences((new ReferenceDAO(sess)).fill(s.getReferences()));
		s.setMinerals(((new SampleMineralDAO(sess))).fill(s.getMinerals()));
		s.setRockType(new RockTypeDAO(sess).fill(s.getRockType()));
		
		//Check to see if there are any references that don't have a corresponding georef
		GeoReferenceDAO geoDAO = new GeoReferenceDAO(sess);
		
		Set<Reference> refs = s.getReferences();
		
		for(Reference currRef : refs)
		{
	
			ArrayList<String> referenceNumber = new ArrayList<String>();
			referenceNumber.add(currRef.getName());
			
		ArrayList<GeoReference> geoRefs = geoDAO.referencesByNumber(referenceNumber);		
		//add them to the current georefs

		if (geoRefs.size() > 0)
			currRef.setGeoref(geoRefs.get(0));
		
		}
		
	}
	
	public Set<MetamorphicRegion> getMatchingMetamorphicRegions(final Sample s){
		final org.hibernate.Query mregions = namedQuery("Sample.getMetRegions");
		sess.enableFilter("metRegionBox").setParameter(
		"polygon", s.getLocation()); 
		Set<MetamorphicRegion> regions = new HashSet<MetamorphicRegion>();
		if (mregions.list().size() != 0) {
		regions.addAll(mregions.list());
		}
		sess.disableFilter("metRegionBox");
		return regions;
	}

	@Override
	public Sample save(Sample s) throws MpDbException {
		replaceTransientObjects(s);
		s.setImages(ImageUtil.stripFilename(s.getImages()));
		s.setMetamorphicRegions(getMatchingMetamorphicRegions(s));
		s = _save(s);
		return s;
	}

	public Results<Sample> getProjectSamples(final PaginationParameters p,
			long id) throws MpDbException {
		final Query sizeQ = sizeQuery("Sample.forProject", id);
		final Query pageQ = pageQuery("Sample.forProject", p, id);
		Results<Sample> results = getSamples(sizeQ, pageQ);
		
		return results;
	}

	public Results<Sample> getAllPublicSamples(final PaginationParameters p) throws MpDbException {
		final Query sizeQ = sizeQuery("Sample.allPublic");
		final Query pageQ = pageQuery("Sample.allPublic", p);
		return getSamples(sizeQ, pageQ);
	}

	public Results<Sample> getAll(final PaginationParameters p) throws MpDbException {
		final Query sizeQ = sizeQuery("Sample.all");
		final Query pageQ = pageQuery("Sample.all", p);
		return getSamples(sizeQ, pageQ);
	}
	
	public List<Sample> getAll() throws MpDbException{
		final Query q = namedQuery("Sample.all/id");
		return (List<Sample>) getResults(q);
	}
	
	public List<Object[]> getAllIdsForUser(final long id) throws MpDbException{
		final Query q = namedQuery("Sample-ids,publicData");
		return (List<Object[]>) getResults(q);
	}
	
	public List<Sample> getSamplesForReference(final String reference) throws MpDbException{
		final Query q = namedQuery("Sample.byReference");
		q.setString("number", reference);
		return (List<Sample>) getResults(q);
	}

	private Results<Sample> getSamples(Query sizeQuery, Query pageQuery) throws MpDbException {
		final List<Sample> l = (List<Sample>) getResults(pageQuery);
		final int size = ((Number) getResult(sizeQuery)).intValue();
		
		return new Results<Sample>(size, l);
	}
	
	public Object[] allCollectors() throws MpDbException {
		final Query q = namedQuery("Sample.Collectors/Collector");
			return	((List<String>)getResults(q)).toArray();
	}
	
	public Object[] allCountries() throws MpDbException {
		final Query q = namedQuery("Sample.Countries/Countries");
		return	((List<String>)getResults(q)).toArray();
	}
	
	public long getPublicCount(){
		final Query q = namedQuery("Sample.PublicCount");
		return (Long) q.uniqueResult();
	}
	
	public long getPrivateCount(){
		final Query q = namedQuery("Sample.PrivateCount");
		return (Long) q.uniqueResult();
	}
	
	public long getPublicationCount(){
		final Query q = namedQuery("Sample.PublicationPublicCount");
		return (Long) q.uniqueResult();
	}
	public void sampleMetamorphicRegionsRetroactive() throws MpDbException{
		final Query q = namedQuery("Sample.all/number");
		for (Sample s : (List<Sample>) getResults(q)){
		save(s);
		}
	}
}

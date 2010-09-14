package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.service.SampleService;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.GeoReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.ProjectDAO;
import edu.rpi.metpetdb.server.dao.impl.SampleDAO;

public class SampleServiceImpl extends MpDbServlet implements SampleService {
	private static final long serialVersionUID = 1L;

	public Results<Sample> all(final PaginationParameters p) throws MpDbException {
		return (new SampleDAO(this.currentSession())).getAll(p);
	}

	public Results<Sample> allSamplesForUser(final PaginationParameters p,
			long id) throws MpDbException {
		this.currentSession().enableFilter("user").setParameter("id", id);
		return (new SampleDAO(this.currentSession())).getAll(p);
	}
	
	
	/**
	 * used for pagination tables to select all/public/private
	 */
	public Map<Object,Boolean> allIdsForUser(long id) throws MpDbException {
		this.currentSession().enableFilter("user").setParameter("id", id);
		Map<Object,Boolean> ids = new HashMap<Object,Boolean>();
		for (Object[] row : new SampleDAO(this.currentSession()).getAllIdsForUser(id)){
			ids.put(row[0], (Boolean) row[1]);
		}
		return ids;
	}
	
	
	public List<Sample> allSamplesForUser(final long id) throws MpDbException {
		this.currentSession().enableFilter("user").setParameter("id", id);
		return (new SampleDAO(this.currentSession())).getAll();
	}
	
	public Results<Sample> allPublicSamples(final PaginationParameters p) throws MpDbException {
		this.currentSession().enableFilter("public");
		return (new SampleDAO(this.currentSession())).getAll(p);
	}

	public Results<Sample> projectSamples(final PaginationParameters p, long id) throws MpDbException {
		return (new SampleDAO(this.currentSession()).getProjectSamples(p, id));
	}
	
	public Set<String> allCollectors() throws MpDbException {
		return objectArrayToStringSet((new SampleDAO(this.currentSession())).allCollectors());
	}
	
	public Set<String> viewableCollectorsForUser(final int userId) throws MpDbException {
		this.currentSession().enableFilter("samplePublicOrUser").setParameter("userId", userId);
		return objectArrayToStringSet((new SampleDAO(this.currentSession())).allCollectors());
	}
	
	public Set<String> allCountries() throws MpDbException {
		return objectArrayToStringSet((new SampleDAO(this.currentSession())).allCountries());
	}
	
	public Set<String> viewableCountriesForUser(final int userId) throws MpDbException {
		this.currentSession().enableFilter("samplePublicOrUser").setParameter("userId", userId);
		return objectArrayToStringSet((new SampleDAO(this.currentSession())).allCountries());
	}
	

	public Sample details(final long id) throws MpDbException {
		Sample s = new Sample();
		s.setId(id);
		s = (new SampleDAO(this.currentSession())).fill(s);
		s.getImages().size();
		
		ArrayList<String> referenceNumbers = new ArrayList<String>();
		
		for (Reference currReference : s.getReferences())
		{			
			ArrayList<String> refName = new ArrayList();
			refName.add(currReference.getName());
			List<GeoReference> geoRef = new GeoReferenceDAO(this.currentSession()).referencesByNumber(refName);
	
			if (geoRef != null && geoRef.size() > 0)
				s.getGeoReferences().add(geoRef.get(0));
		}
		
		
		return s;
	}
	
	public List<Sample> details(final List<Long> ids) throws MpDbException {
		List<Sample> results = new ArrayList<Sample>();
		for (Long id : ids) {
			Sample s = new Sample();
			s.setId(id);
			s = (new SampleDAO(this.currentSession())).fill(s);
			s.getImages().size();
			results.add(s);
		}
		return results;
	}

	

	public Sample save(Sample sample) throws MpDbException, ValidationException,
			LoginRequiredException {
		doc.validate(sample);
		final Sample s = (new SampleDAO(this.currentSession())).save(sample);
		commit();
		return s;
	}
	
	public void saveAll(Collection<Sample> samples)
			throws ValidationException, LoginRequiredException, MpDbException {
		final SampleDAO dao = new SampleDAO(this.currentSession());
		for(Sample s : samples) {
			doc.validate(s);
			dao.save(s);
		}
		commit();
}

	public MObject delete(long id) throws MpDbException, LoginRequiredException {
		deleteImpl(id);
		commit();
		return null;
	}

	public void deleteAll(Collection<Long> ids) throws MpDbException,
			LoginRequiredException {
		final Iterator<Long> itr = ids.iterator();
		while(itr.hasNext()) {
			deleteImpl(itr.next());
		}
		commit();
	}
	
	private void deleteImpl(long id) throws MpDbException, LoginRequiredException {
		SampleDAO dao = new SampleDAO(this.currentSession());
		Sample s = new Sample();
		s.setId(id);
		s = dao.fill(s);
		dao.delete(s);
	}
	
	private Set<String> objectArrayToStringSet(final Object[] o){
		final Set<String> options = new HashSet();
		for (int i = 0; i < o.length; i++){
			if (o[i] != null)
				options.add(o[i].toString());
		}
		return options;
	}
	
	public Boolean canEdit(final long id) throws MpDbException{
		User u = MpDbServlet.currentReq().user;
		ProjectDAO pDAO = (new ProjectDAO(this.currentSession()));
		boolean result = pDAO.isSampleVisibleToUser(u.getId(), id);
		return result;
	}

	public Results<Sample> allSamplesForProject(PaginationParameters p,
			long projectId) throws MpDbException, LoginRequiredException {	
		return null;
	}
	
	public long getPublicCount() {
		return new SampleDAO(this.currentSession()).getPublicCount();
	}
	
	public long getPrivateCount() {
		return new SampleDAO(this.currentSession()).getPrivateCount();
	}
	
	public long getPublicationCount() {
		return new SampleDAO(this.currentSession()).getPublicationCount();
	}
	public void sampleMetamorphicRegionsRetroactive() throws MpDbException {
		DataStore.disableSecurityFilters(this.currentSession());
		new SampleDAO(this.currentSession()).sampleMetamorphicRegionsRetroactive();
		commit();
		DataStore.enableSecurityFilters(this.currentSession(), this.currentUserIdIfExists());
		}
}

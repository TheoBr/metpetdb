package edu.rpi.metpetdb.server.impl;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.service.ReferenceService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ReferenceDAO;
import edu.rpi.metpetdb.server.dao.impl.RegionDAO;

public class ReferenceServiceImpl extends MpDbServlet implements ReferenceService {
	private static final long serialVersionUID = 1L;
	
	public Set<String> allReferences() throws MpDbException {
		return objectArrayToStringSet((new ReferenceDAO(this.currentSession())).allReferences());
	}
	
	public Set<String> viewableReferencesForUser(final int userId) throws MpDbException {
		this.currentSession().enableFilter("hasSamplePublicOrUser").setParameter("userId", userId);
		return objectArrayToStringSet((new ReferenceDAO(this.currentSession())).allReferences());
	}	
	
	private Set<String> objectArrayToStringSet(final Object[] o){
		final Set<String> options = new HashSet();
		for (int i = 0; i < o.length; i++){
			if (o[i] != null)
				options.add(o[i].toString());
		}
		return options;
	}

}

package edu.rpi.metpetdb.server.impl;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.service.RegionService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.RegionDAO;

public class RegionServiceImpl extends MpDbServlet implements RegionService {
	private static final long serialVersionUID = 1L;
	
	public Set<String> allNames() throws MpDbException {
		return objectArrayToStringSet((new RegionDAO(this.currentSession())).allNames());
	}
	
	public Set<String> viewableNamesForUser(final int userId) throws MpDbException {
		this.currentSession().enableFilter("hasSamplePublicOrUser").setParameter("userId", userId);
		return objectArrayToStringSet((new RegionDAO(this.currentSession())).allNames());
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

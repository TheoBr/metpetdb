package edu.rpi.metpetdb.server.impl;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.service.MetamorphicRegionService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.MetamorphicRegionDAO;

public class MetamorphicRegionServiceImpl extends MpDbServlet implements MetamorphicRegionService {
	private static final long serialVersionUID = 1L;
	
	public Set<String> allMetamorphicRegions() throws MpDbException {
		final Object[] l =  (new MetamorphicRegionDAO(this.currentSession())).allMetamorphicRegions();
		final Set<String> options = new HashSet();
		for (int i = 0; i < l.length; i++){
			if (l[i] != null)
				options.add(l[i].toString());
		}
		return options;
	}

}

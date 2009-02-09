package edu.rpi.metpetdb.server.impl;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.service.ReferenceService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ReferenceDAO;

public class ReferenceServiceImpl extends MpDbServlet implements ReferenceService {
	private static final long serialVersionUID = 1L;
	
	public Set<String> allReferences() throws MpDbException {
		final Object[] l =  (new ReferenceDAO(this.currentSession())).allReferences();
		final Set<String> options = new HashSet();
		for (int i = 0; i < l.length; i++){
			if (l[i] != null)
				options.add(l[i].toString());
		}
		return options;
	}

}

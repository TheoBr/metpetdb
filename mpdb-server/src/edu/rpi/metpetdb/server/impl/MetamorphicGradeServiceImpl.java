package edu.rpi.metpetdb.server.impl;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.service.MetamorphicGradeService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.MetamorphicGradeDAO;

public class MetamorphicGradeServiceImpl extends MpDbServlet implements MetamorphicGradeService {
	private static final long serialVersionUID = 1L;
	
	public Set<String> allMetamorphicGrades() {
		final Object[] l =  (new MetamorphicGradeDAO(this.currentSession())).allMetamorphicGrades();
		final Set<String> options = new HashSet();
		for (int i = 0; i < l.length; i++){
			if (l[i] != null)
				options.add(l[i].toString());
		}
		return options;
	}

}

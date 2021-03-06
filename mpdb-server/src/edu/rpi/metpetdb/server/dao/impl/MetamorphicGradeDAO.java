package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.MetamorphicGradeNotFoundException;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class MetamorphicGradeDAO extends MpDbDAO<MetamorphicGrade> {

	public MetamorphicGradeDAO(Session session) {
		super(session);
	}

	@Override
	public MetamorphicGrade delete(MetamorphicGrade inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public MetamorphicGrade fill(MetamorphicGrade mg) throws MpDbException {
		// Use Name
		final org.hibernate.Query mgrades = namedQuery("MetamorphicGrade.byName");
		mgrades.setString("name", mg.getName());
		if (mgrades.uniqueResult() != null)
			return (MetamorphicGrade) mgrades.uniqueResult();

		throw new MetamorphicGradeNotFoundException();
	}

	@Override
	public MetamorphicGrade save(MetamorphicGrade inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allMetamorphicGrades() throws MpDbException{
		final Query q = namedQuery("MetamorphicGrade.all/name");
		return	((List<MetamorphicGrade>)getResults(q)).toArray();
	}

}

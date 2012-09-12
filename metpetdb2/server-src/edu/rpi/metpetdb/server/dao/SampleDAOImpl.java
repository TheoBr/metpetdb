package edu.rpi.metpetdb.server.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.MetamorphicGrade;
import edu.rpi.metpetdb.server.model.Sample;

public class SampleDAOImpl extends HibernateDaoSupport implements SampleDAO {

	@Override
	public Sample loadSample(Long id) {

		return (Sample)this.getSession().load(Sample.class, id);

	}

	@Override
	public void saveSample(Sample sample) {

		this.getSession().saveOrUpdate(sample);
		this.getSession().flush();
	//	this.getSession().clear();
	}

}

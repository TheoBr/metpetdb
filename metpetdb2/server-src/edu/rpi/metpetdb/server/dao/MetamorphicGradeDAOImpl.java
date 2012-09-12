package edu.rpi.metpetdb.server.dao;

import java.util.List;
import java.util.Set;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.MetamorphicGrade;
import edu.rpi.metpetdb.server.model.Sample;

public class MetamorphicGradeDAOImpl extends HibernateDaoSupport implements MetamorphicGradeDAO  {

	@Override
	public void loadExistingGrades(Sample sample) {

		Set<MetamorphicGrade> metamorphicGrades = sample.getMetamorphicGrades();
		
		for (MetamorphicGrade currentMetamorphicGrade : metamorphicGrades)
		{
			HibernateTemplate template = this.getHibernateTemplate();
			List<MetamorphicGrade> existingMetamorphicGrade = template.findByNamedParam("from MetamorphicGrade where name = :name",  "name", currentMetamorphicGrade.getName());
			
			
			if (existingMetamorphicGrade.get(0) != null);
			{
				metamorphicGrades.remove(currentMetamorphicGrade);
				metamorphicGrades.add(existingMetamorphicGrade.get(0));
			}
		}
	}

}

package edu.rpi.metpetdb.server.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.Mineral;

public class MineralDAOImpl extends HibernateDaoSupport implements MineralDAO  {

	@Override
	public List<Mineral> loadMinerals() {
		
		List<Mineral> minerals = null;
		
		
		minerals = this.getHibernateTemplate().loadAll(Mineral.class);
		
		return minerals;
	}

}

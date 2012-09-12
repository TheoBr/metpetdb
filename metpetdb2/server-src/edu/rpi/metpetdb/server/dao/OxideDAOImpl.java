package edu.rpi.metpetdb.server.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.Element;
import edu.rpi.metpetdb.server.model.Oxide;

public class OxideDAOImpl extends HibernateDaoSupport implements OxideDAO {

	@Override
	public Oxide loadOxide(Long id) {

		return (Oxide)this.getSession().load(Oxide.class, id);

	}

	@Override
	public void saveOxide(Oxide oxide) {
		this.getSession().save(oxide);
		this.getSession().flush();

	}

}

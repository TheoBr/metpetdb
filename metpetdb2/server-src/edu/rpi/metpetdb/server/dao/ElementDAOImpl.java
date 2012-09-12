package edu.rpi.metpetdb.server.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.Element;

public class ElementDAOImpl extends HibernateDaoSupport implements ElementDAO {

	@Override
	public Element loadElement(Long id) {

			return (Element)this.getSession().load(Element.class, id);
	}

	@Override
	public void saveElement(Element element) {
		
		this.getSession().save(element);
		this.getSession().flush();
	}

}

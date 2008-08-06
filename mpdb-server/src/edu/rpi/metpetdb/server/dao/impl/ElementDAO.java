package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.ElementNotFoundException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.server.dao.MpDbDAO;
import edu.rpi.metpetdb.server.model.Element;

public class ElementDAO extends MpDbDAO<Element> {

	public ElementDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element delete(Element inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Element fill(Element inst) throws DAOException {
		// Use Name
		if (inst.getId() > 0) {
			final Query q = namedQuery("Element.byName");
			q.setString("name", inst.getName());
			if (q.uniqueResult() != null)
				return (Element) q.uniqueResult();
		}

		// TODO: Sometimes people use the symbol as the name

		throw new ElementNotFoundException();
	}

	@Override
	public Element save(Element inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	public List<Element> getAll() {
		final Query q = namedQuery("Element.all");
		return q.list();
	}
}

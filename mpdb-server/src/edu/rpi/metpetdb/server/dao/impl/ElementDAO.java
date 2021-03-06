package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.ElementNotFoundException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ElementDAO extends MpDbDAO<Element> {

	public ElementDAO(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Element delete(Element inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public Element fill(Element inst) throws MpDbException {
		// Use Name
		if (inst.getName() != null) {
			final Query q = namedQuery("Element.byName");
			q.setString("name", inst.getName());
			if (getResult(q) != null)
				return (Element) getResult(q);
		}

		// Use Symbol
		if (inst.getSymbol() != null) {
			final Query q = namedQuery("Element.bySymbol");
			q.setString("symbol", inst.getSymbol());
			if (getResult(q) != null)
				return (Element) getResult(q);
		}

		// Sometimes people use the symbol as the name, check that
		if (inst.getName() != null && inst.getName().length() < 3) {
			final Query q = namedQuery("Element.bySymbol");
			q.setString("symbol", inst.getName());
			if (getResult(q) != null)
				return (Element) getResult(q);
		}

		throw new ElementNotFoundException();
	}

	@Override
	public Element save(Element inst) throws MpDbException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	public List<Element> getAll() throws MpDbException {
		final Query q = namedQuery("Element.all");
		return (List<Element>) getResults(q);
	}
}

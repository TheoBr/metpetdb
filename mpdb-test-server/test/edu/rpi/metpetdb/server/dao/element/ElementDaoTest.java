package edu.rpi.metpetdb.server.dao.element;

import static org.junit.Assert.assertEquals;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.server.DataStore;

public class ElementDaoTest {
	private static Session s;

	@Before
	public void setUp() {
		DataStore.initFactory();

		s = DataStore.open();

	}

	@After
	public void tearDown() {
		s.close();
	}
	
	@Test
	public void loadElement() {
		final Query q = s.getNamedQuery("Element.byName").setParameter("name",
				"Hydrogen");
		final Element element = (Element) q.uniqueResult();
		assertEquals("Hydrogen", element.getName());
	}
}

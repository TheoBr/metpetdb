package edu.rpi.metpetdb.server.dao.mineral;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Iterator;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.server.DataStore;

public class MineralDaoTest {

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
	public void loadMinerals() {
		final Query q = s.getNamedQuery("Mineral.all");
		final Collection<Mineral> minerals = q.list();
		initMinerals(minerals);
		printMineralTree(minerals, "");
	}
	
	private void initMinerals(final Collection<Mineral> minerals) {
		for(Mineral m : minerals) {
			Hibernate.initialize(m);
			Hibernate.initialize(m.getChildren());
			initMinerals(m.getChildren());
		}
	}

	private void printMineralTree(final Collection<Mineral> tree,
			final String spaces) {
		final Iterator<Mineral> itr = tree.iterator();
		while (itr.hasNext()) {
			final Mineral m = itr.next();
			System.out.println(spaces + m.getName());
			printMineralTree(m.getChildren(), spaces + "-");
		}
	}

	@Test
	public void loadMineralCriteria() {
		final Mineral m = (Mineral) s.createCriteria(Mineral.class).add(
				Restrictions.eq("name", "Tectosilicates")).setFetchMode(
				"children", FetchMode.SELECT).uniqueResult();
		assertEquals("Tectosilicates", m.getName());
	}

	@Test
	public void loadMineral() {
		final Query q = s.getNamedQuery("Mineral.byName").setParameter("name",
				"Tectosilicates");
		final Mineral m = (Mineral) q.uniqueResult();
		assertEquals("Tectosilicates", m.getName());
	}

	@Test
	public void loadAlternativeMineral() {
		final Query q = s.getNamedQuery("Mineral.byName").setParameter("name",
				"K-feldspar");
		final Mineral m = (Mineral) q.uniqueResult();
		assertEquals("Alkali feldspar", m.getName());
	}

}

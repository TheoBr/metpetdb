package edu.rpi.metpetdb.server.dao;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.model.Mineral;

public class MineralDaoTest extends TestCase {
	
	private static Session s;

	@Override
	public void setUp() {
		DataStore.initFactory();

		s = DataStore.open();
		
	}
	
	@Test
	public void testLoadMinerals() {
		final Query q = s.getNamedQuery("Mineral.all");
		printMineralTree(q.list(),"");
	}
	
	private void printMineralTree(final Collection<Mineral> tree, final String spaces) {
		final Iterator<Mineral> itr = tree.iterator();
		while(itr.hasNext()) {
			final Mineral m = itr.next();
			System.out.println(spaces + m.getName());
			printMineralTree(m.getChildren(), spaces + "-");
		}
	}

}

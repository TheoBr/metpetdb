package edu.rpi.metpetdb.server.model.archive;


import junit.framework.TestCase;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SubqueryExpression;

import edu.rpi.metpetdb.client.model.A;
import edu.rpi.metpetdb.client.model.B;
import edu.rpi.metpetdb.server.DataStore;

public class ArchiveTableTest extends TestCase {

	public ArchiveTableTest() {
	}
	
	public void setUp() {

	}
	
	
	public void testA() {
		final Session s = DataStore.open();
		//select * from  b  inner join a_b on b.b_id=a_b.b_id where a_b.version = (select max(a_b.version) from a_b)
		final A a1 = (A) s.createCriteria(A.class).createCriteria("bs").uniqueResult();
		for(B b: a1.getBs()) {
			System.out.println("B - " + b.getId());
		}
		s.close();
	}
}
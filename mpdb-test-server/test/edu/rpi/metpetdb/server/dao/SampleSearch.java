package edu.rpi.metpetdb.server.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.AttributeDTO;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.User;

//TODO make some generic methods in databasetestcase
public class SampleSearch extends DatabaseTestCase {

	public SampleSearch() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Test loading a sample by its id, a valid id should be given
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void testSearch() 
	{
		final Session session = InitDatabase.getSession();
		List results = session.createSQLQuery("select s.sample_id from samples s").list();
/*		for(int i = 0; i < results.size(); i++)
		{
			List<Sample> results2 = session.createFilter(((Sample)results[i]).getMinerals(), "where amount > 0").list();
		}
	*/	
		
		assertEquals(5,results.size());		
	}
	
	@Test
	public void testSearch2() 
	{
		final Session session = InitDatabase.getSession();
		List results = session.createSQLQuery("select * from users u where u.user_id = :id").addEntity(User.class).setParameter("id", 1).list();
		Iterator iter = results.iterator();
		while(iter.hasNext())
		{
			User u = (User)iter.next();;
			System.out.println(u.getId());
			System.out.println(u.getUsername());
		
		}
		assertEquals(results.size(), 1);		
	}

	@Test
	public void testSearch3() 
	{
		final Session session = InitDatabase.getSession();
		List results = session.createSQLQuery("select * from users u where u.username like :name").setParameter("name", "anthony").list();
		
		assertEquals(1,results.size());		
	}

	@Test
	public void testSearch4() 
	{
		final Session session = InitDatabase.getSession();
		List<AttributeDTO> AttributePairs = new LinkedList<AttributeDTO>();
		AttributePairs.add(new AttributeDTO("u.username", "anthony"));		
		AttributePairs.add(new AttributeDTO("u.user_id", "1"));
		
		System.out.println("making the string");
		String query = new String("select * from users u where");
		int i = 0;
		for(AttributeDTO attr : AttributePairs){
			if(i == 0){
				query = query.concat(" " + attr.getAttribute() + " like :attr" + i);
			}
			else{
				query = query.concat(" and " + attr.getAttribute() + " like :attr" + i);
			}
			i++;		
		}
		Query q = session.createSQLQuery(query);		
		i = 0;	
		for(AttributeDTO attr : AttributePairs){
			String param1 = "attr" + i;
			q.setParameter(param1, attr.getValue());
			i++;		
		}
			
		List results = q.list();
		session.close();
		
		assertEquals(1,results.size());		
	}	
}

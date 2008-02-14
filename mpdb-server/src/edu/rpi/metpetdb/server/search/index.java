package edu.rpi.metpetdb.server.search;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.Transaction;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DataStore;


public class index
{
	public index()
	{
		Session session = DataStore.open();
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();
		List<Sample> samples = session.createQuery("from Sample as sample").list();
		for (Sample sample : samples) {
		    fullTextSession.index(samples);
		}
		tx.commit(); //index are written at commit time   
	}
}
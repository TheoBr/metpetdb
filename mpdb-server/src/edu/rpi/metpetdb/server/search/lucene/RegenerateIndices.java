package edu.rpi.metpetdb.server.search.lucene;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DataStore;

/**
 * Regenerates the Lucene indices for samples
 * 
 * @author anthony
 * 
 */
public class RegenerateIndices {

	@SuppressWarnings("unchecked")
	public static void regenerate() {
		Session session = DataStore.open();
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();
		List<Sample> samples = session.createQuery("from Sample as sample")
				.list();
		for (Sample sample : samples) {
			fullTextSession.index(sample);
		}
		tx.commit(); // index are written at commit time
		session.close();
	}

	
	public static void main(String args[]) {
		regenerate();
	}
}

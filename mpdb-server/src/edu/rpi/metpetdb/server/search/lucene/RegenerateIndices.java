package edu.rpi.metpetdb.server.search.lucene;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.permissions.principals.AdminPrincipal;

/**
 * Regenerates the Lucene indices for search
 * 
 * @author anthony
 * 
 */
public class RegenerateIndices {
	private static int BATCH_SIZE = 64;
	@SuppressWarnings("unchecked")
	public static void regenerate() {
		Session session = DataStore.open();

		try {
			// FIXME fake an admin to regenerate search index
		//	MpDbServlet.testReq = new MpDbServlet.Req();
		//	MpDbServlet.testReq.principals = new HashSet<Principal>();
		//	MpDbServlet.testReq.principals.add(new AdminPrincipal());
			FullTextSession fullTextSession = Search.createFullTextSession(session);
			
			
			fullTextSession.setFlushMode(FlushMode.MANUAL);
			fullTextSession.setCacheMode(CacheMode.IGNORE);
			Transaction tx = fullTextSession.beginTransaction();
			
			fullTextSession.purgeAll(Sample.class);
			fullTextSession.flushToIndexes();
			fullTextSession.getSearchFactory().optimize(Sample.class);
			fullTextSession.purgeAll(User.class);
			fullTextSession.flushToIndexes();
			fullTextSession.getSearchFactory().optimize(User.class);
			fullTextSession.purgeAll(Subsample.class);
			fullTextSession.flushToIndexes();
			fullTextSession.getSearchFactory().optimize(Subsample.class);
			//read the data from the database
			//Scrollable results will avoid loading too many objects in memory
			ScrollableResults results = session.createCriteria(Sample.class ).scroll( ScrollMode.FORWARD_ONLY );
			reindex(results,fullTextSession);
		//	ScrollableResults results = session.createCriteria(Subsample.class ).scroll( ScrollMode.FORWARD_ONLY );
		//	reindex(results,fullTextSession);
		//	results = session.createCriteria(User.class ).scroll( ScrollMode.FORWARD_ONLY );
		//	reindex(results,fullTextSession);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			//MpDbServlet.testReq = null;
		}
	}
	
	public static void reindex(ScrollableResults results, FullTextSession fullTextSession){
		int index = 0;
		while( results.next() ) {
			index++;
			fullTextSession.index( results.get(0) );
			if (index % BATCH_SIZE == 0) {
				fullTextSession.flushToIndexes();
				fullTextSession.clear();
			}
		}
	}

	public static void main(String args[]) {
		regenerate();
	}
}

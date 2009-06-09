package edu.rpi.metpetdb.server.search.lucene;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
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

	@SuppressWarnings("unchecked")
	public static void regenerate() {
		Session session = DataStore.open();
		try {
			// FIXME fake an admin to regenerate search index
			MpDbServlet.testReq = new MpDbServlet.Req();
			MpDbServlet.testReq.principals = new HashSet<Principal>();
			MpDbServlet.testReq.principals.add(new AdminPrincipal());
			FullTextSession fullTextSession = Search
					.createFullTextSession(session);
			fullTextSession.purgeAll(Sample.class);
			fullTextSession.purgeAll(User.class);
			fullTextSession.purgeAll(Subsample.class);
//			fullTextSession.purgeAll(ChemicalAnalysis.class);
			Transaction tx = fullTextSession.beginTransaction();
			List<Sample> samples = session.createQuery("from Sample as sample")
					.list();
			for (Sample sample : samples) {
				fullTextSession.index(sample);
			}
			List<Subsample> subsamples = session.createQuery(
					"from Subsample as ss").list();
			for (Subsample subsample : subsamples) {
				fullTextSession.index(subsample);
			}
//			List<ChemicalAnalysis> chemicalAnalyses = session.createQuery(
//					"from ChemicalAnalysis as ca").list();
//			for (ChemicalAnalysis ca : chemicalAnalyses) {
//				fullTextSession.index(ca);
//			}
			List<User> users = session.createQuery(
					"from User as u").list();
			for (User u : users) {
				fullTextSession.index(u);
			}
				
			tx.commit(); // index are written at commit time
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			MpDbServlet.testReq = null;
		}
	}

	public static void main(String args[]) {
		regenerate();
	}
}

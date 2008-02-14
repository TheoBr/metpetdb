package edu.rpi.metpetdb.server.search;

import java.awt.print.Book;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.server.DataStore;

public class SampleSearch extends FlowPanel {

	public SampleSearch() {
		Session session = DataStore.open();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] {
				"sesarNumber", "alias" }, new StandardAnalyzer());
		try {
			Query query = parser.parse("sesarNumber:123456789 AND alias:test");
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
					query, Book.class);
			List result = hibQuery.list();
		} catch (Exception e) {

		}

		tx.commit();
		session.close();
	}
}
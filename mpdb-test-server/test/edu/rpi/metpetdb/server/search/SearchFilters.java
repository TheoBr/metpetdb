package edu.rpi.metpetdb.server.search;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.reader.ReaderProvider;
import org.hibernate.search.store.DirectoryProvider;
import org.junit.Test;

import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Sample;

public class SearchFilters extends DatabaseTestCase {

	public SearchFilters() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Test finding samples within a bound box of latitude and longitudal
	 * coordinates
	 */
	@Test
	public void testLatLongBoundBoxSearch() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("alias", "1"));
		final FullTextQuery hibQuery = fullTextSession.createFullTextQuery(
				termQuery, Sample.class);

		session.enableFilter("boundingBox").setParameter("x1", 50)
				.setParameter("y1", 45).setParameter("x2", 55).setParameter(
						"y2", 55);

		// final Query q = s
		// .createSQLQuery("select count(*) from samples where sample_id="
		// + id
		// + " and ST_Intersects("
		// + "location, "
		// +
		// "ST_SetSRID(ST_MakeBox2D(ST_MakePoint(50,50),ST_MakePoint(55,55)),4326))"
		// );

		final List<Sample> result = hibQuery.list();
		assertEquals(1, result.size());
	}

}

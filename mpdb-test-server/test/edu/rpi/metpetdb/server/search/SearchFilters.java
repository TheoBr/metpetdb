package edu.rpi.metpetdb.server.search;

import java.sql.SQLException;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;
import org.postgis.Polygon;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;

public class SearchFilters extends DatabaseTestCase {

	public SearchFilters() {
		super("test-data/test-sample-data.xml");
	}

	/**
	 * Test finding samples within a bound box of latitude and longitudal
	 * coordinates
	 * @throws SQLException 
	 */
	@Test
	public void testLatLongBoundBoxSearch() throws SQLException {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("user_firstName", "anthony"));
		final FullTextQuery hibQuery = fullTextSession.createFullTextQuery(
				termQuery, Sample.class);
		final Polygon poly = new Polygon(
				"POLYGON((45 45, 45 55, 55 55, 55 45, 45 45))");
		poly.setSrid(4326);
		//TODO update parameter to be the  bounding box
		session.enableFilter("boundingBox").setParameter("polygon", poly ); 

		final List<Sample> result = hibQuery.list();
		assertEquals(2, result.size());
	}
	///POLYGON((MINX MINY, MINX MAXY, MAXX MAXY, MAXX MINY, MINX MINY))

}

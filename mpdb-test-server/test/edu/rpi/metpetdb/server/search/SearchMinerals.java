package edu.rpi.metpetdb.server.search;

import java.util.List;
import java.util.Set;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;

import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.SampleMineral;

public class SearchMinerals extends DatabaseTestCase {

	public SearchMinerals() {
		super("test-data/test-sample-data.xml");
	}
	@Test
	public void testSampleContainsMineral() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term(
				"sampleMineral_mineral_name", "silicates"));
		// final TermQuery termQuery = new TermQuery(new Term(
		// "sampleMineral_amount", "0.0"));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result) {
			final Set<SampleMineral> minerals = s.getMinerals();
			for (final SampleMineral sm : minerals) {
				final Mineral m = sm.getMineral();
				System.out.println("find sample with mineral " + m.getName()
						+ " my id was " + s.getId());
			}
		}

		assertEquals(3, result.size());
	}

}

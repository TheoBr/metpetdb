package edu.rpi.metpetdb.server.search;

import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.RangeFilter;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.util.NumberUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;

public class SearchOxidesAndElements extends DatabaseTestCase {

	public SearchOxidesAndElements() {
		super("test-data/test-sample-data.xml");
	}

	@Test
	public void testCreateIndices() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();
		final List<Sample> samples = session.createQuery(
				"from Sample as sample").list();
		for (final Sample sample : samples)
			fullTextSession.index(sample);
		tx.commit(); // index are written at commit time
	}

	@Test
	public void testElementAmountRange1() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final RangeQuery rangeQuery = new RangeQuery(new Term(
				"subsample_chemicalAnalysis_elements_amount",
				"0"), new Term("subsample_chemicalAnalysis_elements_amount", NumberUtils.float2sortableStr(5.0f)), true);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(rangeQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(1, result.size());

	}
	
	@Test
	public void testElementSymbolAl() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_elements_element_symbol", "al"));
	
		final FullTextQuery hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(2, result.size());

	}
	
	@Test
	public void testElementAmountRange2() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final RangeFilter rangeFilter = new RangeFilter("subsample_chemicalAnalysis_elements_amount",NumberUtils.float2sortableStr(4.99999f), NumberUtils.float2sortableStr(12.0f),true, true);
		final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_elements_element_symbol", "al"));
		final FilteredQuery filter = new FilteredQuery(termQuery, rangeFilter);
		

		final FullTextQuery hibQuery = fullTextSession
				.createFullTextQuery(filter, Sample.class);
		//hibQuery.enableFullTextFilter("elementAmountFilter");
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(2, result.size());

	}
	
	@Test
	public void testOxideSpeciesAl2O3() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_oxides_oxide_species", "al2o3"));
	
		final FullTextQuery hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(2, result.size());

	}
	
	@Test
	public void testOxideAmountRange2() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final RangeFilter rangeFilter = new RangeFilter("subsample_chemicalAnalysis_oxides_amount",NumberUtils.float2sortableStr(4.99999f), NumberUtils.float2sortableStr(22.43242f),true, true);
		final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_oxides_oxide_species", "al2o3"));
		final FilteredQuery filter = new FilteredQuery(termQuery, rangeFilter);
		

		final FullTextQuery hibQuery = fullTextSession
				.createFullTextQuery(filter, Sample.class);
		//hibQuery.enableFullTextFilter("elementAmountFilter");
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(2, result.size());

	}
	
	@Test
	public void testOxideAmountRangeMinMax() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final RangeFilter rangeFilterOnMin = new RangeFilter("subsample_chemicalAnalysis_oxides_minAmount", NumberUtils.float2sortableStr(-99999f), NumberUtils.float2sortableStr(100f),true, true);
		final RangeFilter rangeFilterOnMax = new RangeFilter("subsample_chemicalAnalysis_oxides_maxAmount", NumberUtils.float2sortableStr(0f), NumberUtils.float2sortableStr(99999f),true, true);
		final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_oxides_oxide_species", "al2o3"));
		final FilteredQuery filterOnMinQuery = new FilteredQuery(termQuery, rangeFilterOnMin);
		final FilteredQuery filterOnBothQuery = new FilteredQuery(filterOnMinQuery, rangeFilterOnMax);
		
		
		final FullTextQuery hibQuery = fullTextSession
				.createFullTextQuery(filterOnBothQuery, Sample.class);
		//hibQuery.enableFullTextFilter("elementAmountFilter");
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		assertEquals(2, result.size());

	}



}

package edu.rpi.metpetdb.server.search;
import static org.junit.Assert.*;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
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

import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
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

/*	@Test
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

		final SearchSample searchSamp = new SearchSample();
		Element tempOxide = new Element();
		tempOxide.setSymbol("Al");
		searchSamp.addElement(tempOxide, 4f, 8f);
		
		for (SearchElement o : (Set<SearchElement>) searchSamp.getElements()) {
			final RangeFilter rangeFilterOnMin = new RangeFilter("subsample_chemicalAnalysis_elements_minAmount", NumberUtils.float2sortableStr(-99999), NumberUtils.float2sortableStr(o
					.getUpperBound()),true, true);
			final RangeFilter rangeFilterOnMax = new RangeFilter("subsample_chemicalAnalysis_elements_maxAmount", NumberUtils.float2sortableStr(o
					.getLowerBound()), NumberUtils.float2sortableStr(99999),true, true);
			final TermQuery elementQuery = new TermQuery(
					new Term(
							"subsample_chemicalAnalysis_elements_element_symbol",
							o.getElementSymbol()));
			final FilteredQuery filterOnMinQuery = new FilteredQuery(elementQuery, rangeFilterOnMin);
			final FilteredQuery filterOnBothQuery = new FilteredQuery(filterOnMinQuery, rangeFilterOnMax);
			QueryParser parser = new QueryParser("title", new StandardAnalyzer() );
			try{
				org.apache.lucene.search.Query luceneQuery = parser.parse(filterOnBothQuery.toString());
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(luceneQuery, Sample.class);
				final List<Sample> result = hibQuery.list();
				assertEquals(1, result.size());
			}
			catch(Exception e)
			{
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(filterOnBothQuery, Sample.class);
				final List<Sample> results = hibQuery.list();
				assertEquals(1, results.size());
			}
		}	

	}*/
	
	@Test
	public void testElementAmountRange2() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final SearchSample searchSamp = new SearchSample();
		Element tempOxide = new Element();
		tempOxide.setSymbol("Al");
		searchSamp.addElement(tempOxide, 4d, 6d);
		BooleanQuery fullQuery = new BooleanQuery();
		for (SearchElement o : (Set<SearchElement>) searchSamp.getElements()) {
			final RangeFilter rangeFilterOnMin = new RangeFilter("subsample_chemicalAnalysis_elements_minAmount", NumberUtils.double2sortableStr(-99999f), NumberUtils.double2sortableStr(o
			.getUpperBound()),true, true);
			final RangeFilter rangeFilterOnMax = new RangeFilter("subsample_chemicalAnalysis_elements_maxAmount", NumberUtils.double2sortableStr(o
			.getLowerBound()), NumberUtils.double2sortableStr(99999f),true, true);
			final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_elements_element_symbol", o.getElementSymbol()));
			final FilteredQuery filterOnMinQuery = new FilteredQuery(termQuery, rangeFilterOnMin);
			final FilteredQuery filterOnBothQuery = new FilteredQuery(filterOnMinQuery, rangeFilterOnMax);
			fullQuery.add(filterOnBothQuery, BooleanClause.Occur.MUST);
			QueryParser parser = new QueryParser("title", new StandardAnalyzer() );
			try{
				org.apache.lucene.search.Query luceneQuery = parser.parse(fullQuery.toString());
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(luceneQuery, Sample.class);
				final List<Sample> results = hibQuery.list();
				assertEquals(1, results.size());
			}
			catch(Exception e)
			{
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
				final List<Sample> results = hibQuery.list();
				assertEquals(1, results.size());
			}
		}		
	}
	
/*	@Test
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
	*/
	@Test
	public void testOxideAmountRangeMinMax() {
		final SearchSample searchSamp = new SearchSample();
		Oxide tempOxide = new Oxide();
		tempOxide.setSpecies("AL2O3");
		searchSamp.addOxide(tempOxide, 4d, 8d);
		
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);
		for (SearchOxide o : (Set<SearchOxide>) searchSamp.getOxides()) {
			final RangeFilter rangeFilterOnMin = new RangeFilter("subsample_chemicalAnalysis_oxides_minAmount", NumberUtils.double2sortableStr(-99999f), NumberUtils.double2sortableStr(o
			.getUpperBound()),true, true);
			final RangeFilter rangeFilterOnMax = new RangeFilter("subsample_chemicalAnalysis_oxides_maxAmount", NumberUtils.double2sortableStr(o
			.getLowerBound()), NumberUtils.double2sortableStr(99999f),true, true);
			final TermQuery termQuery = new TermQuery(new Term("subsample_chemicalAnalysis_oxides_oxide_species", o.getSpecies()));
			final FilteredQuery filterOnMinQuery = new FilteredQuery(termQuery, rangeFilterOnMin);
			final FilteredQuery filterOnBothQuery = new FilteredQuery(filterOnMinQuery, rangeFilterOnMax);
			
			
			QueryParser parser = new QueryParser("title", new StandardAnalyzer() );
			try{
				org.apache.lucene.search.Query luceneQuery = parser.parse(filterOnBothQuery.toString());
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(luceneQuery, Sample.class);
				final List<Sample> results = hibQuery.list();
				assertEquals(1, results.size());
			}
			catch(Exception e)
			{
				final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(filterOnBothQuery, Sample.class);
				final List<Sample> results = hibQuery.list();
				assertEquals(1, results.size());
			}
		}
	}



}

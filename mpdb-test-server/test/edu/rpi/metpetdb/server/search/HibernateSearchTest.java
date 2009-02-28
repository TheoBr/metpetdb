package edu.rpi.metpetdb.server.search;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeFilter;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.util.NumberUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;

public class HibernateSearchTest extends DatabaseTestCase {

	public HibernateSearchTest() {
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

		tx = fullTextSession.beginTransaction();
		final List<User> users = session.createQuery("from User as user")
				.list();
		for (final User user : users)
			fullTextSession.index(user);
		tx.commit(); // index are written at commit time
	}

	@Test
	public void testTermSearch() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("alias", "1"));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, rock type is " + s.getRockType());

		assertEquals(1, result.size());
	}

	@Test
	public void testTermSearch2() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("rockType", "ibm"));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, rock type is " + s.getRockType());

	}

	@Test
	public void testRangeSearch() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

		final RangeQuery rangeQuery = new RangeQuery(new Term("sesarNumber",
				"000000004"), new Term("sesarNumber", "000000009"), true);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(rangeQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());

		tx.commit();
	}

	@Test
	public void testMultiFields() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

		System.out.println("in multifield search");
		final String[] searchFor = {
				"000000004", "rockie rock"
		};
		final String[] columnsIn = {
				"sesarNumber", "rockType"
		};
		final BooleanClause.Occur[] flags = {
				BooleanClause.Occur.MUST, BooleanClause.Occur.MUST
		};

		try {
			final Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(searchFor, columnsIn, flags, new StandardAnalyzer());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(query, Sample.class);
			final List<Sample> result = hibQuery.list();

			for (final Sample s : result)
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber());
		} catch (final ParseException e) {
		}

		tx.commit();
	}

	@Test
	public void testJoin() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

		System.out.println("in test join");
		final String[] searchFor = {
				"000000004", "anthony"
		};
		final String[] columnsIn = {
				"sesarNumber", "user_username"
		};
		final BooleanClause.Occur[] flags = {
				BooleanClause.Occur.MUST, BooleanClause.Occur.MUST
		};

		try {
			final Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(searchFor, columnsIn, flags, new StandardAnalyzer());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(query, Sample.class);
			final List<Sample> result = hibQuery.list();

			for (final Sample s : result)
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber());
		} catch (final ParseException e) {
		}

		tx.commit();
	}

	@Test
	public void testSearchSampleSearch() {
		final SearchSample searchSamp = new SearchSample();

		// searchSamp.setSesarNumber("000000000"); searchSamp.setAlias("1");
		final RockType rockType1 = new RockType();
		rockType1.setRockType("logitech");
		final RockType rockType2 = new RockType();
		rockType2.setRockType("rockie rock");
		searchSamp.addPossibleRockType(rockType1);
		searchSamp.addPossibleRockType(rockType2);

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

		final List<String> searchForValue = new LinkedList<String>();
		final List<String> columnsIn = new LinkedList<String>();
		final List<BooleanClause.Occur> flags = new LinkedList<BooleanClause.Occur>();

		String columnName;
		Object methodResult = null;
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {
			columnName = i.columnName();
			methodResult = i.get(searchSamp);
			if (methodResult == null) {
			} else {
				if (methodResult instanceof Set) {
					for (Object o : (Set) methodResult) {
						System.out.println("adding a should for variable "
								+ columnName + " with value " + o.toString());
						searchForValue.add(o.toString());
						columnsIn.add(columnName);
						flags.add(BooleanClause.Occur.SHOULD);
					}
				} else {
					if (columnName.equals("publicData")) {
					} else {
						System.out.println("adding a must for variable "
								+ columnName + " with value "
								+ methodResult.toString());
						searchForValue.add(methodResult.toString());
						columnsIn.add(columnName);
						flags.add(BooleanClause.Occur.MUST);
					}
				}
			}
		}

		final String searchArray[] = new String[searchForValue.size()];
		searchForValue.toArray(searchArray);
		final String columnsArray[] = new String[columnsIn.size()];
		columnsIn.toArray(columnsArray);
		final BooleanClause.Occur flagsArray[] = new BooleanClause.Occur[flags
				.size()];
		flags.toArray(flagsArray);
		try {
			final Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(searchArray, columnsArray, flagsArray,
							new StandardAnalyzer());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(query, Sample.class);
			final List<Sample> result = hibQuery.list();
			assertEquals(1, result.size());
			System.out.println(query.toString());
			for (final Sample s : result)
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber());
		} catch (final ParseException e) {
		}

		tx.commit();
	}

	@Test
	public void testFindPublicSamples() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final TermQuery termQuery = new TermQuery(new Term("publicData",
				Boolean.TRUE.toString()));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> results = hibQuery.list();
		assertEquals(3, results.size());
	}

	@Test
	public void testSearchDateRange() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Calendar rightNow = Calendar.getInstance();
		rightNow.set(2008, 7, 25);
		final Date startDate = rightNow.getTime();
		rightNow.set(2008, 8, 1);
		final Date endDate = rightNow.getTime();

		RangeQuery rq = new RangeQuery(new Term("collectionDate", DateTools
				.dateToString(startDate, DateTools.Resolution.DAY)), new Term(
				"collectionDate", DateTools.dateToString(endDate,
						DateTools.Resolution.DAY)), true);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(rq, Sample.class);
		final List<Sample> results = hibQuery.list();
		assertEquals(5, results.size());
	}

	@Test
	public void testPublicOrUserSamples() {
		final User testUser = new User();
		testUser.setId(2);
		testUser.setName("matt");
		testUser.setEmailAddress("fyffem@cs.rpi.edu");

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery(); // Is this public data?
														// final
		TermQuery termQuery = new TermQuery(new Term("publicData", Boolean.TRUE
				.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD); // Is this the
																	// current
																	// user?
		final TermQuery termQuery2 = new TermQuery(new Term(
				"user_emailAddress", testUser.getEmailAddress()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);

		BooleanQuery fullQuery = new BooleanQuery();

		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);

		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getNumber());

		assertEquals(5, results.size());
	}

	@Test
	public void testSampleFilteringByUserWithDates() {
		final User testUser = new User();
		testUser.setId(2);
		testUser.setName("matt");

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery(); // Is this public data?
														// final
		TermQuery termQuery = new TermQuery(new Term("publicData", Boolean.TRUE
				.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD); // Is this the
																	// current
																	// user?
		final TermQuery termQuery2 = new TermQuery(new Term("user_Name",
				testUser.getName()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);

		BooleanQuery fullQuery = new BooleanQuery();

		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);

		// Date Search
		final Calendar rightNow = Calendar.getInstance();
		rightNow.set(2008, 7, 25);
		final Date startDate = rightNow.getTime();
		rightNow.set(2008, 8, 1);
		final Date endDate = rightNow.getTime();

		RangeQuery rq = new RangeQuery(new Term("collectionDate", DateTools
				.dateToString(startDate, DateTools.Resolution.DAY)), new Term(
				"collectionDate", DateTools.dateToString(endDate,
						DateTools.Resolution.DAY)), true);

		fullQuery.add(rq, BooleanClause.Occur.MUST);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getNumber());

		assertEquals(4, results.size());
	}

	@Test
	public void testOxideSearch() {
		final SearchSample searchSamp = new SearchSample();
		Oxide tempOxide = new Oxide();
		tempOxide.setSpecies("al2o3");
		searchSamp.addOxide(tempOxide, 10d, 16d);
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		String columnName;
		Object methodResult = null;
		BooleanQuery fullQuery = new BooleanQuery();
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {

			// column to search on
			columnName = i.columnName();

			// return type of the method
			methodResult = i.get(searchSamp);

			// if there is no value returned in this field...
			if (methodResult == null) { // ignore it

			} else {
				if (columnName.equals("subsample_chemicalAnalysis_oxides")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchOxide o : (Set<SearchOxide>) methodResult) {
							final RangeFilter rangeFilter = new RangeFilter(
									"subsample_chemicalAnalysis_oxides_amount",
									NumberUtils.double2sortableStr(o
											.getLowerBound()), NumberUtils
											.double2sortableStr(o
													.getUpperBound()), true,
									true);
							final TermQuery oxideQuery = new TermQuery(
									new Term(
											"subsample_chemicalAnalysis_oxides_oxide_species",
											o.getSpecies()));
							final FilteredQuery filter = new FilteredQuery(
									oxideQuery, rangeFilter);
							setQuery.add(filter, BooleanClause.Occur.SHOULD);
						}
						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				} else if (columnName
						.equals("subsample_chemicalAnalysis_elements")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchElement o : (Set<SearchElement>) methodResult) {
							final RangeFilter rangeFilter = new RangeFilter(
									"subsample_chemicalAnalysis_elements_amount",
									NumberUtils.double2sortableStr(o
											.getLowerBound()), NumberUtils
											.double2sortableStr(o
													.getUpperBound()), true,
									true);
							final TermQuery elementQuery = new TermQuery(
									new Term(
											"subsample_chemicalAnalysis_elements_element_symbol",
											o.getElementSymbol()));
							final FilteredQuery filter = new FilteredQuery(
									elementQuery, rangeFilter);
							setQuery.add(filter, BooleanClause.Occur.SHOULD);
						}
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				}
			}
		}
	}

	/*
	 * @Test public void testOxideRangeSearch() { final SearchSample searchSamp
	 * = new SearchSample(); Oxide tempOxide = new Oxide();
	 * tempOxide.setSpecies("al2o3"); searchSamp.addOxide(tempOxide, 10f, 16f);
	 * final Session session = InitDatabase.getSession(); final FullTextSession
	 * fullTextSession = Search .createFullTextSession(session);
	 * 
	 * String columnName; Object methodResult = null; BooleanQuery fullQuery =
	 * new BooleanQuery(); SearchProperty[] enums =
	 * SearchSampleProperty.class.getEnumConstants(); for (SearchProperty i :
	 * enums) {
	 * 
	 * // column to search on columnName = i.columnName();
	 * 
	 * // return type of the method methodResult = i.get(searchSamp);
	 * 
	 * // if there is no value returned in this field... if (methodResult ==
	 * null) { // ignore it } else { // otherwise, what type of returned data is
	 * it? if (columnName.equals("subsample_chemicalAnalysis_oxides")) { if
	 * (((Set) methodResult).size() > 0) { final BooleanQuery setQuery = new
	 * BooleanQuery(); for (SearchOxide o : (Set<SearchOxide>) methodResult) {
	 * final RangeFilter rangeFilterOnMin = new RangeFilter(
	 * "subsample_chemicalAnalysis_oxides_minAmount",
	 * NumberUtils.float2sortableStr(-99999), NumberUtils.float2sortableStr(o
	 * .getUpperBound()), true, true); final RangeFilter rangeFilterOnMax = new
	 * RangeFilter( "subsample_chemicalAnalysis_oxides_minAmount",
	 * NumberUtils.float2sortableStr(o .getLowerBound()), NumberUtils
	 * .float2sortableStr(99999), true, true); final TermQuery oxideQuery = new
	 * TermQuery( new Term( "subsample_chemicalAnalysis_oxides_oxide_species",
	 * o.getSpecies())); final FilteredQuery filterOnMinQuery = new
	 * FilteredQuery( oxideQuery, rangeFilterOnMin); final FilteredQuery
	 * filterOnBothQuery = new FilteredQuery( filterOnMinQuery,
	 * rangeFilterOnMax); setQuery.add(filterOnBothQuery,
	 * BooleanClause.Occur.SHOULD); } // require that one of these results be
	 * found in the // full query fullQuery.add(setQuery,
	 * BooleanClause.Occur.MUST); } } } }
	 * 
	 * // Run the query and get the actual results
	 * 
	 * final org.hibernate.Query hibQuery = fullTextSession
	 * .createFullTextQuery(fullQuery, Sample.class); final List<Sample> results
	 * = hibQuery.list(); for (final Sample s : results)
	 * System.out.println("found sample, id is " + s.getId()); assertEquals(1,
	 * results.size());
	 * 
	 * }
	 * 
	 * @Test public void testElementSearch() { final SearchSample searchSamp =
	 * new SearchSample(); Element tempElement = new Element();
	 * tempElement.setSymbol("al"); searchSamp.addElement(tempElement, 4.9f,
	 * 12.0f); final Session session = InitDatabase.getSession(); final
	 * FullTextSession fullTextSession = Search .createFullTextSession(session);
	 * 
	 * String columnName; Object methodResult = null; BooleanQuery fullQuery =
	 * new BooleanQuery(); SearchProperty[] enums =
	 * SearchSampleProperty.class.getEnumConstants(); for (SearchProperty i :
	 * enums) {
	 * 
	 * // column to search on columnName = i.columnName();
	 * 
	 * // return type of the method methodResult = i.get(searchSamp);
	 * 
	 * // if there is no value returned in this field... if (methodResult ==
	 * null) { // ignore it } else { // otherwise, what type of returned data is
	 * it? if (columnName.equals("subsample_chemicalAnalysis_oxides")) { if
	 * (((Set) methodResult).size() > 0) { final BooleanQuery setQuery = new
	 * BooleanQuery(); for (SearchOxide o : (Set<SearchOxide>) methodResult) {
	 * final RangeFilter rangeFilter = new RangeFilter(
	 * "subsample_chemicalAnalysis_oxides_amount",
	 * NumberUtils.float2sortableStr(o .getLowerBound()), NumberUtils
	 * .float2sortableStr(o .getUpperBound()), true, true); final TermQuery
	 * oxideQuery = new TermQuery( new Term(
	 * "subsample_chemicalAnalysis_oxides_oxide_species", o.getSpecies()));
	 * final FilteredQuery filter = new FilteredQuery( oxideQuery, rangeFilter);
	 * setQuery.add(filter, BooleanClause.Occur.SHOULD); } // require that one
	 * of these results be found in the // full query fullQuery.add(setQuery,
	 * BooleanClause.Occur.MUST); } } else if (columnName
	 * .equals("subsample_chemicalAnalysis_elements")) { if (((Set)
	 * methodResult).size() > 0) { final BooleanQuery setQuery = new
	 * BooleanQuery(); for (SearchElement o : (Set<SearchElement>) methodResult)
	 * { final RangeFilter rangeFilter = new RangeFilter(
	 * "subsample_chemicalAnalysis_elements_amount",
	 * NumberUtils.float2sortableStr(o .getLowerBound()), NumberUtils
	 * .float2sortableStr(o .getUpperBound()), true, true); final TermQuery
	 * elementQuery = new TermQuery( new Term(
	 * "subsample_chemicalAnalysis_elements_element_symbol",
	 * o.getElementSymbol())); final FilteredQuery filter = new FilteredQuery(
	 * elementQuery, rangeFilter); setQuery.add(filter,
	 * BooleanClause.Occur.SHOULD); } // require that one of these results be
	 * found in the // full query fullQuery.add(setQuery,
	 * BooleanClause.Occur.MUST); } } } }
	 * 
	 * // Run the query and get the actual results
	 * 
	 * final org.hibernate.Query hibQuery = fullTextSession
	 * .createFullTextQuery(fullQuery, Sample.class); final List<Sample> results
	 * = hibQuery.list(); for (final Sample s : results)
	 * System.out.println("found sample, id is " + s.getId()); assertEquals(1,
	 * results.size());
	 * 
	 * }
	 * 
	 * @Test public void testElementRangeSearch() { final SearchSample
	 * searchSamp = new SearchSample(); Element tempElement = new Element();
	 * tempElement.setSymbol("al"); searchSamp.addElement(tempElement, 4.9f,
	 * 12.0f); final Session session = InitDatabase.getSession(); final
	 * FullTextSession fullTextSession = Search .createFullTextSession(session);
	 * 
	 * String columnName; Object methodResult = null; BooleanQuery fullQuery =
	 * new BooleanQuery(); SearchProperty[] enums =
	 * SearchSampleProperty.class.getEnumConstants(); for (SearchProperty i :
	 * enums) {
	 * 
	 * // column to search on columnName = i.columnName();
	 * 
	 * // return type of the method methodResult = i.get(searchSamp);
	 * 
	 * // if there is no value returned in this field... if (methodResult ==
	 * null) { // ignore it } else { // otherwise, what type of returned data is
	 * it? if (columnName.equals("subsample_chemicalAnalysis_elements")) { if
	 * (((Set) methodResult).size() > 0) { final BooleanQuery setQuery = new
	 * BooleanQuery(); for (SearchElement o : (Set<SearchElement>) methodResult)
	 * { final RangeFilter rangeFilterOnMin = new RangeFilter(
	 * "subsample_chemicalAnalysis_elements_minAmount",
	 * NumberUtils.float2sortableStr(-99999), NumberUtils.float2sortableStr(o
	 * .getUpperBound()), true, true); final RangeFilter rangeFilterOnMax = new
	 * RangeFilter( "subsample_chemicalAnalysis_elements_minAmount",
	 * NumberUtils.float2sortableStr(o .getLowerBound()), NumberUtils
	 * .float2sortableStr(99999), true, true); final TermQuery elementQuery =
	 * new TermQuery( new Term(
	 * "subsample_chemicalAnalysis_elements_element_symbol",
	 * o.getElementSymbol())); final FilteredQuery filterOnMinQuery = new
	 * FilteredQuery( elementQuery, rangeFilterOnMin); final FilteredQuery
	 * filterOnBothQuery = new FilteredQuery( filterOnMinQuery,
	 * rangeFilterOnMax); setQuery.add(filterOnBothQuery,
	 * BooleanClause.Occur.SHOULD); } // require that one of these results be
	 * found in the // full query fullQuery.add(setQuery,
	 * BooleanClause.Occur.MUST); } } } }
	 * 
	 * // Run the query and get the actual results
	 * 
	 * final org.hibernate.Query hibQuery = fullTextSession
	 * .createFullTextQuery(fullQuery, Sample.class); final List<Sample> results
	 * = hibQuery.list(); for (final Sample s : results)
	 * System.out.println("found sample, id is " + s.getId()); assertEquals(1,
	 * results.size());
	 * 
	 * }
	 */

	@Test
	public void testSampleSearch() {
		final SearchSample searchSamp = new SearchSample();
		Oxide tempOxide = new Oxide();
		tempOxide.setSpecies("AL2O3");
		searchSamp.addOxide(tempOxide, 4d, 8d);

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();

		String columnName;
		Object methodResult = null;
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {
			// column to search on
			columnName = i.columnName();

			// return type of the method
			methodResult = i.get(searchSamp);

			// if there is no value returned in this field...
			if (methodResult == null) {
				// ignore it
			} else { // otherwise, what type of returned data is it?
				if (columnName.equals("subsample_chemicalAnalysis_oxides")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchOxide o : (Set<SearchOxide>) methodResult) {
							/*
							 * final RangeFilter rangeFilterOnMin = new
							 * RangeFilter
							 * ("subsample_chemicalAnalysis_oxides_minAmount",
							 * NumberUtils.float2sortableStr(-99999f),
							 * NumberUtils.float2sortableStr(o
							 * .getUpperBound()),true, true); final RangeFilter
							 * rangeFilterOnMax = newRangeFilter(
							 * "subsample_chemicalAnalysis_oxides_minAmount",
							 * NumberUtils.float2sortableStr(o
							 * .getLowerBound()),
							 * NumberUtils.float2sortableStr(99999f),true,
							 * true);
							 */
							final RangeFilter rangeFilterOnMin = new RangeFilter(
									"subsample_chemicalAnalysis_oxides_amount",
									NumberUtils.float2sortableStr(-10f),
									NumberUtils.float2sortableStr(10f), true,
									true);
							final TermQuery oxideQuery = new TermQuery(
									new Term(
											"subsample_chemicalAnalysis_oxides_oxide_species",
											o.getSpecies()));
							final FilteredQuery filterOnMinQuery = new FilteredQuery(
									oxideQuery, rangeFilterOnMin);
							// final FilteredQuery filterOnBothQuery = new
							// FilteredQuery(filterOnMinQuery,
							// rangeFilterOnMax);
							setQuery.add(filterOnMinQuery,
									BooleanClause.Occur.SHOULD);
						}
						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				}

			}
		}

		// Run the query and get the actual results

		QueryParser parser = new QueryParser("title", new StandardAnalyzer());
		try {
			org.apache.lucene.search.Query luceneQuery = parser.parse(fullQuery
					.toString());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(luceneQuery, Sample.class);
			final List<Sample> results = hibQuery.list();
			assertEquals(1, results.size());
		} catch (Exception e) {
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(fullQuery, Sample.class);
			final List<Sample> results = hibQuery.list();
			assertEquals(1, results.size());
		} finally {
			session.close();
		}

	}
}

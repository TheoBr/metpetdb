package edu.rpi.metpetdb.server.search;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.User;

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
		};// , BooleanClause.Occur.MUST};

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
		final SearchSampleDTO searchSamp = new SearchSampleDTO();

		// searchSamp.setSesarNumber("000000000");
		searchSamp.setAlias("1");
		searchSamp.addPossibleRockType("logitech");
		searchSamp.addPossibleRockType("rockie rock");

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
				.dateToString(startDate, DateTools.Resolution.DAY)),
				new Term("collectionDate", DateTools.dateToString(endDate,
						DateTools.Resolution.DAY)), true);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(rq, Sample.class);
		final List<Sample> results = hibQuery.list();
		assertEquals(5, results.size());
	}

	@Test
	public void testPublicOrUserSamples() {
		final UserDTO testUser = new UserDTO();
		testUser.setId(2);
		testUser.setFirstName("matt");
		testUser.setEmailAddress("fyffem@cs.rpi.edu");

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery();
		// Is this public data?
		final TermQuery termQuery = new TermQuery(new Term("publicData",Boolean.TRUE.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD);
		// Is this the current user?
		//final TermQuery termQuery2 = new TermQuery(new Term("user_firstName",
		//		testUser.getFirstName()));
		final TermQuery termQuery2 = new TermQuery(new Term("user_emailAddress",
				testUser.getEmailAddress()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);

		BooleanQuery fullQuery = new BooleanQuery();

		// in the full query, make it mandatory that the privacy requirements
		// are met
		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);

		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getAlias());
		
		assertEquals(5, results.size());
	}

	@Test
	public void testSampleFilteringByUserWithDates() {
		final UserDTO testUser = new UserDTO();
		testUser.setId(2);
		testUser.setFirstName("matt");

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery();
		// Is this public data?
		final TermQuery termQuery = new TermQuery(new Term("publicData",Boolean.TRUE.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD);
		// Is this the current user?
		final TermQuery termQuery2 = new TermQuery(new Term("user_firstName",
				testUser.getFirstName()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);

		BooleanQuery fullQuery = new BooleanQuery();

		// in the full query, make it mandatory that the privacy requirements
		// are met
		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);

		// Date Search
		final Calendar rightNow = Calendar.getInstance();
		rightNow.set(2008, 7, 25);
		final Date startDate = rightNow.getTime();
		rightNow.set(2008, 8, 1);
		final Date endDate = rightNow.getTime();

		RangeQuery rq = new RangeQuery(new Term("collectionDate", DateTools
				.dateToString(startDate, DateTools.Resolution.DAY)),
				new Term("collectionDate", DateTools.dateToString(endDate,
						DateTools.Resolution.DAY)), true);
		
		fullQuery.add(rq, BooleanClause.Occur.MUST);
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getAlias());

		assertEquals(4, results.size());
	}
	
	@Test
	public void testSampleSearch() {
		// In the actual search, we will receive User information as a parameter
		final UserDTO testUser = new UserDTO();
		testUser.setId(2);
		testUser.setFirstName("matt");

		// In the actual search, we will receive search criteria as a SearchSampleDTO
		final SearchSampleDTO searchSamp = new SearchSampleDTO();
//		searchSamp.setAlias("1");
		searchSamp.addPossibleRockType("logitech");
		searchSamp.addPossibleRockType("rockie rock");
		
		final Calendar rightNow = Calendar.getInstance();
		rightNow.set(2008, 7, 25);
		final Date firstDate = rightNow.getTime();
		rightNow.set(2008, 8, 1);
		final Date secondDate = rightNow.getTime();
		
		searchSamp.setCollectionDateRange(new DateSpan(firstDate, secondDate));

		
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();
		
		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery();
		// Is this public data?
		final TermQuery termQuery = new TermQuery(new Term("publicData",Boolean.TRUE.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD);
		// Is this the current user?
		final TermQuery termQuery2 = new TermQuery(new Term("user_firstName",
				testUser.getFirstName()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);

		// in the full query, make it mandatory that the privacy requirements are met
		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);
		// Actual Search
		
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
				if (methodResult instanceof Set) { // if a set of data is returned, it should be an OR query
					if(((Set)methodResult).size() > 0)
					{
						final BooleanQuery setQuery = new BooleanQuery();
						for (Object o : (Set) methodResult) {
							// iterate through each item and add it to the query
							final TermQuery objectQuery = new TermQuery(new Term(columnName, o.toString()));
							setQuery.add(objectQuery, BooleanClause.Occur.SHOULD);
						}
										
						// require that one of these results be found in the full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}					
				} else if(methodResult instanceof DateSpan){ // if the data is a DateSpan
					// Get the start date of the span
					final Date startDate = ((DateSpan)methodResult).getStartAsDate();
					final Date realStartDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate());
					// Get the end date of the span
					final Date endDate = ((DateSpan)methodResult).getEndAsDate();
					final Date realEndDate = new Date(endDate.getYear(),endDate.getMonth(), endDate.getDate());

					// Do a range query on these dates
					RangeQuery rq = new RangeQuery(new Term("collectionDate", DateTools
							.dateToString(realStartDate, DateTools.Resolution.DAY)),
							new Term("collectionDate", DateTools.dateToString(realEndDate,
									DateTools.Resolution.DAY)), true);					
					fullQuery.add(rq, BooleanClause.Occur.MUST);
				}else if(columnName.equals("location")){ // if the column being searched on is location
					if (searchSamp.getBoundingBox() != null) { // if there is a bounding box for this sample
						session.enableFilter("boundingBox").setParameter("polygon",
								searchSamp.getBoundingBox()); // filter results based on the box
					}
				}else {  // it's just a standard string, do a term search
					final TermQuery stringQuery = new TermQuery(new Term(columnName, methodResult.toString()));
					fullQuery.add(stringQuery, BooleanClause.Occur.MUST);
				}
			}
		}
				
		// Run the query and get the actual results		
		
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getAlias());
		assertEquals(3, results.size());
	}
	
}

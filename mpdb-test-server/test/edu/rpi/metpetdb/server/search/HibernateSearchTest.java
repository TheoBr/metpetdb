package edu.rpi.metpetdb.server.search;

import java.sql.Timestamp;
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
						+ s.getSesarNumber() + " username is "
						+ s.getOwner().getUsername());
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
			for (final Sample s : result)
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber() + " username is "
						+ s.getOwner().getUsername());
		} catch (final ParseException e) {
		}

		tx.commit();
	}

	@Test
	public void testSampleSearchOnUsers(){
		final UserDTO testUser = new UserDTO();
		testUser.setId(2);
		testUser.setUsername("matt");
		
		Timestamp startDate = new Timestamp(0);
		Timestamp endDate = new Timestamp(108, 8, 1, 11, 44, 35, 50);

		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);
		
		final Transaction tx = fullTextSession.beginTransaction();
		
		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new  BooleanQuery();
		// Is this public data?
		final TermQuery termQuery = new TermQuery(new Term("public_data", "Y"));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD);
		// Is this the current user?
		final TermQuery termQuery2 = new TermQuery(new Term("user_username", testUser.getUsername()));
		privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);
		
		BooleanQuery fullQuery = new BooleanQuery();
	
		// in the full query, make it mandatory that the privacy requirements are met
		fullQuery.add(privacyQuery, BooleanClause.Occur.MUST);
		
		// does a range query based on date on the field startDate.
		if(startDate!=null && endDate!=null)
        {         
           RangeQuery rq = new RangeQuery( new Term("collectionDate",DateTools.dateToString(startDate, DateTools.Resolution.MILLISECOND)),new Term("collectionDate",DateTools.dateToString(endDate, DateTools.Resolution.MILLISECOND)), true );
           fullQuery.add(rq, BooleanClause.Occur.MUST);
        }   	
		
		final org.hibernate.Query hibQuery = fullTextSession
			.createFullTextQuery(fullQuery, Sample.class);
		final List<Sample> results = hibQuery.list();

		for (final Sample s : results)
			System.out.println("found sample, alias is " + s.getAlias());
		
		assertEquals(5, results.size());
		
		tx.commit();
	}
	
}

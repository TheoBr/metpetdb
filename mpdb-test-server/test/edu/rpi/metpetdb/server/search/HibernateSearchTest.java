package edu.rpi.metpetdb.server.search;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.junit.Test;

import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Mineral;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.SampleMineral;
import edu.rpi.metpetdb.server.model.User;

public class HibernateSearchTest extends DatabaseTestCase {

	public HibernateSearchTest() {
		super("test-data/test-sample-data.xml");
	}

	/*
	 * @Test public void testCreateIndex() { final Session session =
	 * InitDatabase.getSession(); FullTextSession fullTextSession =
	 * Search.createFullTextSession(session); Transaction tx =
	 * fullTextSession.beginTransaction(); List<Sample> samples =
	 * session.createQuery("from Sample as sample").list(); for (Sample sample :
	 * samples) { fullTextSession.index(sample); } tx.commit(); //index are
	 * written at commit time }
	 */

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

		final Transaction tx = fullTextSession.beginTransaction();

		final TermQuery termQuery = new TermQuery(new Term("alias", "1"));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, rock type is " + s.getRockType());

		assertEquals(1, result.size());
		tx.commit();
	}

	@Test
	public void testTermSearch2() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

		final TermQuery termQuery = new TermQuery(new Term("rockType", "ibm"));
		final org.hibernate.Query hibQuery = fullTextSession
				.createFullTextQuery(termQuery, Sample.class);
		final List<Sample> result = hibQuery.list();

		for (final Sample s : result)
			System.out.println("found sample, rock type is " + s.getRockType());

		tx.commit();
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

		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
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

		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
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
		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
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
	public void testSampleContainsMineral() {
		final Session session = InitDatabase.getSession();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);

		final Transaction tx = fullTextSession.beginTransaction();

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
				System.out.println("find sample with mineral " + m.getName());
			}
		}

		assertEquals(1, result.size());
		tx.commit();
	}
}

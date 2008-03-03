package edu.rpi.metpetdb.server.search;

import java.util.List;

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

import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.model.Sample;
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
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		Transaction tx = fullTextSession.beginTransaction();
		List<Sample> samples = session.createQuery("from Sample as sample")
				.list();
		for (Sample sample : samples) {
			fullTextSession.index(sample);
		}
		tx.commit(); // index are written at commit time

		tx = fullTextSession.beginTransaction();
		List<User> users = session.createQuery("from User as user").list();
		for (User user : users) {
			fullTextSession.index(user);
		}
		tx.commit(); // index are written at commit time
	}

	@Test
	public void testTermSearch() {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		TermQuery termQuery = new TermQuery(new Term("rockType", "rock"));
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
				termQuery, Sample.class);
		List<Sample> result = hibQuery.list();

		for (Sample s : result) {
			System.out.println("found sample, rock type is " + s.getRockType());
		}

		tx.commit();
	}

	@Test
	public void testTermSearch2() {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		TermQuery termQuery = new TermQuery(new Term("rockType", "ibm"));
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
				termQuery, Sample.class);
		List<Sample> result = hibQuery.list();

		for (Sample s : result) {
			System.out.println("found sample, rock type is " + s.getRockType());
		}

		tx.commit();
	}

	@Test
	public void testRangeSearch() {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		RangeQuery rangeQuery = new RangeQuery(new Term("sesarNumber",
				"000000004"), new Term("sesarNumber", "000000009"), true);
		org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
				rangeQuery, Sample.class);
		List<Sample> result = hibQuery.list();

		for (Sample s : result) {
			System.out.println("found sample, sesar number is "
					+ s.getSesarNumber());
		}

		tx.commit();
	}

	@Test
	public void testMultiFields() {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		System.out.println("in multifield search");
		String[] searchFor = { "000000004", "rockie rock" };
		String[] columnsIn = { "sesarNumber", "rockType" };
		BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
				BooleanClause.Occur.MUST };

		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
		try {
			Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(searchFor, columnsIn, flags, new StandardAnalyzer());
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
					query, Sample.class);
			List<Sample> result = hibQuery.list();

			for (Sample s : result) {
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber());
			}
		} catch (ParseException e) {
		}

		tx.commit();
	}

	@Test
	public void testJoin() {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

		System.out.println("in test join");
		String[] searchFor = { "000000004", "anthony" };
		String[] columnsIn = { "sesarNumber", "user_username" };
		BooleanClause.Occur[] flags = { BooleanClause.Occur.MUST,
				BooleanClause.Occur.MUST };// , BooleanClause.Occur.MUST};

		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
		try {
			Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(searchFor, columnsIn, flags, new StandardAnalyzer());
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(
					query, Sample.class);
			List<Sample> result = hibQuery.list();

			for (Sample s : result) {
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber() + " username is "
						+ s.getOwner().getUsername());
			}
		} catch (ParseException e) {
		}

		tx.commit();
	}
}

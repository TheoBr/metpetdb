package edu.rpi.metpetdb.server.search;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.model.Sample;

public class SearchDb {
	public SearchDb() {
	}

	public static List<Sample> sampleSearch(SearchSampleDTO searchSamp) {

		final Session session = DataStore.open();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		Transaction tx = fullTextSession.beginTransaction();

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
					} else if (columnName.equals("collectionDate")) {
					} else if (!columnName.equals("location")) {
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
		List<Sample> result = null;
		try {
			final org.hibernate.Query hibQuery;
			if (searchArray.length > 0) {
				final Query query = MultiFieldQueryParser.parse(searchArray,
						columnsArray, flagsArray, new StandardAnalyzer());
				hibQuery = fullTextSession.createFullTextQuery(query,
						Sample.class);
			} else {
				//If they do not specify a search query just get all of the samples
				//later on we will filter if we must
				//TODO fix so that we sort by the correct sort parameter
				hibQuery = session.getNamedQuery("Sample.all/alias");
			}
			// Check for any filters
			if (searchSamp.getBoundingBox() != null) {
				session.enableFilter("boundingBox").setParameter("polygon",
						searchSamp.getBoundingBox());
			}
			result = hibQuery.list();

			for (final Sample s : result)
				System.out.println("found sample, sesar number is "
						+ s.getSesarNumber() +  " username is "
						+ s.getOwner().getUsername());
		} catch (final ParseException e) {
		}

		tx.commit();
		return result;
	}
}

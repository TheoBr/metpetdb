package edu.rpi.metpetdb.server.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.AttributeDTO;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.model.Sample;

public class SearchDb {
	public SearchDb() {
	}

	public static List<Sample> sampleSearch(
			final List<AttributeDTO> AttributePairs) {
		final Session session = DataStore.open();
		final FullTextSession fullTextSession = Search
				.createFullTextSession(session);
		final Transaction tx = fullTextSession.beginTransaction();

		final ArrayList<String> searchFor = new ArrayList<String>();
		final ArrayList<String> columnsIn = new ArrayList<String>();
		final ArrayList<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>();
/*		for (final AttributeDTO attribute : AttributePairs) {
			searchFor.add(attribute.getValue());
			columnsIn.add(attribute.getAttribute());
			flags.add(BooleanClause.Occur.MUST);
		}*/

		/*
		 * BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
		 * BooleanClause.Occur.MUST, BooleanClause.Occur.MUST_NOT};
		 */
		try {
			final Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse((String[]) (searchFor.toArray()),
							(String[]) (columnsIn.toArray()),
							(BooleanClause.Occur[]) (flags.toArray()),
							new StandardAnalyzer());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(query, Sample.class);
			return (List<Sample>) hibQuery.list();
		} catch (ParseException e) {
		}

		tx.commit();
		return new ArrayList<Sample>();
	}
}

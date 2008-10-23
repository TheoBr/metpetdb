package edu.rpi.metpetdb.server.search;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
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
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.server.DataStore;

public class SearchDb {
	public SearchDb() {
	}

	public static List<Sample> sampleSearch(SearchSample searchSamp,
			User userSearching) {

		final Session session = DataStore.open();
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();

		// Check if it's public data or if the user is this user
		BooleanQuery privacyQuery = new BooleanQuery();
		// Is this public data?
		final TermQuery termQuery = new TermQuery(new Term("publicData",
				Boolean.TRUE.toString()));
		privacyQuery.add(termQuery, BooleanClause.Occur.SHOULD);
		// Is this the current user?
		if (userSearching != null) {
			final TermQuery termQuery2 = new TermQuery(new Term(
					"user_id", (new Integer(userSearching.getId())).toString()));
			privacyQuery.add(termQuery2, BooleanClause.Occur.SHOULD);
		}

		// in the full query, make it mandatory that the privacy requirements
		// are met
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
				if (columnName.equals("subsample_chemicalAnalysis_oxides")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchOxide o : (Set<SearchOxide>) methodResult) {
							final RangeFilter rangeFilterOnMin = new RangeFilter("subsample_chemicalAnalysis_oxides_minAmount", NumberUtils.float2sortableStr(-99999f), NumberUtils.float2sortableStr(o
									.getUpperBound()),true, true);
							final RangeFilter rangeFilterOnMax = new RangeFilter("subsample_chemicalAnalysis_oxides_maxAmount", NumberUtils.float2sortableStr(o
									.getLowerBound()), NumberUtils.float2sortableStr(99999f),true, true);
							final TermQuery oxideQuery = new TermQuery(new Term("subsample_chemicalAnalysis_oxides_oxide_species",							o.getSpecies()));
							final FilteredQuery filterOnMinQuery = new FilteredQuery(oxideQuery, rangeFilterOnMin);
							final FilteredQuery filterOnBothQuery = new FilteredQuery(filterOnMinQuery, rangeFilterOnMax);
							setQuery.add(filterOnBothQuery, BooleanClause.Occur.SHOULD);
						}
						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				}
				else if (columnName
						.equals("subsample_chemicalAnalysis_elements")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchElement o : (Set<SearchElement>) methodResult) {
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
							setQuery.add(filterOnBothQuery, BooleanClause.Occur.SHOULD);
						}
						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				}
				else if (methodResult instanceof Set) { // if a set of data is
					// returned, it should be an
					// OR query
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (Object o : (Set) methodResult) {
							// iterate through each item and add it to the query
							final TermQuery objectQuery = new TermQuery(
									new Term(columnName, o.toString()));
							setQuery.add(objectQuery,
									BooleanClause.Occur.SHOULD);
						}

						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				} else if (methodResult instanceof DateSpan) { // if the data is
					// a DateSpan
					// Get the start date of the span
					final Date startDate = ((DateSpan) methodResult)
							.getStartAsDate();
					final Date realStartDate = new Date(startDate.getYear(),
							startDate.getMonth(), startDate.getDate());
					// Get the end date of the span
					final Date endDate = ((DateSpan) methodResult)
							.getEndAsDate();
					final Date realEndDate = new Date(endDate.getYear(),
							endDate.getMonth(), endDate.getDate());

					// Do a range query on these dates
					RangeQuery rq = new RangeQuery(new Term("collectionDate",
							DateTools.dateToString(realStartDate,
									DateTools.Resolution.DAY)), new Term(
							"collectionDate", DateTools.dateToString(
									realEndDate, DateTools.Resolution.DAY)),
							true);
					fullQuery.add(rq, BooleanClause.Occur.MUST);
				} else if (columnName.equals("location")) { // if the column
					// being searched on
					// is location
					if (searchSamp.getBoundingBox() != null) { // if there is a
						// bounding box
						// for this
						// sample
						session.enableFilter("boundingBox").setParameter(
								"polygon", searchSamp.getBoundingBox()); // filter
						// results
						// based
						// on
						// the
						// box
					}
				} else { // it's just a standard string, do a term search
					if(columnName.length() > 0)
					{
						final TermQuery stringQuery = new TermQuery(new Term(
								columnName, methodResult.toString()));
						fullQuery.add(stringQuery, BooleanClause.Occur.MUST);
					}
				}
			}
		}

		// Run the query and get the actual results

		QueryParser parser = new QueryParser("title", new StandardAnalyzer() );
		try{
			org.apache.lucene.search.Query luceneQuery = parser.parse(fullQuery.toString());
			final org.hibernate.Query hibQuery = fullTextSession
			.createFullTextQuery(luceneQuery, Sample.class);
			final List<Sample> results = hibQuery.list();
			return results;
			
		}
		catch(Exception e)
		{
			final org.hibernate.Query hibQuery = fullTextSession
			.createFullTextQuery(fullQuery, Sample.class);
			final List<Sample> results = hibQuery.list();
			return results;
		}
		finally
		{
			session.close();
		}
		
	}
}

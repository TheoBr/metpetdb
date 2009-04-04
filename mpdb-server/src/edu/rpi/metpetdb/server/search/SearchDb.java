package edu.rpi.metpetdb.server.search;

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
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeFilter;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.solr.util.NumberUtils;
import org.hibernate.CallbackException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.security.ConvertSecurityException;

public class SearchDb {
	public SearchDb() {
	}

	public static Results<Sample> sampleSearch(final PaginationParameters p,
			SearchSample searchSamp, User userSearching) throws MpDbException {

		List<Filter> filters = new LinkedList<Filter>();
		// Either do chemical analysis -> subsample -> Sample search if they
		// have chem anal restrictions
		// Or just Sample search if no chem anal restrictions

		final User u = userSearching;
		final int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();
		Session session = DataStore.open();
		DataStore.enableSecurityFilters(session, userId);
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		boolean haveChemicalProperties = checkForChemicalProperties(searchSamp);
		Query fullQuery;

		if (!haveChemicalProperties) {
			fullQuery = getSamplesQuery(searchSamp, userSearching, session);
		} else {
			// Get chemical analyses first
			Query tempChemQuery = getChemicalsQuery(searchSamp, userSearching,
					session);

			// Get the sample ids that correspond to the chemical analyses.
			final FullTextQuery chemQuery = fullTextSession
					.createFullTextQuery(tempChemQuery, ChemicalAnalysis.class);
			final Results<ChemicalAnalysis> CAResults = new Results<ChemicalAnalysis>(
					chemQuery.getResultSize(), chemQuery.list());
			for (int i = 0; i < CAResults.getCount(); i++) {
				searchSamp.addPossibleSampleIds(CAResults.getList().get(i)
						.getSample().getId());
			}

			if (CAResults.getCount() == 0) // no chemical analysis fit search,
											// no reason to try finding samples
											// with "no matches"
			{
				return null;
			}
			// Get samples third... will need to incorporate top two pieces
			fullQuery = getSamplesQuery(searchSamp, userSearching, session);
		}

		// Actual Search

		// Do in for when have ids
		// session.createCriteria(Sample.class).add(Restrictions.in(propertyName,
		// values))

		System.out.println("Search Query:" + fullQuery.toString());

		final FullTextQuery hibQuery = fullTextSession.createFullTextQuery(
				fullQuery, Sample.class);

		if (p != null) {
			hibQuery.setFirstResult(p.getFirstResult());
			hibQuery.setMaxResults(p.getMaxResults());
		}
		try {
			final Results<Sample> results = new Results<Sample>(hibQuery
					.getResultSize(), hibQuery.list());
			return results;
		} catch (CallbackException e) {
			session.clear();
			throw ConvertSecurityException.convertToException(e);
		} finally {
			session.close();
		}
	}

	public static Results<ChemicalAnalysis> chemicalAnalysisSearch(
			final PaginationParameters p, SearchSample searchSamp,
			User userSearching) throws MpDbException {
		List<Filter> filters = new LinkedList<Filter>();
		final User u = userSearching;
		final int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();
		Session session = DataStore.open();
		DataStore.enableSecurityFilters(session, userId);
		FullTextSession fullTextSession = Search.createFullTextSession(session);

		boolean haveSampleProperties = checkForSampleProperties(searchSamp);
		Query fullQuery;

		if (!haveSampleProperties) {
			fullQuery = getChemicalsQuery(searchSamp, userSearching, session);
		} else {

			// Get samples first
			Query tempSampleQuery = getSamplesQuery(searchSamp, userSearching,
					session);

			// // Get the sample ids that correspond to the chemical analyses.
			final FullTextQuery sampleQuery = fullTextSession
					.createFullTextQuery(tempSampleQuery, Sample.class);
			final Results<Sample> SResults = new Results<Sample>(sampleQuery
					.getResultSize(), sampleQuery.list());
			for (int i = 0; i < SResults.getCount(); i++) {
				Set<Subsample> tempSubsamples = SResults.getList().get(i)
						.getSubsamples();
				for (Subsample subsample : tempSubsamples) {
					searchSamp.addPossibleSubsampleIds(subsample.getId());
				}
			}

			if (SResults.getCount() == 0) // no chemical analysis fit search, no
											// reason to try finding samples
											// with "no matches"
			{
				return null;
			}
			// Get samples third... will need to incorporate top two pieces

			// Get chemical analyses third... will need to incorporate top two
			// pieces
			fullQuery = getChemicalsQuery(searchSamp, userSearching, session);
		}

		System.out.println("Search Query:" + fullQuery.toString());

		final FullTextQuery hibQuery = fullTextSession.createFullTextQuery(
				fullQuery, ChemicalAnalysis.class);

		if (p != null) {
			hibQuery.setFirstResult(p.getFirstResult());
			hibQuery.setMaxResults(p.getMaxResults());
		}
		try {
			final Results<ChemicalAnalysis> results = new Results<ChemicalAnalysis>(
					hibQuery.getResultSize(), hibQuery.list());
			return results;
		} catch (CallbackException e) {
			session.clear();
			throw ConvertSecurityException.convertToException(e);
		} finally {
			session.close();
		}
	}

	public static Query getSamplesQuery(SearchSample searchSamp,
			User userSearching, Session session) {
		List<String> queries = new LinkedList<String>();
		List<String> columnsIn = new LinkedList<String>();
		List<BooleanClause.Occur> flags = new LinkedList<BooleanClause.Occur>();

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
				if (i.isSampleAttr()) {
					if (columnName.equals("location")) { // if the column
						// being searched on
						// is location
						if (searchSamp.getBoundingBox() != null) { // if there
							// is a
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
					} else if (columnName.equals("publicData")) {
						// incorporate privacy stuff
						fullQuery.add(getPrivacyQuery(Integer
								.parseInt(methodResult.toString()),
								userSearching), BooleanClause.Occur.MUST);
					} else if (methodResult instanceof Set) { // if a set of
						// data is
						// returned, it should be an
						// OR query

						List<String> setQueries = new LinkedList<String>();
						List<String> setColumnsIn = new LinkedList<String>();
						List<BooleanClause.Occur> setFlags = new LinkedList<BooleanClause.Occur>();

						if (((Set) methodResult).size() > 0) {
							for (Object o : (Set) methodResult) {
								// iterate through each item and add it to the
								// query
								final TermQuery objectQuery = new TermQuery(
										new Term(columnName, o.toString()));
								setQueries.add(objectQuery.toString());
								setColumnsIn.add(columnName);
								setFlags.add(BooleanClause.Occur.SHOULD);
							}

							// require that one of these results be found in the
							// full query
							if (setQueries.size() > 0) {
								fullQuery.add(getQuery(setQueries,
										setColumnsIn, setFlags),
										BooleanClause.Occur.MUST);
							}
						}
					} else if (methodResult instanceof DateSpan) { // if the
						// data is
						// a DateSpan
						// Get the start date of the span
						final Date startDate = ((DateSpan) methodResult)
								.getStartAsDate();
						final Date realStartDate = new Date(
								startDate.getYear(), startDate.getMonth(),
								startDate.getDate());
						// Get the end date of the span
						final Date endDate = ((DateSpan) methodResult)
								.getEndAsDate();
						final Date realEndDate = new Date(endDate.getYear(),
								endDate.getMonth(), endDate.getDate());

						// Do a range query on these dates
						RangeQuery rq = new RangeQuery(new Term(columnName,
								DateTools.dateToString(realStartDate,
										DateTools.Resolution.DAY)), new Term(
								columnName, DateTools.dateToString(realEndDate,
										DateTools.Resolution.DAY)), true);
						queries.add(rq.toString());
						columnsIn.add(columnName);
						flags.add(BooleanClause.Occur.MUST);
					} else { // it's just a standard string, do a term search
						if (columnName.length() > 0) {
							final TermQuery stringQuery = new TermQuery(
									new Term(columnName, methodResult
											.toString()));
							queries.add(stringQuery.toString());
							columnsIn.add(columnName);
							flags.add(BooleanClause.Occur.MUST);
						}
					}
				}
			}
		}

		// Run the query and get the actual results
		if (queries.size() > 0) {
			fullQuery.add(getQuery(queries, columnsIn, flags),
					BooleanClause.Occur.MUST);
		}

		return fullQuery;
	}

	public static Query getChemicalsQuery(SearchSample searchSamp,
			User userSearching, Session session) {
		List<String> queries = new LinkedList<String>();
		List<String> columnsIn = new LinkedList<String>();
		List<BooleanClause.Occur> flags = new LinkedList<BooleanClause.Occur>();

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

			if (i.isChemicalAnalysisAttr()) {
				// if there is no value returned in this field...
				if (columnName.equals("oxides")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchOxide o : (Set<SearchOxide>) methodResult) {
							final RangeFilter rangeFilterOnMin = new RangeFilter(
									"oxides_minAmount", NumberUtils
											.double2sortableStr(-99999999f),
									NumberUtils.double2sortableStr(o
											.getUpperBound()), true, true);
							final RangeFilter rangeFilterOnMax = new RangeFilter(
									"oxides_maxAmount", NumberUtils
											.double2sortableStr(o
													.getLowerBound()),
									NumberUtils.double2sortableStr(99999999f),
									true, true);
							final TermQuery oxideQuery = new TermQuery(
									new Term("oxides_oxide_species", o
											.getSpecies()));
							final FilteredQuery filterOnMinQuery = new FilteredQuery(
									oxideQuery, rangeFilterOnMin);
							final FilteredQuery filterOnBothQuery = new FilteredQuery(
									filterOnMinQuery, rangeFilterOnMax);
							setQuery.add(filterOnBothQuery,
									BooleanClause.Occur.SHOULD);
							fullQuery.add(setQuery, BooleanClause.Occur.MUST);
						}
					}
				} else if (columnName.equals("elements")) {
					if (((Set) methodResult).size() > 0) {
						final BooleanQuery setQuery = new BooleanQuery();
						for (SearchElement o : (Set<SearchElement>) methodResult) {
							final RangeFilter rangeFilterOnMin = new RangeFilter(
									"elements_minAmount", NumberUtils
											.double2sortableStr(-99999999f),
									NumberUtils.double2sortableStr(o
											.getUpperBound()), true, true);
							final RangeFilter rangeFilterOnMax = new RangeFilter(
									"elements_maxAmount", NumberUtils
											.double2sortableStr(o
													.getLowerBound()),
									NumberUtils.double2sortableStr(99999999f),
									true, true);
							final TermQuery elementQuery = new TermQuery(
									new Term("elements_element_symbol", o
											.getElementSymbol()));
							final FilteredQuery filterOnMinQuery = new FilteredQuery(
									elementQuery, rangeFilterOnMin);
							final FilteredQuery filterOnBothQuery = new FilteredQuery(
									filterOnMinQuery, rangeFilterOnMax);
							setQuery.add(filterOnBothQuery,
									BooleanClause.Occur.SHOULD);
						}
						// require that one of these results be found in the
						// full query
						fullQuery.add(setQuery, BooleanClause.Occur.MUST);
					}
				} else if (columnName.equals("publicData")) {
					// incorporate privacy stuff
					fullQuery.add(getPrivacyQuery(Integer.parseInt(methodResult
							.toString()), userSearching),
							BooleanClause.Occur.MUST);
				} else if (methodResult instanceof Set) { // if a set of
					// data is
					// returned, it should be an
					// OR query

					List<String> setQueries = new LinkedList<String>();
					List<String> setColumnsIn = new LinkedList<String>();
					List<BooleanClause.Occur> setFlags = new LinkedList<BooleanClause.Occur>();

					if (((Set) methodResult).size() > 0) {
						for (Object o : (Set) methodResult) {
							// iterate through each item and add it to the
							// query
							final TermQuery objectQuery = new TermQuery(
									new Term(columnName, o.toString()));
							setQueries.add(objectQuery.toString());
							setColumnsIn.add(columnName);
							setFlags.add(BooleanClause.Occur.SHOULD);
						}

						// require that one of these results be found in the
						// full query
						if (setQueries.size() > 0) {
							fullQuery.add(getQuery(setQueries, setColumnsIn,
									setFlags), BooleanClause.Occur.MUST);
						}
					}
				}
			}
		}

		// Run the query and get the actual results
		if (queries.size() > 0) {
			fullQuery.add(getQuery(queries, columnsIn, flags),
					BooleanClause.Occur.MUST);
		}

		return fullQuery;
	}

	// currently just at highest level, can incorporate
	// sample/subsample/chemical analysis info by adding a parameter
	public static Query getPrivacyQuery(int typeOfDataDesired,
			User userSearching) {
		TermQuery fieldForPrivacy;
		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();

		if (userSearching != null
				&& (typeOfDataDesired == 0 || typeOfDataDesired == 2)) // private
		// data
		{
			BooleanQuery privateQuery = new BooleanQuery();

			// Private Samples
			TermQuery termQuery = new TermQuery(new Term("publicData",
					Boolean.FALSE.toString()));
			privateQuery.add(termQuery, BooleanClause.Occur.MUST);

			// Data owned by the user
			fieldForPrivacy = new TermQuery(new Term("user_id", (new Integer(
					userSearching.getId())).toString()));
			privateQuery.add(fieldForPrivacy, BooleanClause.Occur.MUST);

			if (typeOfDataDesired == 2) // if this is all we want
			{
				return privateQuery;
			} else // if we want public data too
			{
				fullQuery.add(privateQuery, BooleanClause.Occur.SHOULD);
			}
		}
		if (typeOfDataDesired == 0 || typeOfDataDesired == 1) // public data
		{
			// find public samples
			TermQuery termQuery = new TermQuery(new Term("publicData",
					Boolean.TRUE.toString()));

			if (typeOfDataDesired == 1) // just want public data
			{
				return termQuery;
			} else // want all data
			{
				fullQuery.add(termQuery, BooleanClause.Occur.SHOULD);
			}

		}

		return fullQuery;
	}

	public static Query getQuery(List<String> queries, List<String> fields,
			List<BooleanClause.Occur> flags) {
		String queryArray[] = new String[queries.size()];
		queries.toArray(queryArray);
		String columnsArray[] = new String[fields.size()];
		fields.toArray(columnsArray);
		BooleanClause.Occur flagsArray[] = new BooleanClause.Occur[flags.size()];
		flags.toArray(flagsArray);

		try {
			Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(queryArray, columnsArray, flagsArray,
							new StandardAnalyzer());
			return query;
		} catch (ParseException e) {
			return null;
		}
	}
	public static FilteredQuery addFiltersToQuery(Query query,
			List<Filter> filters) {
		if (filters.size() <= 0) {
			return new FilteredQuery(query, null);
		}
		boolean firstPass = true;
		FilteredQuery filteredQuery = new FilteredQuery(query, null);;
		for (Filter filter : filters) {
			if (firstPass) {
				filteredQuery = new FilteredQuery(query, null);
			} else {
				filteredQuery = new FilteredQuery(filteredQuery, null);
			}
		}
		return filteredQuery;
	}

	public static boolean checkForChemicalProperties(SearchSample searchSamp) {
		Object methodResult = null;
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {

			// return type of the method
			methodResult = i.get(searchSamp);
			
			if(i.isChemicalAnalysisAttr() && !i.isSampleAttr())
			{
				if(methodResult instanceof Set)
				{
					if(((Set)methodResult).size() > 0){
						return true;	
					}
				}
				else
				{
					if(methodResult != null)
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean checkForSampleProperties(SearchSample searchSamp) {
		Object methodResult = null;
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {

			// return type of the method
			methodResult = i.get(searchSamp);
			
			if(!i.isChemicalAnalysisAttr() && i.isSampleAttr())
			{
				if(methodResult instanceof Set)
				{
					if(((Set)methodResult).size() > 0){
						return true;	
					}
				}
				else
				{
					if(methodResult != null)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}

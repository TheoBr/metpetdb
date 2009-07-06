package edu.rpi.metpetdb.server.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CallbackException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.Mineral;
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
	private static String RETURN_CHEMICAL_ANALYSIS = "";
	private static String RETURN_CHEMICAL_ANALYSIS_SUBSAMPLE_ID = "select ca.subsampleId";
	private static String RETURN_CHEMICAL_ANALYSIS_COUNT = "select count(*)";
	private static String RETURN_CHEMICAL_ANALYSIS_ID = "select ca.id";
	
	public SearchDb() {
	}

	public static Results<Sample> sampleSearch(final PaginationParameters p,
			SearchSample searchSamp, User userSearching, Session session) throws MpDbException {

		// Either do chemical analysis -> subsample -> Sample search if they
		// have chem anal restrictions
		// Or just Sample search if no chem anal restrictions

		final User u = userSearching;
		final int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();
		DataStore.enableSecurityFilters(session, userId);
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		boolean haveChemicalProperties = checkForChemicalProperties(searchSamp);
		Query fullQuery;

		if (!haveChemicalProperties) {
			fullQuery = getSamplesQuery(searchSamp, userSearching, session);
		} else {
			// Get chemical analyses first
			org.hibernate.Query chemQuery = getChemicalsQuery(searchSamp, userSearching,
					session, RETURN_CHEMICAL_ANALYSIS_SUBSAMPLE_ID);

			// Get the subsample ids that correspond to the chemical analyses.
			System.out.println("Search Query Project Subsample Ids:" + chemQuery.getQueryString());

			// no chemical analysis fit search, no reason to try finding samples with "no matches"
			String  subsampleIds = getLongIdStringForList(chemQuery.list());
			if (subsampleIds == null) {
				return new Results<Sample>(0, new ArrayList<Sample>());
			}

			// Get samples third... will need to incorporate top two pieces
			fullQuery = getSamplesQuery(searchSamp, userSearching, session);
			FullTextQuery sampleQuery = fullTextSession.createFullTextQuery(fullQuery, Sample.class);
			System.out.println("Search Query Project Sample Ids:" + fullQuery.toString());
			String  sampleIds = getLongIdString(sampleQuery.setProjection("id").list());
			if (sampleIds == null) {
				return new Results<Sample>(0, new ArrayList<Sample>());
			}

			org.hibernate.Query hql = session.createQuery("from Sample s where s.id in (" + sampleIds +
					") and exists (select 1 from Subsample ss where ss.sampleId = s.id and ss.id in (" + 
					subsampleIds + "))");
			org.hibernate.Query sizeQuery = session.createQuery("select count(*) " + hql.getQueryString());
			System.out.println("Search Query Find Samples by Id and SubsampleId:" + hql.getQueryString());
			hql = setPagination(p,hql,session,true);
			try {
				//this is here in order to convert java.util.Collection$EmptyList into
				//the correct representation so it can be serialized by GWT
				List<Sample> list = hql.list();
				if (list.size() == 0)
					list = new ArrayList<Sample>();
				final Results<Sample> results = new Results<Sample>(((Long)sizeQuery.uniqueResult()).intValue(), list);
				return results;
			} catch (CallbackException e) {
				throw ConvertSecurityException.convertToException(e);
			}
		}

		System.out.println("Search Query:" + fullQuery.toString());

		try {			
			FullTextQuery sizeHibQuery = fullTextSession.createFullTextQuery(
					fullQuery, Sample.class);
			String  sampleIds = getLongIdString(sizeHibQuery.setProjection("id").list());
			if (sampleIds == null) {
				return new Results<Sample>(0, new ArrayList<Sample>());
			}
			org.hibernate.Query resultQuery = session.createQuery("from Sample s where s.id in (" + sampleIds + ")");
			org.hibernate.Query sizeQuery = session.createQuery("select count(*) " + resultQuery.getQueryString());
			resultQuery = setPagination(p,resultQuery,session,true);
			List<Sample> list = resultQuery.list();
			if (list.size() == 0)
				list = new ArrayList<Sample>();
			final Results<Sample> results = new Results<Sample>(((Long)sizeQuery.uniqueResult()).intValue(),list);
			return results;
		} catch (CallbackException e) {
			throw ConvertSecurityException.convertToException(e);
		}
	}

	public static Results<ChemicalAnalysis> chemicalAnalysisSearch(
			final PaginationParameters p, SearchSample searchSamp,
			User userSearching) throws MpDbException {
		final User u = userSearching;
		final int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();
		Session session = DataStore.open();
		DataStore.enableSecurityFilters(session, userId);
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		boolean haveSampleProperties = checkForSampleProperties(searchSamp);
		

		if (!haveSampleProperties) {
			org.hibernate.Query fullQuery = getChemicalsQuery(searchSamp, userSearching, session, RETURN_CHEMICAL_ANALYSIS);
			org.hibernate.Query sizeQuery = getChemicalsQuery(searchSamp, userSearching, session, RETURN_CHEMICAL_ANALYSIS_COUNT);
			System.out.println("Search Query:" + fullQuery.toString());			

			fullQuery = setPagination(p,fullQuery,session,false);
			try {
				//this is here in order to convert java.util.Collection$EmptyList into
				//the correct representation so it can be serialized by GWT
				List<ChemicalAnalysis> list = fullQuery.list();
				if (list.size() == 0)
					list = new ArrayList<ChemicalAnalysis>();
				final Results<ChemicalAnalysis> results = new Results<ChemicalAnalysis>(((Long)sizeQuery.uniqueResult()).intValue(), list);
				return results;
			} catch (CallbackException e) {
				throw ConvertSecurityException.convertToException(e);
			} 
		} else {
			Query fullQuery;
			// Get samples first
			Query tempSampleQuery = getSamplesQuery(searchSamp, userSearching,
					session,true);
			// // Get the subsample ids that correspond to the samples.
			final FullTextQuery sampleQuery = fullTextSession
					.createFullTextQuery(tempSampleQuery, Subsample.class);
			System.out.println("Search Query Project Subsample Ids:" + sampleQuery.toString());	
			String  subsampleIds = getLongIdString(sampleQuery.setProjection("id").list());
			if (subsampleIds == null) {
				return new Results<ChemicalAnalysis>(0, new ArrayList<ChemicalAnalysis>());
			}
			// Get chemical analyses third... will need to incorporate top two pieces
			org.hibernate.Query chemQuery = getChemicalsQuery(searchSamp, userSearching, session, RETURN_CHEMICAL_ANALYSIS_ID);
			System.out.println("Search Query Project Chemical Analysis Ids:" + chemQuery.getQueryString());	

			String  chemIds = getIntIdStringForList(chemQuery.list());
			if (chemIds == null) {
				return new Results<ChemicalAnalysis>(0, new ArrayList<ChemicalAnalysis>());
			}
			org.hibernate.Query hql = session.createQuery("from ChemicalAnalysis ca where ca.id in (" + chemIds +
					") and ca.subsampleId in (" + 
					subsampleIds + ")");
			org.hibernate.Query sizeQuery = session.createQuery("select count(*) " + hql.getQueryString());
			System.out.println("Search Query Find Chemical Analyses by Id and SubsampleId:" + hql.getQueryString());

			hql = setPagination(p,hql,session,false);
			try {
				//this is here in order to convert java.util.Collection$EmptyList into
				//the correct representation so it can be serialized by GWT
				List<ChemicalAnalysis> list = hql.list();
				if (list.size() == 0)
					list = new ArrayList<ChemicalAnalysis>();
				final Results<ChemicalAnalysis> results = new Results<ChemicalAnalysis>(((Long)sizeQuery.uniqueResult()).intValue(), list);
				return results;
			} catch (CallbackException e) {
				throw ConvertSecurityException.convertToException(e);
			} 
		}
	}

	public static Query getSamplesQuery(SearchSample searchSamp, User userSearching, Session session){
		return getSamplesQuery(searchSamp,userSearching,session,false);
	}

	public static Query getSamplesQuery(SearchSample searchSamp,
			User userSearching, Session session, Boolean onSubsample) {
		List<String> queries = new LinkedList<String>();
		List<String> columnsIn = new LinkedList<String>();
		List<BooleanClause.Occur> flags = new LinkedList<BooleanClause.Occur>();

		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();

		String columnName;
		Object methodResult = null;
		Boolean andTokenization = null;
		final String SAMPLE_PREFIX = "sample_";
		final String LOCATION_COLUMN = (onSubsample) ? SAMPLE_PREFIX + "location" : "location";
		final String BOUNDINGBOX_COLUMN =  "boundingBox";
		final String POLYGON_COLUMN = "polygon";
		final String PUBLICDATA_COLUMN = (onSubsample) ? SAMPLE_PREFIX + "publicData" : "publicData";

		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		for (SearchProperty i : enums) {

			// column to search on

			columnName = (!onSubsample || i.columnName() == "") ? i.columnName() : "sample_" + i.columnName();

			// return type of the method
			methodResult = i.get(searchSamp);
			
			// should we and the tokenized field values?
			andTokenization = i.isTokenizationAnded();

			// if there is no value returned in this field...
			if (methodResult == null) {
				// ignore it
			} else { // otherwise, what type of returned data is it?
				if (i.isSampleAttr()) {
					if (columnName.equals(LOCATION_COLUMN)) { // if the column
						// being searched on
						// is location
						if (searchSamp.getBoundingBox() != null) { // if there
							// is a
							// bounding box
							// for this
							// sample
							session.enableFilter(BOUNDINGBOX_COLUMN).setParameter(
									POLYGON_COLUMN, searchSamp.getBoundingBox()); // filter
							// results
							// based
							// on
							// the
							// box
						}
					} else if (columnName.equals(PUBLICDATA_COLUMN)) {
						// incorporate privacy stuff
						fullQuery.add(getPrivacyQuery(Integer
								.parseInt(methodResult.toString()),
								userSearching), BooleanClause.Occur.MUST);
					} else if (methodResult instanceof Set) { // if a set of
						// data is
						// returned, it should be an
						// OR query
						if (((Set) methodResult).size() > 0) {
								List<String> setQueries = new LinkedList<String>();
								List<String> setColumnsIn = new LinkedList<String>();
								List<BooleanClause.Occur> setFlags = new LinkedList<BooleanClause.Occur>();

								for (Object o : (Set) methodResult) {
									// iterate through each item and add it to the
									// query
									final TermQuery objectQuery = new TermQuery(
											new Term(columnName, o.toString()));
									setQueries.add(objectQuery.toString());
									setColumnsIn.add(columnName);
									setFlags.add(BooleanClause.Occur.SHOULD);
								}	
								if (setQueries.size() > 0) {
									fullQuery.add(getQuery(setQueries,
											setColumnsIn, setFlags, andTokenization),
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

	public static org.hibernate.Query getChemicalsQuery(SearchSample searchSamp,
			User userSearching, Session session, String selectQuery) {
		Set<String> queries = new HashSet<String>();

		String columnName;
		Object methodResult = null;
		SearchProperty[] enums = SearchSampleProperty.class.getEnumConstants();
		String query = "";
		String fromQuery = "from ChemicalAnalysis ca";
		for (SearchProperty i : enums) {

			// column to search on
			columnName = i.columnName();

			// return type of the method
			methodResult = i.get(searchSamp);

			if (i.isChemicalAnalysisAttr()) {
				// if there is no value returned in this field...
				if (columnName.equals("oxides")) {
					if (((Set) methodResult).size() > 0) {
						fromQuery += " join ca.oxides o";
						String oxideQuery = "";
						for (SearchOxide o : (Set<SearchOxide>) methodResult) {
							String q = "(o.minAmount <= " + o.getUpperBound() + 
							" and o.maxAmount >= " + o.getLowerBound() + " and lower(o.oxide.species) = lower('" + o.getSpecies() + "')";
							if (o.getMinerals().size() > 0){
								q+= " and (lower(ca.mineral.name) in (" ;
								for (Mineral m : o.getMinerals()){
									q+= "lower('" +m.getName() +"'),";
								}
								q = q.substring(0,q.length()-1);
								q+= "))";
							} else if (o.getWholeRock()){
								q+= " and ca.largeRock = 'Y'";
							}
							q+= ")";
							oxideQuery += q + " or ";
						}	
						queries.add("(" + oxideQuery.substring(0,oxideQuery.length()-4) + ")");
						
					}
				} else if (columnName.equals("elements")) {
					if (((Set) methodResult).size() > 0) {
						fromQuery += " join ca.elements e";
						String elementQuery = "";
						for (SearchElement o : (Set<SearchElement>) methodResult) {
							String q = "(e.minAmount <= " + o.getUpperBound().toString() + 
							" and e.maxAmount >= " + o.getLowerBound() + " and lower(e.element.symbol) = lower('" + o.getElementSymbol() + "')";
							if (o.getMinerals().size() > 0){
								q+= " and (lower(ca.mineral.name) in (" ;
								for (Mineral m : o.getMinerals()){
									q+="lower('" + m.getName() +"'),";
								}
								q = q.substring(0,q.length()-1);
								q+= "))";
							} else if (o.getWholeRock()){
								q+= " and ca.largeRock = 'Y'";
							}
							q+= ")";	
							elementQuery += q + " or ";
						}
						queries.add("(" + elementQuery.substring(0, elementQuery.length()-4) + ")");
					}
				} else if (columnName.equals("publicData")) {
					// incorporate privacy stuff
					queries.add(getAnalysisPrivacyHQLQuery((Integer)methodResult,userSearching));
				} 
			}
		}

		// Run the query and get the actual results
		if (queries.size() > 0) {
			query = selectQuery + " " + fromQuery + " where ";
			for (String q : queries){
				query += q + " and ";
			}
			query = query.substring(0,query.length()-5);
		}
		
		return session.createQuery(query);
	}
	
	
	public static String getAnalysisPrivacyHQLQuery(int typeOfDataDesired, User userSearching){
		String q = "";
		if (userSearching != null
				&& (typeOfDataDesired == 0 || typeOfDataDesired == 2)) // private data
		{
			q+="(ca.publicData = 'N' and ca.owner.id = " + userSearching.getId() + ")";
		}
		if (typeOfDataDesired == 0 || typeOfDataDesired == 1) // public data
		{
			// find public samples
			if (!q.equalsIgnoreCase("")){
				q = "(" + q;
				q+=" or (ca.publicData = 'Y'))";
			} else
				q+="(ca.publicData = 'Y')";
		}
		return q;
	}

	public static Query getPrivacyQuery(int typeOfDataDesired, User userSearching){
		return getPrivacyQuery(typeOfDataDesired, userSearching,"");
	}
	// currently just at highest level, can incorporate
	// sample/subsample/chemical analysis info by adding a parameter
	public static Query getPrivacyQuery(int typeOfDataDesired,
			User userSearching, String prefix) {
		TermQuery fieldForPrivacy;
		// The full scope of the query we want to make
		BooleanQuery fullQuery = new BooleanQuery();

		if (userSearching != null
				&& (typeOfDataDesired == 0 || typeOfDataDesired == 2)) // private
			// data
		{
			BooleanQuery privateQuery = new BooleanQuery();

			// Private Samples
			TermQuery termQuery = new TermQuery(new Term(prefix + "publicData",
					Boolean.FALSE.toString()));
			privateQuery.add(termQuery, BooleanClause.Occur.MUST);

			// Data owned by the user
			fieldForPrivacy = new TermQuery(new Term(prefix + "user_id", (new Integer(
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
			TermQuery termQuery = new TermQuery(new Term(prefix + "publicData",
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
		return getQuery(queries,fields,flags,false);
	}

	public static Query getQuery(List<String> queries, List<String> fields,
			List<BooleanClause.Occur> flags, boolean andedTokenization) {
		String queryArray[] = new String[queries.size()];
		queries.toArray(queryArray);
		String columnsArray[] = new String[fields.size()];
		fields.toArray(columnsArray);
		BooleanClause.Occur flagsArray[] = new BooleanClause.Occur[flags.size()];
		flags.toArray(flagsArray);
		try {
			if (andedTokenization){
				BooleanQuery query = new BooleanQuery();
				for (int i = 0; i < queryArray.length; i++){
					BooleanQuery tokenizedQuery = new BooleanQuery();
					String searchTerm = queryArray[i].split(":",2)[1];
					for (String s : searchTerm.split(" ")){
						TermQuery term = new TermQuery(new Term(columnsArray[i],s.toLowerCase()));
						tokenizedQuery.add(term,BooleanClause.Occur.MUST);
					}	
					query.add(tokenizedQuery, flagsArray[i]);
				} 
				return query;
			} else {
				
					Query query = org.apache.lucene.queryParser.MultiFieldQueryParser
					.parse(queryArray, columnsArray, flagsArray, new StandardAnalyzer());
					return query;
			}
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
	
	private static org.hibernate.Query setPagination(PaginationParameters p, org.hibernate.Query q, Session session, Boolean isSample){
		if (p != null){
			if (!p.getParameter().equals("")){
				String queryString = q.getQueryString();
				String parameter = p.getParameter();
				if (parameter.equals("owner"))
					parameter += ".name";
				else if (parameter.equals("reference"))
					parameter = "referenceName";
				else if (parameter.equals("rockType"))
					parameter += ".rockType";
				else if (parameter.equals("regions"))
					parameter = "firstRegion";
				else if (parameter.equals("references"))
					parameter = "firstReference";
				else if (parameter.equals("metamorphicGrades"))
					parameter = "firstMetamorphicGrade";
				else if (parameter.equals("minerals"))
					parameter = "firstMineral";
				else if (parameter.equals("mineral")){
					parameter = "analysisMaterial";
				}
				queryString += " order by " + ((isSample) ? "s." : "ca.") + parameter;
				queryString += (p.isAscending()) ? "" : " DESC";
				org.hibernate.Query q2 = session.createQuery(queryString);
				q2.setFirstResult(p.getFirstResult());
				q2.setMaxResults(p.getMaxResults());
				return q2;
			}
			q.setFirstResult(p.getFirstResult());
			q.setMaxResults(p.getMaxResults());
		} else {
			q.setFirstResult(0);
			q.setMaxResults(100);
		}
		return q;
	}
	
	private static FullTextQuery setPagination(PaginationParameters p, FullTextQuery q){
		if (p != null){
			q.setFirstResult(p.getFirstResult());
			q.setMaxResults(p.getMaxResults());
//			q.setSort(new Sort(p.getParameter(),p.isAscending()));
		} else {
			q.setFirstResult(0);
			q.setMaxResults(100);
		}
		return q;
	}
	
	private static String getLongIdStringForList(final List<Long> projectedIds){
		if (projectedIds.size() == 0) {
			return null;
		}
		String  ids = "";
		for (Long o : projectedIds){
			ids += o.toString() + ",";
		}
		if (ids != ""){
			ids = ids.substring(0, ids.length()-1);
		}
		return ids;
	}
	
	private static String getIntIdStringForList(final List<Integer> projectedIds){
		if (projectedIds.size() == 0) {
			return null;
		}
		String  ids = "";
		for (Integer o : projectedIds){
			ids += o.toString() + ",";
		}
		if (ids != ""){
			ids = ids.substring(0, ids.length()-1);
		}
		return ids;
	}
	
	private static String getLongIdString(final List<Object[]> projectedIds){
		if (projectedIds.size() == 0) {
			return null;
		}
		String  ids = "";
		for (Object[] o : projectedIds){
			for (Object id : o){
				ids += ((Long)id).toString() + ",";
			}
		}
		if (ids != ""){
			ids = ids.substring(0, ids.length()-1);
		}
		return ids;
	}
}

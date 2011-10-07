package edu.rpi.metpetdb.server.search;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.TermQuery;
import org.hibernate.CallbackException;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SearchProperty;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.server.DataStore;
import edu.rpi.metpetdb.server.security.ConvertSecurityException;

public class iPhoneSearchDb {
	
	public iPhoneSearchDb() {
	}

	
	public static Results<Sample> sampleSearch(final PaginationParameters p,
			SearchSample searchSamp, User userSearching, Session session, String latitude, String longitude) throws MpDbException {

		// Either do chemical analysis -> subsample -> Sample search if they
		// have chem anal restrictions
		// Or just Sample search if no chem anal restrictions
		System.out.println(session.toString());
		final User u = userSearching;
		 int userId;
		if (u == null)
			userId = 0;
		else
			userId = u.getId();
		
		DataStore.enableSecurityFilters(session, userId);
		FullTextSession fullTextSession = Search.getFullTextSession(session);

		boolean haveChemicalProperties = checkForChemicalProperties(searchSamp);
		
		System.out.println("hasChemicalProperties" + haveChemicalProperties);
		
		Query fullQuery;

			fullQuery = getSamplesQuery(searchSamp, userSearching, session);

		System.out.println("Search Query:" + fullQuery.toString());

		try {			
			FullTextQuery sizeHibQuery = fullTextSession.createFullTextQuery(
					fullQuery, Sample.class);
			System.out.println("Full Search Query" + sizeHibQuery.getQueryString());
			
			String  sampleIds = getLongIdString(sizeHibQuery.setProjection("id").list());
			System.out.println("sampleIds" + sampleIds);
			if (sampleIds == null) {
				return new Results<Sample>(0, new ArrayList<Sample>());
			}
			
			org.hibernate.Query resultQuery = session.createQuery("from Sample s where s.id in (" + sampleIds + ")");
			org.hibernate.Query sizeQuery = session.createQuery("select count(*) " + resultQuery.getQueryString());
		
			
			System.out.println("Search resultQuery:" + resultQuery.getQueryString());
			System.out.println("Search sizeQuery:" + sizeQuery.getQueryString());
			resultQuery = setPagination(latitude, longitude, p, resultQuery,session,true);
			List<Sample> list = resultQuery.list();
			System.out.println("Search query result list:" + list.toString());
			if (list.size() == 0)
				list = new ArrayList<Sample>();
			final Results<Sample> results = new Results<Sample>(((Long)sizeQuery.uniqueResult()).intValue(),list);
			return results;
		} catch (CallbackException e) {
			throw ConvertSecurityException.convertToException(e);
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
			System.out.println("column name: " + i.columnName());
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
								userSearching,onSubsample ? SAMPLE_PREFIX : ""), BooleanClause.Occur.MUST);
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

	//	fullQuery.
		return fullQuery;
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
				BooleanQuery query = new BooleanQuery();
				for (int i = 0; i < queryArray.length; i++){ 
					BooleanQuery subquery = new BooleanQuery();
					String searchTerm = queryArray[i].split(":",2)[1];
					Reader reader = new StringReader(searchTerm);
					Analyzer analyzer = new StandardAnalyzer();
					TokenStream stream = analyzer.tokenStream(columnsArray[i], reader);
					Token token = new Token();
					token = stream.next(token);
					while (token != null){
						if (token.termLength() != 0){
							String term = new String(token.termBuffer(),0,token.termLength());
							if (andedTokenization) 
								subquery.add(new TermQuery(new Term(columnsArray[i],term)),BooleanClause.Occur.MUST);
							else
								subquery.add(new TermQuery(new Term(columnsArray[i],term)),BooleanClause.Occur.SHOULD);
						}
						token = stream.next(token);
					}
					query.add(subquery,BooleanClause.Occur.SHOULD);
				} 
				return query;

		} catch (Exception e) {
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
						if (methodResult instanceof Boolean){
							if ((Boolean) methodResult != false) {
								return true;
							}
						} else {
							return true;
						}
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
	
	private static org.hibernate.Query setPagination(String latitude, String longitude, PaginationParameters p, org.hibernate.Query q, Session session, Boolean isSample){
		if (p != null){
			if (p.getParameter() != null && !p.getParameter().equals("")){
				String queryString = q.getQueryString();
				
				//So they grab the parameter first...and add the column name to it
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
				String realParameter = null;
				
				// This is fantastically broken...
				if (!parameter.equals("distance"))
						realParameter = ((isSample) ? "s." : "ca.") + parameter;
			//	else
					realParameter = " dist('" + latitude + "', '" + longitude + "', s.location)";
				
				String lowerParam = parameter.toLowerCase();
				if (!(lowerParam.contains("count") || lowerParam.contains("date") || lowerParam.contains("spotid") ||
						lowerParam.equalsIgnoreCase("referenceX") || lowerParam.equalsIgnoreCase("referenceY") || lowerParam.equalsIgnoreCase("distance"))){
					realParameter = "lower(" +realParameter +")";
				}
				queryString += " order by "  + realParameter;
				queryString += (p.isAscending()) ? "" : " DESC";
			org.hibernate.Query q2 = session.createQuery(queryString);
			
			System.out.println("RESULTS" + queryString);
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
	
	/**
	Test driver method
 	**/
	public static void main(String args[])
	{
		Configuration cfg = new Configuration();
		
		cfg.configure(new File("/Users/scball/metpetweb4/mpdb-server/src/edu/rpi/metpetdb/server/dao/hibernate.cfg.xml"));
		
		
		SessionFactory sf = cfg.buildSessionFactory();
		
		
		Session s =sf.openSession();
		
		
		User user = new User();
		user.setId(6);
		user.setEmailAddress("foo@bar.com");
		
		
		PaginationParameters pp = new PaginationParameters();
		pp.setFirstResult(0);
		pp.setMaxResults(100);
		pp.setParameter("distance");
		SearchSample searchSample = new SearchSample();
		//searchSample.addReferences("1966-012531");
		
		try {
			iPhoneSearchDb.sampleSearch(pp, searchSample, user, s, "36", "-105");
		} catch (MpDbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}

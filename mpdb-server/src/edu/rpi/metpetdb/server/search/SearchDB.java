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
import edu.rpi.metpetdb.server.model.Sample;

public class SearchDB{
	public SearchDB() 
	{		
	}

	public List<Sample> SampleSearch(List<AttributeDTO> AttributePairs) {
		final Session session = InitDatabase.getSession();
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		List<Sample> result;
		Transaction tx = fullTextSession.beginTransaction();
				
		System.out.println("in test join");
		ArrayList<String> searchFor = new ArrayList<String>();
		ArrayList<String> columnsIn = new ArrayList<String>();
		ArrayList<BooleanClause.Occur> flags = new ArrayList<BooleanClause.Occur>();
		for(AttributeDTO attribute : AttributePairs)
		{
			searchFor.add(attribute.getValue());
			columnsIn.add(attribute.getAttribute());
			flags.add(BooleanClause.Occur.MUST);
		}
		
		/*  BooleanClause.Occur[] flags = {BooleanClause.Occur.SHOULD,
        BooleanClause.Occur.MUST,
        BooleanClause.Occur.MUST_NOT};*/
		try
		{
			Query query =  org.apache.lucene.queryParser.MultiFieldQueryParser.parse((String[])(searchFor.toArray()), (String[])(columnsIn.toArray()), (BooleanClause.Occur[])(flags.toArray()), new StandardAnalyzer());
			org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery( query, Sample.class );
			result = hibQuery.list();
		}
		catch(ParseException e)
		{}
  
		tx.commit();
		return result;
	}	
}

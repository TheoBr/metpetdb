package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.SearchSample;

public interface SearchProperty extends Property<SearchSample> {

	/**
	 * The column name that is used by Lucene to query the value. For example in
	 * Sample the owner property is specified to have a prefix of "user". This
	 * means that any search for values on an owner have to be prefixed by
	 * "user". If the search was for all samples whose username matches
	 * something the column name would be "user_username".
	 * 
	 * @return
	 */
	abstract String columnName();
	
	abstract boolean isSampleAttr();
	
	abstract boolean isChemicalAnalysisAttr();
	
	abstract boolean isTokenizationAnded();
}

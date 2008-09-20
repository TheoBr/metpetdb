package edu.rpi.metpetdb.client.paging;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Parameters that are passed to the server to determine how data should be
 * sorted.
 * 
 * @author anthony
 * 
 */
public class PaginationParameters implements IsSerializable {

	private int firstResult;
	private int maxResults;
	private String parameter;
	private boolean isAscending;

	/**
	 * {@link #PaginationParameters(int, int, String, boolean)}
	 */
	public PaginationParameters() {

	}

	/**
	 * Creates a new pagination parameter
	 * 
	 * @param firstRow
	 * 		the number of the first row
	 * @param numRows
	 * 		the number of rows
	 * @param orderBy
	 * 		column to order by
	 * @param isAscending
	 * 		whether to sort ascending or not
	 */
	public PaginationParameters(final int firstRow, final int numRows,
			final String orderBy, final boolean isAscending) {
		this.firstResult = firstRow;
		this.maxResults = numRows;
		this.parameter = orderBy;
		this.isAscending = isAscending;
	}

	/**
	 * The first result is the index of the first row to start paging it, i.e.
	 * forms the part of the query LIMIT [first result]
	 * 
	 * @return the first result
	 */
	public int getFirstResult() {
		return firstResult;
	}

	/**
	 * Sets the first result for paging
	 * 
	 * @param firstResult
	 */
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	/**
	 * Gets the number of results that should be returned, i.e. forms the OFFSET
	 * [max results] part of the query
	 * 
	 * @return the max number of results
	 */
	public int getMaxResults() {
		return maxResults;
	}

	/**
	 * Sets the max number of results for paging
	 * 
	 * @param maxResults
	 */
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	/**
	 * Gets the parameter that is used for sorting, aka the column name
	 * according to hibernate.
	 * 
	 * @return the parameter
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * Sets the parameter that is used for sorting
	 * 
	 * @param parameter
	 */
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	/**
	 * Returns whether this column should be sorted ascending
	 * 
	 * @return true to sort ascending false to sort descending
	 */
	public boolean isAscending() {
		return isAscending;
	}

	/**
	 * Sets whether the column is assorted ascendingly or not.
	 * 
	 * @param isAscending
	 */
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

}

package edu.rpi.metpetdb.client.paging;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PaginationParameters implements IsSerializable {

	private int firstResult;
	private int maxResults;
	private String parameter;
	private boolean isAscending;

	public PaginationParameters() {

	}

	public PaginationParameters(final int firstRow, final int numRows,
			final String orderBy, final boolean isAscending) {
		this.firstResult = firstRow;
		this.maxResults = numRows;
		this.parameter = orderBy;
		this.isAscending = isAscending;
	}

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public boolean isAscending() {
		return isAscending;
	}

	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

}

package edu.rpi.metpetdb.client.ui.widgets.paging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Cookies;

import edu.rpi.metpetdb.client.paging.PaginationParameters;

public class ListCookieJar {

	private static final int DEFAULT_PAGE_SIZE = 75;

	private enum CookieData {
		PAGE_SIZE, COLUMNS, PAGINATION_PARAMETERS,
	}

	private static final String DELIMETER = ":";
	private static final String COLUMN_DELIMETER = ";";

	private final String cookieName;

	public ListCookieJar(String cookieName) {
		this.cookieName = cookieName;
	}

	/**
	 * Parses the cookie out into a map, cookies are stored like so key:value:
	 * 
	 * @return
	 */
	private Map<String, String> parseCookie() {
		final String cookie = Cookies.getCookie(cookieName) == null ? ""
				: Cookies.getCookie(cookieName);
		final String cookieData[] = cookie.split(DELIMETER);
		final Map<String, String> data = new HashMap<String, String>();
		if (cookieData.length > 0) {
			int index = 0;
			while (index < cookieData.length) {
				final String key = cookieData[index];
				final String value;
				if (index + 1 < cookieData.length)
					value = cookieData[index + 1];
				else
					value = "";
				data.put(key, value);
				index+=2;
			}
		}
		return data;
	}

	/**
	 * Returns the page size as found in the cookie, if none is found it returns
	 * the default page size
	 * 
	 * @return
	 */
	public int getPageSize() {
		final Map<String, String> cookie = parseCookie();
		if (cookie.containsKey(CookieData.PAGE_SIZE.name())) {
			final String pageSize = cookie.get(CookieData.PAGE_SIZE.name());
			try {
				final int intPageSize = Integer.parseInt(pageSize);
				return intPageSize;
			} catch (Exception e) {
				return handleNoPageSize();
			}
		} else {
			return handleNoPageSize();
		}
	}

	private int handleNoPageSize() {
		setPageSize(DEFAULT_PAGE_SIZE);
		return DEFAULT_PAGE_SIZE;
	}

	/**
	 * Sets the page size in the cookie
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		final Map<String, String> cookie = parseCookie();
		cookie.put(CookieData.PAGE_SIZE.name(), String.valueOf(pageSize));
		writeCookie(cookie);
	}

	/**
	 * Returns the columns that are stored in the cookie, if none are found it
	 * returns an empty list
	 * 
	 * @return
	 */
	public List<String> getColumns() {
		final Map<String, String> cookie = parseCookie();
		final List<String> columns = new ArrayList<String>();
		if (cookie.containsKey(CookieData.COLUMNS.name())) {
			final String columnStr = cookie.get(CookieData.COLUMNS.name());
			final String realColumns[] = columnStr.split(COLUMN_DELIMETER);
			for (String s : realColumns) {
				columns.add(s);
			}
		}
		return columns;
	}

	/**
	 * Sets the columns to be stored in the cookie
	 * 
	 * @param columns
	 */
	public void setColumns(List<String> columns) {
		final Map<String, String> cookie = parseCookie();
		final StringBuffer buf = new StringBuffer();
		for (String s : columns) {
			buf.append(s);
			buf.append(COLUMN_DELIMETER);
		}
		cookie.put(CookieData.COLUMNS.name(), buf.toString());
		writeCookie(cookie);
	}

	/**
	 * Returns the pagination parameters present in the cookie, if none are
	 * present the first result will be null
	 * 
	 * @return
	 */
	public PaginationParameters getPaginationParameters() {
		final Map<String, String> cookie = parseCookie();
		final PaginationParameters p = new PaginationParameters();
		if (cookie.containsKey(CookieData.PAGINATION_PARAMETERS.name())) {
			final String pStr = cookie.get(CookieData.PAGINATION_PARAMETERS
					.name());
			final String pStrA[] = pStr.split(COLUMN_DELIMETER);
			if (pStrA.length == 4) {
				try {
					p.setAscending(Boolean.parseBoolean(pStrA[0]));
					p.setFirstResult(Integer.parseInt(pStrA[1]));
					p.setMaxResults(Integer.parseInt(pStrA[2]));
					p.setParameter(pStrA[3]);
				} catch (Exception e) {
					// do nothing if we fail
				}
			}
			return p;
		} else {
			return null;
		}
	}

	/**
	 * Sets the pagination parameters to be stored in the cookie
	 */
	public void setPaginationParameters(final PaginationParameters p) {
		final Map<String, String> cookie = parseCookie();
		final StringBuffer pStr = new StringBuffer();
		pStr.append(p.isAscending());
		pStr.append(p.getFirstResult());
		pStr.append(p.getMaxResults());
		pStr.append(p.getParameter());
		cookie.put(CookieData.PAGINATION_PARAMETERS.name(), pStr.toString());
		writeCookie(cookie);
	}

	private void writeCookie(final Map<String, String> cookie) {
		final Iterator<Entry<String, String>> itr = cookie.entrySet()
				.iterator();
		final StringBuffer cookieData = new StringBuffer();
		while (itr.hasNext()) {
			final Entry<String, String> e = itr.next();
			cookieData.append(e.getKey());
			cookieData.append(DELIMETER);
			cookieData.append(e.getValue());
			cookieData.append(DELIMETER);
		}
		Cookies.setCookie(cookieName, cookieData.toString());
	}
}

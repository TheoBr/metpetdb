package edu.rpi.metpetdb.client.paging;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An {@link Iterator} over a 2D {@link Collection} of column data.
 */
public class PagingResponseIterator implements Iterator<Iterator<Object>> {
	/**
	 * The {@link Iterator} of row data. Each row is a {@link Collection} of
	 * column data.
	 */
	private Iterator<Collection<Object>> rows;

	/**
	 * Default constructor.
	 */
	public PagingResponseIterator() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param rows
	 *            the row data
	 */
	public PagingResponseIterator(Collection<Collection<Object>> rows) {
		if (rows != null) {
			this.rows = rows.iterator();
		}
	}

	public boolean hasNext() {
		if (rows == null) {
			return false;
		}
		return rows.hasNext();
	}

	public Iterator<Object> next() {
		if (!hasNext()) {
			throw (new NoSuchElementException());
		}
		Collection<Object> next = rows.next();
		if (next == null) {
			return null;
		} else {
			return new ObjectIterator<Object>(next.iterator());
		}
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Converts an iterator of some type into an iterator of objects.
	 * 
	 * @param <E>
	 *            the type of iterator to wraps
	 */
	private static class ObjectIterator<E> implements Iterator<Object> {
		private Iterator<E> iterator;

		public ObjectIterator(Iterator<E> iterator) {
			this.iterator = iterator;
		}

		public boolean hasNext() {
			return iterator.hasNext();
		}

		public Object next() {
			return iterator.next();
		}

		public void remove() {
			throw (new UnsupportedOperationException());
		}
	}
}

package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.rpi.metpetdb.client.model.interfaces.MObject;

/**
 * The definition of columns for a list
 * 
 * @author anthony
 * 
 */
public class ColumnDefinition<RowType> implements
		Iterable<Column<RowType, ?>> {

	private class ColumnDefinitionIterator implements
			Iterator<Column<RowType, ?>> {

		private int currentIdx = 0;

		public boolean hasNext() {
			return currentIdx < columnNames.size();
		}

		public Column<RowType, ?> next() {
			return columns.get(columnNames.get(currentIdx++));
		}

		public void remove() {
			columns.remove(columnNames.get(currentIdx));
		}

	}

	/** Sorted list of the column names (sorted by showing order) */
	private List<String> columnNames;

	/** Mapping of a column name to the actual column */
	private Map<String, Column<RowType, ?>> columns;

	public ColumnDefinition() {
		columnNames = new ArrayList<String>();
		columns = new HashMap<String, Column<RowType, ?>>();
	}

	/**
	 * By default the column is the property name associated with the column,
	 * and the order is last
	 * 
	 * @see #addColumn(String, Column, int)
	 * @param column
	 */
	public void addColumn(Column<RowType, ?> column) {
		if (column != null) {
			final String columnId;
			if (column.property != null)
				columnId = column.property.name();
			else if (column.getName() != null) 
				columnId = column.getName();
			else if (column.getHeader() instanceof String)
				columnId = column.getHeader().toString();
			else
				columnId = "";
			addColumn(columnId, column, columnNames.size());
		}
	}

	/**
	 * Adds a column to the column definition.
	 * 
	 * @param name
	 *            unique name of the column (unique to the list it is on)
	 * @param column
	 *            column to add
	 * @param order
	 *            order to add it in
	 */
	public void addColumn(String name, Column<RowType, ?> column, int order) {
		if (order > columnNames.size())
			columnNames.add(name);
		else
			columnNames.add(order, name);

		columns.put(name, column);
	}

	public Column<RowType, ?> getColumn(int index) {
		return columns.get(columnNames.get(index));
	}

	public Column<RowType, ?> getColumn(final String name) {
		return columns.get(name);
	}

	public String getColumnName(int index) {
		return columnNames.get(index);
	}

	public Iterator<Column<RowType, ?>> iterator() {
		return new ColumnDefinitionIterator();
	}

	public int size() {
		return columnNames.size();
	}

	public boolean contains(final String name) {
		return columnNames.contains(name);
	}

}

package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.PagingOptions;
import com.google.gwt.widgetideas.table.client.PagingScrollTable;
import com.google.gwt.widgetideas.table.client.ReadOnlyTableModel;
import com.google.gwt.widgetideas.table.client.TableModel.Response;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.PagingResponseIterator;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.ServerOp;

public abstract class ListEx<T extends MObjectDTO> extends FlowPanel {

	private static final boolean DEFAULT_SORT_ORDER = true;

	private Column[] columns;

	protected class PagingResponse extends Response<T> {

		/**
		 * The 2D {@link Collection} of serializable cell data.
		 */
		private Collection<Collection<Object>> rows;

		/**
		 * Default constructor.
		 */
		public PagingResponse() {
			this(null);
		}

		/**
		 * Constructor.
		 * 
		 * @param rows
		 *            the row data
		 */
		public PagingResponse(Collection<Collection<Object>> rows) {
			this(rows, null);
		}

		/**
		 * Constructor.
		 * 
		 * @param rows
		 *            the row data
		 * @param rowValues
		 *            the values to associate with each row
		 */
		public PagingResponse(Collection<Collection<Object>> rows,
				List<T> rowValues) {
			super(rowValues);
			this.rows = rows;
		}

		@Override
		public Iterator<Iterator<Object>> getIterator() {
			if (rows == null) {
				return null;
			}
			return new PagingResponseIterator(rows);
		}

	}

	protected class TableModel extends ReadOnlyTableModel<T> {
		public void requestRows(final Request request,
				final Callback<T> callback) {
			final int startRow = request.getStartRow();
			final int numRows = request.getNumRows();

			final ColumnSortList sortList = request.getColumnSortList();
			// Get column to sort
			final ColumnSortInfo sortInfo = sortList.getPrimaryColumnSortInfo();
			final PaginationParameters p = new PaginationParameters();
			if (sortInfo != null) {
				p.setParameter(columns[sortInfo.getColumn()].getProperty()
						.name());
				p.setAscending(sortInfo.isAscending());
			} else {
				p.setAscending(DEFAULT_SORT_ORDER);
				p.setParameter(getDefaultSortParameter());
			}
			p.setFirstResult(startRow);
			p.setMaxResults(numRows);

			new ServerOp<Results<T>>() {
				@Override
				public void begin() {
					update(p, this);
				}

				public void onSuccess(final Results<T> result) {
					TableModel.this.setRowCount(result.getCount());
					final PagingResponse response = new PagingResponse(
							getList(result.getList()), result.getList());
					if (result.getCount() == 0) {
						ListEx.this.clear();
						ListEx.this
								.add(new HTML(
										"nothing to see here...move along<br/><br/><br/><br/><br/>Seriously though, i did not find any samples that match your criteria"));
					} else {
						callback.onRowsReady(request, response);
					}
				}

			}.begin();
		}
	}

	public abstract String getDefaultSortParameter();

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<T>> ac);

	public Collection<Collection<Object>> getList(final List<T> data) {
		final Collection<Collection<Object>> rows = new HashSet<Collection<Object>>();
		final Iterator<T> itr = data.iterator();
		int currentRow = 0;
		while (itr.hasNext()) {
			rows.add(processRow(itr.next(), currentRow++));
		}
		return rows;
	}

	public Collection<Object> processRow(final MObjectDTO object,
			final int currentRow) {
		final Collection<Object> data = new ArrayList<Object>();
		for (int i = 0; i < columns.length; ++i) {
			if (columns[i].isCustomFormat()) {
				data.add(columns[i].getRepresentation(object, currentRow));
			} else if (columns[i].getProperty() != null) {
				data.add(columns[i].getProperty().get(object));
			} else
				data.add(columns[i].getTitle());
		}
		return data;
	}

	public ListEx(final Column[] columns) {
		this.columns = columns;
		TableModel tableModel = new TableModel();
		final FixedWidthGrid dataTable = new FixedWidthGrid();
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();
		final PagingScrollTable<T> scrollTable = new PagingScrollTable<T>(
				tableModel, dataTable, headerTable);
		PagingOptions options = new PagingOptions(scrollTable);

		scrollTable.setPageSize(2);
		tableModel.setRowCount(1);

		for (int i = 0; i < columns.length; ++i) {
			headerTable.setText(0, i, columns[i].getTitle());
		}

		// Setup sortable/unsortable columns
		for (int i = 0; i < columns.length; ++i) {
			scrollTable.setColumnSortable(i, columns[i].isSortable());
		}

		dataTable.addTableListener(new TableListener() {

			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				columns[cell].handleClickEvent((MObjectDTO) scrollTable
						.getRowValue(row - 1), row - 1);

			}

		});

		scrollTable.setHeight("100%");
		scrollTable.setWidth("100%");
		dataTable.setWidth("100%");
		dataTable.setHeight("100%");
		headerTable.setWidth("100%");
		add(scrollTable);
		add(options);
		this.setHeight("400px");
		this.setWidth("100%");
	}
}

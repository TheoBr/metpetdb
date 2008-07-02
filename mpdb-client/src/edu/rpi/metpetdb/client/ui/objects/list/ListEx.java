package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

/**
 * An abstract class that pieces together the different parts for creating a
 * paginated table.
 * 
 * @author anthony
 * 
 * @param <T>
 *            the types of objects that will be paginated in this table
 */
public abstract class ListEx<T extends MObjectDTO> extends FlowPanel {

	/**
	 * How we sort by default (true means ascending, false is descending)
	 */
	private static final boolean DEFAULT_SORT_ORDER = true;
	private PagingScrollTable<T> scrollTable;

	public PagingScrollTable<T> getScrollTable() {
		return scrollTable;
	}

	/**
	 * The instance of columns that are layed out on the table
	 */
	private ArrayList<Column> originalColumns;
	private ArrayList<Column> displayColumns;
	private TableModel tableModel;
	private FixedWidthGrid dataTable;
	private FixedWidthFlexTable headerTable;

	/**
	 * Response to send back to the callback so that it can draw the table
	 * 
	 * @author anthony
	 * 
	 */
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

	/**
	 * The table model that is used to draw the table
	 * 
	 * @author anthony
	 * 
	 */
	protected class TableModel extends ReadOnlyTableModel<T> {

		/**
		 * Called automatically when new rows are requested, i.e. when the user
		 * navitages to a new page.
		 */
		public void requestRows(final Request request,
				final Callback<T> callback) {
			final int startRow = request.getStartRow();
			final int numRows = request.getNumRows();

			final ColumnSortList sortList = request.getColumnSortList();
			// Get column to sort
			final ColumnSortInfo sortInfo = sortList.getPrimaryColumnSortInfo();
			final PaginationParameters p = new PaginationParameters();
			if (sortInfo != null) {
				p.setParameter(displayColumns.get(sortInfo.getColumn())
						.getProperty().name());
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
						callback.onRowsReady(request, response);
						ListEx.this.scrollTable
								.getHeaderTable()
								.setWidget(
										1,
										0,
										new HTML(
												"<Strong>We were unable to find anything that matched your current criteria</Strong>"));
						ListEx.this.scrollTable.getHeaderTable()
								.getFlexCellFormatter().setColSpan(1, 0,
										ListEx.this.displayColumns.size());
						ListEx.this.scrollTable.getHeaderTable()
								.getFlexCellFormatter().setAlignment(1, 0,
										HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
					} else {
						if (ListEx.this.scrollTable.getHeaderTable()
								.getRowCount() > 1)
							ListEx.this.scrollTable.getHeaderTable().removeRow(
									1);
						callback.onRowsReady(request, response);
					}

					int size = ListEx.this.scrollTable.getHeaderTable()
							.getOffsetHeight()
							+ ListEx.this.scrollTable.getDataTable()
									.getOffsetHeight();

					if (ListEx.this.scrollTable.getFooterTable() != null) {
						((CheckBox) ((FlexTable) ListEx.this.scrollTable
								.getFooterTable().getWidget(0, 0)).getWidget(0,
								0)).setChecked(false);
						size += ListEx.this.scrollTable.getFooterTable()
								.getOffsetHeight();
					}

					if (result.getCount() > 0) {
						ListEx.this.scrollTable.setHeight(String.valueOf(size)
								+ "px");
					}

				}
			}.begin();
		}

	}

	/**
	 * The default column to be sorted by the table
	 * 
	 * @return the column name according to hibernate
	 */
	public abstract String getDefaultSortParameter();

	/**
	 * The method that is called by the underlining table model to fetch more
	 * data from the server.
	 * 
	 * @param p
	 *            the pagination parameters that are used to specify the special
	 *            properties of the requisted data
	 * @param ac
	 *            the callback to signal when we are done fetching data
	 */
	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<T>> ac);

	/**
	 * From a list of objects it generates the data that will be displayed in
	 * the table
	 * 
	 * @param data
	 *            the list of data
	 * @return a collection of collections of data to be shown in the table
	 *         (basically what is going to be in each cell)
	 */
	public Collection<Collection<Object>> getList(final List<T> data) {
		final Collection<Collection<Object>> rows = new HashSet<Collection<Object>>();
		final Iterator<T> itr = data.iterator();
		int currentRow = 0;
		while (itr.hasNext()) {
			rows.add(processRow(itr.next(), currentRow++));
		}
		return rows;
	}

	/**
	 * Gets the data for only one row in the table from an object. Data is
	 * generated in one of two ways <br/>
	 * <ol>
	 * <li>From the widget/text that is returned from the column
	 * {@link Column#getRepresentation(MObjectDTO, int)}</li>
	 * <li>From the data of the object
	 * {@link MObjectDTO#mGet(edu.rpi.metpetdb.client.model.properties.Property)}</li>
	 * </ol>
	 * If either of those two scenarios are not possible it sets the data to be
	 * the header of the column
	 * 
	 * @param object
	 *            the current object
	 * @param currentRow
	 *            the current row
	 * @return the list of data
	 */
	public Collection<Object> processRow(final MObjectDTO object,
			final int currentRow) {
		final Collection<Object> data = new ArrayList<Object>();
		for (int i = 0; i < displayColumns.size(); ++i) {
			if (displayColumns.get(i).isCustomFormat()) {
				data.add(displayColumns.get(i).getRepresentation(object,
						currentRow));
			} else if (displayColumns.get(i).getProperty() != null) {
				data.add(displayColumns.get(i).getProperty().get(object));
			} else
				data.add(displayColumns.get(i).getTitle());
		}
		return data;
	}

	public ArrayList<Column> getOriginalColumns() {
		return originalColumns;
	}

	public ArrayList<Column> getDisplayColumns() {
		return displayColumns;
	}

	/**
	 * Creates a new paginated table from an array of columns
	 * 
	 * @param columns
	 *            the columns that will be used in this table
	 */
	public ListEx(final ArrayList<Column> columns) {
		this.originalColumns = columns;
		this.displayColumns = columns;
		tableModel = new TableModel();
		dataTable = new FixedWidthGrid();
		headerTable = new FixedWidthFlexTable();
		scrollTable = new PagingScrollTable<T>(tableModel, dataTable,
				headerTable);
		PagingOptions options = new PagingOptions(scrollTable);

		scrollTable.setPageSize(10);
		tableModel.setRowCount(1);

		for (int i = 0; i < displayColumns.size(); ++i) {
			headerTable.setText(0, i, displayColumns.get(i).getTitle());
			headerTable.getCellFormatter().addStyleName(0, i, "bold");
			headerTable.getCellFormatter().addStyleName(0, i, "brown");
			headerTable.getCellFormatter().setAlignment(0, i,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}

		// Setup sortable/unsortable columns
		for (int i = 0; i < displayColumns.size(); ++i) {
			scrollTable
					.setColumnSortable(i, displayColumns.get(i).isSortable());
		}

		dataTable.addTableListener(new TableListener() {

			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				displayColumns.get(cell).handleClickEvent(
						(MObjectDTO) scrollTable.getRowValue(row), row);

			}

		});

		// TODO: can you say we need CSS?
		scrollTable.setWidth("100%");
		dataTable.setWidth("100%");
		headerTable.setWidth("100%");
		headerTable.setHeight("30px");
		headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		dataTable.addStyleName("mpdb-dataTable");

		add(scrollTable);
		add(options);
		this.setWidth("100%");
		// dataTable.setHeight("400px");
	}

	public void newView(ArrayList<Column> columns) {
		this.displayColumns = columns;

		headerTable.removeRow(0);
		for (int i = 0; i < displayColumns.size(); ++i) {
			headerTable.setText(0, i, displayColumns.get(i).getTitle());
			headerTable.getCellFormatter().addStyleName(0, i, "bold");
			headerTable.getCellFormatter().addStyleName(0, i, "brown");
			headerTable.getCellFormatter().setAlignment(0, i,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}

		// Setup sortable/unsortable columns
		for (int i = 0; i < displayColumns.size(); ++i) {
			scrollTable
					.setColumnSortable(i, displayColumns.get(i).isSortable());
		}

		dataTable.addTableListener(new TableListener() {

			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				displayColumns.get(cell).handleClickEvent(
						(MObjectDTO) scrollTable.getRowValue(row), row);

			}

		});
		headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		scrollTable.reloadPage();
	}

	/**
	 * Reloads the current page to force a refresh of the data
	 */
	public void refresh() {
		scrollTable.gotoFirstPage();
		scrollTable.reloadPage();
	}
}

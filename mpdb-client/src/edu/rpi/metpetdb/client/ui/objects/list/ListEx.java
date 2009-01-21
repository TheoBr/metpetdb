package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.PagingOptions;
import com.google.gwt.widgetideas.table.client.PagingScrollTable;
import com.google.gwt.widgetideas.table.client.ReadOnlyTableModel;
import com.google.gwt.widgetideas.table.client.TableModel.Response;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
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
 * @param <
 * 		T> the types of objects that will be paginated in this table
 */
public abstract class ListEx<T extends MObject> extends FlowPanel {
	private int pageSize = 10;
	private PaginationParameters currentPagination;
	private PaginationParameters oldPagination;
	
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

	private Label noResults = new Label();

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
		 * 		the row data
		 */
		public PagingResponse(Collection<Collection<Object>> rows) {
			this(rows, null);
		}

		/**
		 * Constructor.
		 * 
		 * @param rows
		 * 		the row data
		 * @param rowValues
		 * 		the values to associate with each row
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
			if (oldPagination != null) {
				updateWithPagination(oldPagination, callback, request);
				oldPagination = null;
			} else {
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
				
				currentPagination = p;
	
				updateWithPagination(p, callback, request);
			}
		}
		
		public void updateWithPagination(final PaginationParameters p, 
				final Callback<T> callback, final Request request){
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
						final int headerRowCount = ListEx.this.scrollTable
								.getHeaderTable().getRowCount();
						for (int i = 0; i < headerRowCount; i++) {
							ListEx.this.scrollTable.getHeaderTable()
									.getRowFormatter().setVisible(i, false);
						}
						ListEx.this.scrollTable.getHeaderTable().setWidget(
								headerRowCount, 0, noResults);
						ListEx.this.scrollTable.getHeaderTable()
								.getFlexCellFormatter().setColSpan(
										headerRowCount, 0,
										ListEx.this.displayColumns.size());
						ListEx.this.scrollTable.getHeaderTable()
								.getFlexCellFormatter().setAlignment(
										headerRowCount, 0,
										HasHorizontalAlignment.ALIGN_CENTER,
										HasVerticalAlignment.ALIGN_MIDDLE);
							callback.onRowsReady(request, response);
					} else {
						for (int i = 0; i < ListEx.this.scrollTable
								.getHeaderTable().getRowCount(); i++) {
							ListEx.this.scrollTable.getHeaderTable()
									.getRowFormatter().setVisible(i, true);
							if (ListEx.this.scrollTable.getHeaderTable()
									.getWidget(i, 0) == noResults) {
								ListEx.this.scrollTable.getHeaderTable()
										.removeRow(i);
							}
						}
							callback.onRowsReady(request, response);
					}

					int size = ListEx.this.scrollTable.getHeaderTable()
							.getOffsetHeight()
							+ ListEx.this.scrollTable.getDataTable()
									.getOffsetHeight();

					if (ListEx.this.scrollTable.getFooterTable() != null) {
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
	 * 		the pagination parameters that are used to specify the special
	 * 		properties of the requisted data
	 * @param ac
	 * 		the callback to signal when we are done fetching data
	 */
	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<T>> ac);

	/**
	 * From a list of objects it generates the data that will be displayed in
	 * the table
	 * 
	 * @param data
	 * 		the list of data
	 * @return a collection of collections of data to be shown in the table
	 * 	(basically what is going to be in each cell)
	 */
	public Collection<Collection<Object>> getList(final List<T> data) {
		final Collection<Collection<Object>> rows = new ArrayList<Collection<Object>>();
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
	 * <li>From the widget/text that is returned from the column {@link
	 * Column#getRepresentation(MObject, int)}</li>
	 * <li>From the data of the object {@link
	 * MObject#mGet(edu.rpi.metpetdb.client.model.properties.Property)}
	 * </li>
	 * </ol>
	 * If either of those two scenarios are not possible it sets the data to be
	 * the header of the column
	 * 
	 * @param object
	 * 		the current object
	 * @param currentRow
	 * 		the current row
	 * @return the list of data
	 */
	public Collection<Object> processRow(final MObject object,
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
	 * 		the columns that will be used in this table
	 */
	public ListEx(final ArrayList<Column> columns) {
		this.originalColumns = columns;
		this.displayColumns = columns;
		tableModel = new TableModel();
		dataTable = new FixedWidthGrid();
		headerTable = new FixedWidthFlexTable();
		scrollTable = new PagingScrollTable<T>(tableModel, dataTable,
				headerTable) {
			public void reloadPage() {
				for (int i = 0; i < scrollTable.getDataTable().getRowCount(); i++) {
					scrollTable.getDataTable().getRowFormatter()
							.removeStyleName(i, "highlighted-row");
				}
				super.reloadPage();
			}
		};
		PagingOptions options = new PagingOptions(scrollTable);

		scrollTable.setPageSize(pageSize);
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
						(MObject) scrollTable.getRowValue(row), row);

			}

		});

		final Label pageSizeLabel = new Label("Results Per Page:");
		final TextBox pageSizeTxt = new TextBox();
		pageSizeTxt.setWidth("30px");
		pageSizeTxt.setText(String.valueOf(pageSize));
		final Button pageSizeBtn = new Button("Set");
		pageSizeBtn.setStyleName("smallBtn");
		pageSizeTxt.addKeyboardListener(new KeyboardListener() {
			public void onKeyPress(Widget sender, char keyCode, int modifiers){
				if(keyCode == KeyboardListener.KEY_ENTER)
					try { 
						setPageSize(Integer.parseInt(pageSizeTxt.getText()));
					} catch (Exception e) {
						
					} finally {
						
					}
			}
			public void onKeyDown(Widget sender, char keyCode, int modifiers){}
			public void onKeyUp(Widget sender, char keyCode, int modifiers){}
		});
		pageSizeBtn.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				try {
					setPageSize(Integer.parseInt(pageSizeTxt.getText()));
				} catch (Exception e) {

				} finally {

				}
			}
		});
		final HorizontalPanel hp = new HorizontalPanel();
		hp.add(pageSizeLabel);
		hp.add(pageSizeTxt);
		hp.add(pageSizeBtn);
		// scrollTable.getFooterTable().add(new );

		// TODO: can you say we need CSS
		// "We need CSS" -anthony
		scrollTable.setWidth("100%");
		dataTable.setWidth("100%");
		headerTable.setWidth("100%");
		headerTable.setHeight("30px");
		headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		dataTable.addStyleName("mpdb-dataTable");

		hp.addStyleName("inline");
		options.addStyleName("inline");

		add(scrollTable);
		add(options);
		add(hp);
		this.setWidth("100%");
	}

	public ListEx(final ArrayList<Column> columns, final String noResultsMessage) {
		this(columns);
		noResults.setText(noResultsMessage);
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
						(MObject) scrollTable.getRowValue(row), row);

			}

		});
		headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		scrollTable.reloadPage();
	}
	
	public void simpleView(){
		ArrayList<Column> simple = new ArrayList<Column>();
		for (Column c : originalColumns){
			if (c.isSimpleView()){
				simple.add(c);
			}
		}
		newView(simple);
	}

	/**
	 * Reloads the current page to force a refresh of the data
	 */
	public void refresh() {
		scrollTable.gotoFirstPage();
		scrollTable.reloadPage();
	}
	
	public void refresh(final PaginationParameters p) {
		oldPagination = p;
		scrollTable.reloadPage();
	}

	public void setPageSize(final int PageSize) {
		pageSize = PageSize;
		scrollTable.setPageSize(pageSize);
		refresh();
	}
	public int getPageSize() {
		return pageSize;
	}
	
	public PaginationParameters getPaginationParameters(){
		return currentPagination;
	}
}

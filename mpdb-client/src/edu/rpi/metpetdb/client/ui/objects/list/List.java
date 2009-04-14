package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.PagingOptions;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ColumnResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.gen2.table.event.client.PageLoadEvent;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.RowHighlightEvent;
import com.google.gwt.gen2.table.event.client.RowHighlightHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionEvent;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.event.client.RowUnhighlightEvent;
import com.google.gwt.gen2.table.event.client.RowUnhighlightHandler;
import com.google.gwt.gen2.table.event.client.TableEvent.Row;
import com.google.gwt.gen2.table.override.client.HTMLTable.RowFormatter;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.MPagingOptions;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

/**
 * An abstract class that pieces together the different parts for creating a
 * paginated table.
 * 
 * @author anthony
 * 
 * @param < T>
 *            the types of objects that will be paginated in this table
 */
public abstract class List<RowType extends MObject> extends FlowPanel {
	private int pageSize = 25;
	private final static long millisecondsIn30Days = 2592000000L;
	private final static String pageSizeCookie = "pageSize";
	private PaginationParameters currentPagination;
	private PaginationParameters oldPagination;
	private final String STYLENAME = "scrolltable";

	/**
	 * How we sort by default (true means ascending, false is descending)
	 */
	private static final boolean DEFAULT_SORT_ORDER = true;
	protected PagingScrollTable<RowType> scrollTable;

	public PagingScrollTable<RowType> getScrollTable() {
		return scrollTable;
	}

	/**
	 * The instance of columns that are layed out on the table
	 */
	private ArrayList<Column<RowType, ?>> originalColumns;
	private ArrayList<Column<RowType, ?>> displayColumns;

	private MpDbTableModel tableModel;
	protected FixedWidthGrid dataTable;
	private FixedWidthFlexTable headerTable;
	private MTwoColPanel topbar;
	private ListBox pageSizeChoice;

	/**
	 * The table model that is used to draw the table
	 * 
	 * @author anthony, zakness
	 * 
	 */
	public class MpDbTableModel extends TableModel<RowType> {

		/**
		 * Called automatically when new rows are requested, i.e. when the user
		 * navitages to a new page.
		 */
		public void requestRows(final Request request,
				final Callback<RowType> callback) {
			if (oldPagination != null) {
				updateWithPagination(oldPagination, callback, request);
				oldPagination = null;
			} else {
				final int startRow = request.getStartRow();
				final int numRows = request.getNumRows();

				final ColumnSortList sortList = request.getColumnSortList();
				// Get column to sort
				final ColumnSortInfo sortInfo = sortList
						.getPrimaryColumnSortInfo();
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

				new ServerOp<Results<RowType>>() {
					@Override
					public void begin() {
						update(p, this);
					}

					public void onSuccess(final Results<RowType> result) {
						setRowCount(result.getCount());
						final SerializableResponse<RowType> response = new SerializableResponse<RowType>(
								result.getList());
						callback.onRowsReady(request, response);
					}
				}.begin();
			}
		}

		public void updateWithPagination(final PaginationParameters p,
				final Callback<RowType> callback, final Request request) {

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
			final AsyncCallback<Results<RowType>> ac);

	public ArrayList<Column<RowType, ?>> getOriginalColumns() {
		return originalColumns;
	}

	public ArrayList<Column<RowType, ?>> getDisplayColumns() {
		return displayColumns;
	}

	public void addRowSelectionHandler(RowSelectionHandler handler) {
		dataTable.addRowSelectionHandler(handler);
	}

	public ArrayList<RowType> getSelectedValues() {
		final ArrayList<RowType> selectedRows = new ArrayList<RowType>();
		for (Integer i : dataTable.getSelectedRows()) {
			selectedRows.add(scrollTable.getRowValue(i));
		}
		return selectedRows;
	}
	
	public RowType getRowValue(int row) {
		return scrollTable.getRowValue(row);
	}
	
	public int getRowCount() {
		return dataTable.getRowCount();
	}
	
	public RowFormatter getRowFormatter() {
		return dataTable.getRowFormatter();
	}
	
	public void addPageLoadHandler(PageLoadHandler handler) {
		scrollTable.addPageLoadHandler(handler);
	}
	
	public FixedWidthGrid getDataTable() {
		return dataTable;
	}
	

	/**
	 * Creates a new paginated table from an array of columns
	 * 
	 * @param columns
	 *            the columns that will be used in this table
	 */
	public List(final ArrayList<Column<RowType, ?>> columns) {
		this.originalColumns = columns;
		this.displayColumns = columns;
		setStylePrimaryName(STYLENAME);
		
		// table model contains the data
		tableModel = new MpDbTableModel();
		tableModel.setRowCount(1);
		
		// data table is the table widget
		dataTable = new FixedWidthGrid();
		dataTable.resize(pageSize, columns.size());
		dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);
		dataTable.setSelectionEnabled(false);
		dataTable.addStyleName("mpdb-dataTable");
		
		// first row of column headers
		headerTable = new FixedWidthFlexTable();
		for (int i = 0; i < displayColumns.size(); ++i) {
			if (displayColumns.get(i).getHeader() instanceof String)
				headerTable.setText(0, i, (String) displayColumns.get(i).getHeader());
			if (displayColumns.get(i).getHeader() instanceof Widget)
				headerTable.setWidget(0, i, (Widget) displayColumns.get(i).getHeader());
			headerTable.getCellFormatter().addStyleName(0, i, "bold");
			headerTable.getCellFormatter().addStyleName(0, i, "brown");
			headerTable.getCellFormatter().setAlignment(0, i,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}
		headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		
		// scroll table brings it all together
		final DefaultTableDefinition<RowType> tableDefinition = new DefaultTableDefinition<RowType>();
		for (Column<RowType, ?> c : columns) {
			tableDefinition.addColumnDefinition(c);
		}
		scrollTable = new PagingScrollTable<RowType>(tableModel, dataTable,
				headerTable, tableDefinition);
		final FixedWidthGridBulkRenderer<RowType> bulkRenderer = new FixedWidthGridBulkRenderer<RowType>(
				dataTable, scrollTable);
		scrollTable.setBulkRenderer(bulkRenderer);
		scrollTable.setHeight("400px");
		scrollTable.setEmptyTableWidget(getNoResultsWidget());
		scrollTable.setPageSize(loadPageSizeCookie());
		scrollTable.setSortPolicy(SortPolicy.MULTI_CELL);
		scrollTable.setColumnResizePolicy(ColumnResizePolicy.SINGLE_CELL);
		scrollTable.setResizePolicy(ResizePolicy.UNCONSTRAINED);
		scrollTable.setScrollPolicy(ScrollPolicy.HORIZONTAL);
		
		// topbar contains pagination and column options
		topbar = new MTwoColPanel();
		topbar.addStyleName(STYLENAME + "-topbar");
		
		final HTMLPanel perPagePanel = new HTMLPanel(
			"Show <span id=\"perpage-choice\"></span> per page."
		);
		pageSizeChoice = new ListBox();
		String[] choices = {"25", "50", "100"};
		setPageSizeChoices(choices);
		pageSizeChoice.addChangeListener(new ChangeListener() {
			public void onChange(Widget sender) {
				try {
					setPageSize(Integer.parseInt(pageSizeChoice.getItemText(pageSizeChoice.getSelectedIndex())));
				} catch (Exception e) {

				} finally {

				}
			}
		});
		perPagePanel.addAndReplaceElement(pageSizeChoice, "perpage-choice");
		perPagePanel.setStyleName("perpage");
		topbar.getRightCol().add(perPagePanel);
		
		MPagingOptions options = new MPagingOptions(scrollTable);
		topbar.getRightCol().add(options);
	
		add(topbar);
		add(scrollTable);
	}
	public void newView(ArrayList<Column> columns) {
	// this.displayColumns = columns;
	//
	// headerTable.removeRow(0);
	// for (int i = 0; i < displayColumns.size(); ++i) {
	// headerTable.setText(0, i, displayColumns.get(i).getTitle());
	// headerTable.getCellFormatter().addStyleName(0, i, "bold");
	// headerTable.getCellFormatter().addStyleName(0, i, "brown");
	// headerTable.getCellFormatter().setAlignment(0, i,
	// HasHorizontalAlignment.ALIGN_LEFT,
	// HasVerticalAlignment.ALIGN_MIDDLE);
	// }
	//
	// dataTable.addTableListener(new TableListener() {
	//
	// public void onCellClicked(SourcesTableEvents sender, int row,
	// int cell) {
	// displayColumns.get(cell).handleClickEvent(
	// (MObject) scrollTable.getRowValue(row), row);
	//
	// }
	//
	// });
	// headerTable.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
	// scrollTable.reloadPage();
	}

	public void simpleView() {
	// ArrayList<Column> simple = new ArrayList<Column>();
	// for (Column c : originalColumns){
	// if (c.isSimpleView()){
	// simple.add(c);
	// }
	// }
	// newView(simple);
	}

	/**
	 * Reloads the current page to force a refresh of the data
	 */
	public void refresh() {
		dataTable.resize(pageSize, originalColumns.size());
		scrollTable.gotoFirstPage();
		scrollTable.reloadPage();
	}

	public void refresh(final PaginationParameters p) {
		oldPagination = p;
		scrollTable.reloadPage();
	}

	public void setPageSize(final int PageSize) {
		if (PageSize > 0) {
			pageSize = PageSize;
			scrollTable.setPageSize(pageSize);
			createPageSizeCookie(pageSize);
			refresh();
		}
	}
	public int getPageSize() {
		return pageSize;
	}

	public PaginationParameters getPaginationParameters() {
		return currentPagination;
	}

	public void createPageSizeCookie(final int newPageSize) {
		final Date expires = new Date();
		expires.setTime(expires.getTime() + millisecondsIn30Days);
		Cookies.setCookie(pageSizeCookie, String.valueOf(newPageSize), expires);
	}
	
	public void setPageSizeChoices(String[] choices) {
		pageSizeChoice.clear();
		for (String choice : choices) {
			pageSizeChoice.addItem(choice);
		}
	}

	private int loadPageSizeCookie() {
		if (Cookies.getCookie(pageSizeCookie) != null) {
			return Integer.parseInt(Cookies.getCookie(pageSizeCookie));
		} else
			return 10;
	}

	protected abstract Widget getNoResultsWidget();
}

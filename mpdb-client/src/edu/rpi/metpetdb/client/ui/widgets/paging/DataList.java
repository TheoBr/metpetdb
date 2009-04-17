package edu.rpi.metpetdb.client.ui.widgets.paging;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.gen2.table.client.DefaultTableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortInfo;
import com.google.gwt.gen2.table.client.TableModelHelper.ColumnSortList;
import com.google.gwt.gen2.table.client.TableModelHelper.Request;
import com.google.gwt.gen2.table.client.TableModelHelper.SerializableResponse;
import com.google.gwt.gen2.table.event.client.PageLoadHandler;
import com.google.gwt.gen2.table.event.client.RowSelectionHandler;
import com.google.gwt.gen2.table.override.client.HTMLTable.RowFormatter;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.dialogs.CustomTableView;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
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
public abstract class DataList<RowType extends MObject> extends FlowPanel {
	private final static String STORAGE_COOKIE = "mpdb-list";
	private final static String STYLENAME = "scrolltable";
	/**
	 * How we sort by default (true means ascending, false is descending)
	 */
	private static final boolean DEFAULT_SORT_ORDER = true;

	private final ListCookieJar cookies;
	private final InlineLabel results;

	/**
	 * Returns the name of the list for use in the cookie (YUM!)
	 * 
	 * @return
	 */
	protected abstract String getListName();

	/**
	 * Returns the name of the cookie for storing stuff
	 * 
	 * @return
	 */
	private String getCookieName() {
		return getListName() + "-" + STORAGE_COOKIE;
	}

	/**
	 * Maps a column name to the actual column, and contains all of the columns
	 * that this list supports
	 */
	private ColumnDefinition<RowType> allColumns;
	/**
	 * Map of the columns that are being shown NOW!
	 */
	private ColumnDefinition<RowType> displayColumns;
	private DefaultTableDefinition<RowType> tableDefinition = new DefaultTableDefinition<RowType>();

	private MpDbPagingScrollTable<RowType> scrollTable;

	public MpDbPagingScrollTable<RowType> getScrollTable() {
		if (scrollTable == null) {
			scrollTable = new MpDbPagingScrollTable<RowType>(getTableModel(),
					getDataTable(), getHeaderTable(), tableDefinition) {
				@Override
				public Widget getNoResultsWidgetImpl() {
					return getNoResultsWidget();
				}
			};
		}
		return scrollTable;
	}

	private MpDbTableModel tableModel;

	protected MpDbTableModel getTableModel() {
		if (tableModel == null) {
			// table model contains the data
			tableModel = new MpDbTableModel();
			tableModel.setRowCount(1);
		}
		return tableModel;
	}

	private MTwoColPanel topbar;

	private SimplePanel tableActions;

	private MpDbDataTable dataTable;

	public MpDbDataTable getDataTable() {
		if (dataTable == null) {
			dataTable = new MpDbDataTable();
		}
		return dataTable;
	}

	private MpDbHeaderTable headerTable;

	protected MpDbHeaderTable getHeaderTable() {
		if (headerTable == null) {
			headerTable = new MpDbHeaderTable(displayColumns, getDataTable()
					.isSelectionEnabled());
		}
		return headerTable;
	}

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
			final int startRow = request.getStartRow();
			final int numRows = request.getNumRows();

			final ColumnSortList sortList = request.getColumnSortList();
			// Get column to sort
			final ColumnSortInfo sortInfo = sortList.getPrimaryColumnSortInfo();
			final PaginationParameters p = new PaginationParameters();
			if (sortInfo != null) {
				p.setParameter(displayColumns.getColumn(sortInfo.getColumn())
						.getName());
				p.setAscending(sortInfo.isAscending());
			} else {
				p.setAscending(DEFAULT_SORT_ORDER);
				p.setParameter(getDefaultSortParameter());
			}
			p.setFirstResult(startRow);
			p.setMaxResults(numRows);

			new ServerOp<Results<RowType>>() {
				@Override
				public void begin() {
					update(p, this);
				}

				public void onSuccess(final Results<RowType> result) {
					results.setText(result.getCount() + " results discovered!");
					setRowCount(result.getCount());
					final SerializableResponse<RowType> response = new SerializableResponse<RowType>(
							result.getList());
					callback.onRowsReady(request, response);
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
			final AsyncCallback<Results<RowType>> ac);

	public void addRowSelectionHandler(RowSelectionHandler handler) {
		dataTable.addRowSelectionHandler(handler);
	}

	public ArrayList<RowType> getSelectedValues() {
		final ArrayList<RowType> selectedRows = new ArrayList<RowType>();
		for (Integer i : dataTable.getSelectedRows()) {
			selectedRows.add(getScrollTable().getRowValue(i));
		}
		return selectedRows;
	}

	public RowType getRowValue(int row) {
		return getScrollTable().getRowValue(row);
	}

	public int getRowCount() {
		return dataTable.getRowCount();
	}

	public RowFormatter getRowFormatter() {
		return dataTable.getRowFormatter();
	}

	public void addPageLoadHandler(PageLoadHandler handler) {
		getScrollTable().addPageLoadHandler(handler);
	}

	public DefaultTableDefinition<RowType> getTableDefinition() {
		return tableDefinition;
	}

	/**
	 * Sets up the display columns for the list based on what is stored in the
	 * cookie, if there is no cookie it adds in the required columns
	 */
	private void setUpColumns() {
		displayColumns = new ColumnDefinition<RowType>();
		final List<String> cookieColumns = cookies.getColumns();
		if (cookieColumns.size() == 0) {
			// get the defualt columns
			for (Column<RowType, ?> c : getDefaultColumns()) {
				cookieColumns.add(c.getName());
			}
			cookies.setColumns(cookieColumns);
		}
		for (Column<RowType, ?> c : allColumns) {
			tableDefinition.addColumnDefinition(c);
			tableDefinition.setColumnVisible(c, false);
		}
		for (int i = 0; i < cookieColumns.size(); ++i) {
			final Column<RowType, ?> c = allColumns.getColumn(cookieColumns
					.get(i));
			if (c != null) {
				displayColumns.addColumn(c);
				tableDefinition.setColumnVisible(c, true);
			}
		}
	}

	/**
	 * Refreshes the currently shown columns
	 */
	private void refreshColumns() {
		for (Column<RowType, ?> c : allColumns) {
			tableDefinition.setColumnVisible(c, false);
		}
		final List<String> columns = new ArrayList<String>();
		for (Column<RowType, ?> c : displayColumns) {
			tableDefinition.setColumnVisible(c, true);
			columns.add(c.getName());
		}
		cookies.setColumns(columns);
		// update the header
		headerTable.update(displayColumns, getDataTable().isSelectionEnabled());
		getScrollTable().redraw();
		getScrollTable().reloadPage();
		getScrollTable().fillWidth();
	}

	private void setUpPagination() {

	}

	/**
	 * Initializes the actual layout of the list, it should be called after all
	 * settings are set (i.e. if the data table has a selection column)
	 */
	protected void initialize() {
		setUpPagination();
		setStylePrimaryName(STYLENAME);
		// topbar contains pagination and column options
		topbar = new MTwoColPanel();
		topbar.addStyleName(STYLENAME + "-topbar");
		
		final PageSizeChooser psc = new PageSizeChooser() {

			@Override
			public void setPageSizeImpl(int newSize) {
				setPageSize(newSize);
			}

		};
		psc.setSelectedPageSize(cookies.getPageSize());

		topbar.getRightCol().add(psc);
		MPagingOptions options = new MPagingOptions(getScrollTable());
		topbar.getRightCol().add(options);

		// so the pagination displays correctly even without custom columns
		final MLink customCols = new MLink("Custom Columnz", new ClickListener() {
			public void onClick(Widget sender) {
				CustomTableView<RowType> myView = new CustomTableView<RowType>(
						allColumns, displayColumns) {

					@Override
					public void onSetNewColumns(
							ColumnDefinition<RowType> newCols) {
						displayColumns = newCols;
						refreshColumns();
					}

				};
				myView.show();
			}
		});
		topbar.getLeftCol().add(results);
		topbar.getLeftCol().add(customCols);

		// container for widgets used to do stuff with selected rows
		tableActions = new SimplePanel();

		setPageSize(cookies.getPageSize());

		add(topbar);
		add(tableActions);
		add(getScrollTable());
	}

	/**
	 * Creates a new paginated table from an array of columns
	 * 
	 * @param columns
	 *            the columns that will be used in this table
	 */
	public DataList(final ColumnDefinition<RowType> columns) {
		this.allColumns = columns;
		cookies = new ListCookieJar(getCookieName());
		results = new InlineLabel("");
		setUpColumns();
	}

	public FlowPanel getTopBar() {
		return topbar.getLeftCol();
	}

	public void setPageSize(final int pageSize) {
		if (pageSize > 0) {
			dataTable.resize(pageSize, allColumns.size());
			getScrollTable().setPageSize(pageSize);
			cookies.setPageSize(pageSize);
			getScrollTable().gotoFirstPage();
			getScrollTable().reloadPage();
			
		}
	}

	public void setTableActions(Widget w) {
		tableActions.setWidget(w);
	}
	
	public void onAttach() {
		super.onAttach();
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				fillWidth();
			}
		});
	}
	
	public void fillWidth() {
		//HACK: forces the scroll table to resize based on the header column
		//size as opposed to the data table
		getDataTable().resizeColumns(0);
		getScrollTable().fillWidth();
		getDataTable().resizeColumns(displayColumns.size());
	}

	protected abstract Widget getNoResultsWidget();

	protected abstract ColumnDefinition<RowType> getDefaultColumns();
}

package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.PagingGrid;
import com.google.gwt.widgetideas.table.client.PagingScrollTable;
import com.google.gwt.widgetideas.table.client.ReadOnlyTableModel;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;

public abstract class ListEx extends FlowPanel {

	private static final boolean DEFAULT_SORT_ORDER = true;

	protected final PagingGrid dataTable;

	private Column[] columns;

	protected class TableModel extends ReadOnlyTableModel {
		public void requestRows(final Request request, final Callback callback) {
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
			handleResults(p, request, callback);
		}
	}

	public abstract void handleResults(final PaginationParameters p,
			final TableModel.Request request, final TableModel.Callback callback);

	public abstract String getDefaultSortParameter();

	public <T extends MObjectDTO> List<List<Object>> getList(final List<T> data) {
		final List<List<Object>> rows = new ArrayList<List<Object>>();
		final Iterator<T> itr = data.iterator();
		int currentRow = 0;
		while (itr.hasNext()) {
			rows.add(processRow(itr.next(), currentRow++));
		}
		return rows;
	}

	public List<Object> processRow(final MObjectDTO object, final int currentRow) {
		final List<Object> data = new ArrayList<Object>();
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
		dataTable = new PagingGrid(tableModel);

		dataTable.setPageSize(2);
		dataTable.setNumRows(100);
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

		for (int i = 0; i < columns.length; ++i) {
			headerTable.setText(0, i, columns[i].getTitle());
		}

		final PagingScrollTable scrollTable = new PagingScrollTable(dataTable,
				headerTable);

		// Setup sortable/unsortable columns
		for (int i = 0; i < columns.length; ++i) {
			scrollTable.setColumnSortable(i, columns[i].isSortable());
		}

		dataTable.addTableListener(new TableListener() {

			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				columns[cell].handleClickEvent((MObjectDTO) dataTable
						.getRowValue(row - 1), row - 1);

			}

		});

		scrollTable.setHeight("100%");
		scrollTable.setWidth("100%");
		dataTable.setWidth("100%");
		dataTable.setHeight("100%");
		headerTable.setWidth("100%");
		add(scrollTable);
		this.setHeight("400px");
		this.setWidth("100%");
	}
}

package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.FixedWidthGridBulkRenderer;
import com.google.gwt.gen2.table.client.PagingScrollTable;
import com.google.gwt.gen2.table.client.TableDefinition;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MpDbPagingScrollTable<RowType> extends PagingScrollTable<RowType> {

	public MpDbPagingScrollTable(TableModel<RowType> tableModel,
			FixedWidthGrid dataTable, FixedWidthFlexTable headerTable,
			TableDefinition<RowType> tableDefinition) {
		super(tableModel, dataTable, headerTable, tableDefinition);
		final FixedWidthGridBulkRenderer<RowType> bulkRenderer = new FixedWidthGridBulkRenderer<RowType>(
				dataTable, this);
		setBulkRenderer(bulkRenderer);
		setEmptyTableWidget(getNoResultsWidgetImpl());
		setSortPolicy(SortPolicy.MULTI_CELL);
		setColumnResizePolicy(ColumnResizePolicy.SINGLE_CELL);
		setResizePolicy(ResizePolicy.FLOW);
		setScrollPolicy(ScrollPolicy.DISABLED);
	}
	
	public abstract Widget getNoResultsWidgetImpl();

}

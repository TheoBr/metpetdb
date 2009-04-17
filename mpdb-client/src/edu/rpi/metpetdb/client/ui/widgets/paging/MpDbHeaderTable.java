package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;

public class MpDbHeaderTable extends FixedWidthFlexTable {

	public MpDbHeaderTable(final ColumnDefinition<?> displayColumns,
			final boolean selectionEnabled) {
		super();
		update(displayColumns, selectionEnabled);
	}

	public void update(final ColumnDefinition<?> displayColumns,
			final boolean selectionEnabled) {
		int offset = selectionEnabled ? 1 : 0;
		if (getRowCount() > 0) {
			for (int i = 0; i < getCellCount(0); ++i) {
				getCellFormatter().removeStyleName(0, i, "bold");
				getCellFormatter().removeStyleName(0, i, "brown");
			}
		}
		this.clearAll();
		for (int i = 0; i < displayColumns.size(); ++i) {
			if (displayColumns.getColumn(i).getHeader() instanceof String)
				setText(0, i + offset, (String) displayColumns.getColumn(i)
						.getHeader());
			if (displayColumns.getColumn(i).getHeader() instanceof Widget)
				setWidget(0, i + offset, (Widget) displayColumns.getColumn(i)
						.getHeader());
			getCellFormatter().addStyleName(0, i + offset, "bold");
			getCellFormatter().addStyleName(0, i + offset, "brown");
			getCellFormatter().setAlignment(0, i + offset,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		}
		getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
	}

}

package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.gen2.table.client.FixedWidthGrid;

public class MpDbDataTable extends FixedWidthGrid {
	
	public MpDbDataTable() {
		super();
		setSelectionPolicy(SelectionPolicy.CHECKBOX);
		setSelectionEnabled(false);
		addStyleName("mpdb-dataTable");
	}
}

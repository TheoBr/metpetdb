package edu.rpi.metpetdb.client.ui.widgets.paging;

import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.user.client.Event;

public class MpDbDataTable extends FixedWidthGrid {
	
	public MpDbDataTable() {
		super();
		setSelectionPolicy(SelectionPolicy.CHECKBOX);
		setSelectionEnabled(true);		
		this.sinkEvents(Event.ONMOUSEDOWN);
	}
}

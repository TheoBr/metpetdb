package edu.rpi.metpetdb.client.paging;

import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.properties.Property;

public class Column {

	private String title;
	private Property property;
	private boolean sortable;
	private ColumnEvent event;

	public Column(final String title, final Property property) {
		this(title, property, true, null);
	}

	public Column(final String title) {
		this(title, null, false, null);
	}

	public Column(final String title, final Property property,
			final boolean sortable) {
		this(title, property, sortable, null);
	}

	public Column(final String title, final ColumnEvent event) {
		this(title, null, false, event);
	}

	public Column(final String title, final Property property,
			final boolean sortable, final ColumnEvent event) {
		this.title = title;
		this.sortable = sortable;
		this.property = property;
		this.event = event;
	}

	public boolean isSortable() {
		return this.sortable;
	}

	public String getTitle() {
		return this.title;
	}

	public Property getProperty() {
		return this.property;
	}

	public void handleClickEvent(final Object data, final int row) {
		if (event != null)
			event.handleClickEvent(data, row);
	}

	public Widget getWidget(final Object data, final int row) {
		return null;
	}

}
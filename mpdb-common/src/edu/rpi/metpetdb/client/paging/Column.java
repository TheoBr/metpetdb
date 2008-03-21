package edu.rpi.metpetdb.client.paging;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.properties.Property;

public class Column {

	private String title;
	private Property property;
	private boolean sortable;
	private boolean customFormat;

	public Column(final String title, final Property property) {
		this(title, property, true, false);
	}

	public Column(final String title, final Property property,
			final boolean customFormat) {
		this(title, property, true, customFormat);
	}

	public Column(final String title) {
		this(title, null, false, false);
	}

	public Column(final String title, final Property property,
			final boolean sortable, final boolean customFormat) {
		this.title = title;
		this.sortable = sortable;
		this.property = property;
		this.customFormat = customFormat;
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

	public void handleClickEvent(final MObjectDTO data, final int row) {

	}

	public Object getRepresentation(final MObjectDTO data, final int row) {
		if (getWidget(data, row) == null)
			return getText(data, row);
		else
			return getWidget(data, row);
	}

	protected Object getWidget(final MObjectDTO data, final int row) {
		return null;
	}

	protected Object getText(final MObjectDTO data, final int row) {
		return "";
	}

	public boolean isCustomFormat() {
		return customFormat;
	}

	public Column setCustomFormat(boolean customFormat) {
		this.customFormat = customFormat;
		return this;
	}

}
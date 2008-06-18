package edu.rpi.metpetdb.client.paging;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Represents a column that is to be used in a paginated table
 * 
 * @author anthony
 * 
 */
public class Column {

	private String title;
	private Property property;
	private boolean sortable;
	private boolean customFormat;

	/**
	 * {@link #Column(String, Property, boolean, boolean)}
	 * 
	 * @param title
	 * @param property
	 */
	public Column(final String title, final boolean customFormat) {
		this(title, null, false, customFormat);
	}

	public Column(final String title, final Property property) {
		this(title, property, true, false);
	}

	public Column(final String title, final boolean sortable,
			final boolean customFormat) {
		this(title, null, sortable, customFormat);
	}

	/**
	 * {@link #Column(String, Property, boolean, boolean)}
	 * 
	 * @param title
	 * @param property
	 * @param customFormat
	 */
	public Column(final String title, final Property property,
			final boolean customFormat) {
		this(title, property, true, customFormat);
	}

	/**
	 * {@link #Column(String, Property, boolean, boolean)}
	 * 
	 * @param title
	 */
	public Column(final String title) {
		this(title, null, false, false);
	}

	/**
	 * Creates a column for use in a paginated table
	 * 
	 * @param title
	 *            the header
	 * @param property
	 *            the property
	 * @param sortable
	 *            whether it can be sorted
	 * @param customFormat
	 *            if it has a custom format
	 */
	public Column(final String title, final Property property,
			final boolean sortable, final boolean customFormat) {
		this.title = title;
		this.sortable = sortable;
		this.property = property;
		this.customFormat = customFormat;
	}

	/**
	 * Whether this column is allowed to be sorted
	 * 
	 * @return true if it can be sorted
	 */
	public boolean isSortable() {
		return this.sortable;
	}

	/**
	 * The title is the column headers
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * The property that is defined for this column see {@link Property}. It is
	 * used to extract the data from the bean.
	 * 
	 * @return the property
	 */
	public Property getProperty() {
		return this.property;
	}

	/**
	 * When this column is clicked this method is called
	 * 
	 * @param data
	 *            the bean that is represented from the row
	 * @param row
	 *            the row number that was clicked
	 */
	public void handleClickEvent(final MObjectDTO data, final int row) {

	}

	/**
	 * Returns the custom widget/text that this column takes advantage of
	 * 
	 * @param data
	 *            the bean that is being worked on
	 * @param row
	 *            the row that is being worked on
	 * @return either a string or a widget
	 */
	public Object getRepresentation(final MObjectDTO data, final int row) {
		if (getWidget(data, row) == null)
			return getText(data, row);
		else
			return getWidget(data, row);
	}

	/**
	 * This is used if more features are desired from a cell, i.e. having a
	 * link.
	 * 
	 * @param data
	 *            the bean that is being worked on
	 * @param row
	 *            the row that is being worked on
	 * @return the widget
	 */
	protected Object getWidget(final MObjectDTO data, final int row) {
		return null;
	}

	/**
	 * This is used if more features are desired from a cell, i.e. formatting
	 * PostGIS location to be displayed as coordinates.
	 * 
	 * @param data
	 *            the bean that is being worked on
	 * @param row
	 *            the row that is being worked on
	 * @return the text
	 */
	protected Object getText(final MObjectDTO data, final int row) {
		return "";
	}

	/**
	 * Whether this column defines a custom format, i.e. if it overrides
	 * {@link #getText(MObjectDTO, int)} or {@link #getWidget(MObjectDTO, int)}
	 * 
	 * @return true if it overrides, false otherwise
	 */
	public boolean isCustomFormat() {
		return customFormat;
	}

	/**
	 * Sets whether this column defines a custom format
	 * 
	 * @param customFormat
	 *            true if it defines a custom format, else otherwise
	 * @return
	 */
	public Column setCustomFormat(boolean customFormat) {
		this.customFormat = customFormat;
		return this;
	}

}

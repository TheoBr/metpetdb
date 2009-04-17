package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import com.google.gwt.gen2.table.client.AbstractColumnDefinition;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Represents a column that is to be used in a paginated table
 * 
 * @author anthony
 * 
 */
public abstract class Column<RowType extends MObject, ColType> extends AbstractColumnDefinition<RowType, ColType>{

	private Object header;
	protected Property<RowType> property;
	private boolean optional = false;
	/** name of the column in the cookie, be default it is the property name */
	private String name;
	
	public Column(Object header) {
		this(header, null);
	}

	/**
	 * Creates a column for use in a paginated table
	 * 
	 * @param header
	 * 		the header
	 * @param property
	 * 		the property
	 */
	public Column(Object header, final Property<RowType> property) {
		this.header = header;
		this.property = property;
		if (property != null)
			name = property.name();
	}

	/**
	 * The title is the column headers
	 * 
	 * @return the header
	 */
	public Object getHeader() {
		return this.header;
	}

	/**
	 * The property that is defined for this column see {@link Property}. It is
	 * used to extract the data from the bean.
	 * 
	 * @return the property
	 */
	public Property<RowType> getProperty() {
		return this.property;
	}

	/**
	 * When this column is clicked this method is called
	 * 
	 * @param data
	 * 		the bean that is represented from the row
	 * @param row
	 * 		the row number that was clicked
	 */
	public void handleClickEvent(final RowType data, final int row) {

	}
	
	public ColType getCellTooltipValue(final RowType rowValue) {
		return getCellValue(rowValue);
	}

	@Override
	public abstract ColType getCellValue(final RowType rowValue);

	@Override
	public void setCellValue(RowType rowValue, ColType cellValue) {
		//not supported by us, yet...
	}
	
	public Column<RowType, ColType> setOptional(boolean option) {
		optional = option;
		return this;
	}
	public boolean isOptional() {
		return optional;
	}

	public String getName() {
		return name;
	}

	public Column<RowType, ColType> setName(String name) {
		this.name = name;
		return this;
	}

}

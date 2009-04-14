package edu.rpi.metpetdb.client.ui.widgets.paging;

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
	private boolean isDisplayColumn = true;
	private boolean optional = false;
	
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

	public void setIsDisplayColumn(boolean display) {
		isDisplayColumn = display;
	}
	
	public void setOptional(boolean option) {
		optional = option;
	}
	public boolean isOptional() {
		return optional;
	}
	
	public boolean isDisplayColumn() {
		return isDisplayColumn;
	}

}

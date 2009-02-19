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

	private String title;
	protected Property<RowType> property;
	private boolean sortable;
	
	public Column(final String title) {
		this(title, null, true);
	}
	
	public Column(final String title, final Property<RowType> property) {
		this(title, property, true);
	}

	/**
	 * Creates a column for use in a paginated table
	 * 
	 * @param title
	 * 		the header
	 * @param property
	 * 		the property
	 * @param sortable
	 * 		whether it can be sorted
	 */
	public Column(final String title, final Property<RowType> property,
			final boolean sortable) {
		this.title = title;
		this.sortable = sortable;
		this.property = property;
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

	@Override
	public abstract ColType getCellValue(RowType rowValue);


	@Override
	public void setCellValue(RowType rowValue, ColType cellValue) {
		//not supported by us, yet...
	}

}

package edu.rpi.metpetdb.client.ui.widgets.paging;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

public class MultipleStringColumn<RowType extends MObject> extends MultipleColumn<RowType, String> {

	public MultipleStringColumn(String title, Property<RowType> property) {
		super(title, property);
	}

	@Override
	public String getCellValue(RowType rowValue) {
		return getCellContents(rowValue);
	}
	
	

}

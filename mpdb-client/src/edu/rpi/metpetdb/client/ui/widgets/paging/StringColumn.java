package edu.rpi.metpetdb.client.ui.widgets.paging;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

public class StringColumn<RowType extends MObject> extends
		Column<RowType, String> {

	public StringColumn(String title, Property<RowType> property) {
		super(title, property);
	}

	@Override
	public String getCellValue(RowType rowValue) {
		return property.get(rowValue) == null ? "----" : property.get(rowValue)
				.toString();
	}

}

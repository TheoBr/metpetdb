package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import edu.rpi.metpetdb.client.model.properties.Property;

public class StringColumn<RowType> extends
		Column<RowType, String> {


	public StringColumn(Object header, Property<RowType> property) {
		super(header, property);
	}

	@Override
	public String getCellValue(RowType rowValue) {
		return property.get(rowValue) == null ? "----" : property.get(rowValue)
				.toString();
	}

}

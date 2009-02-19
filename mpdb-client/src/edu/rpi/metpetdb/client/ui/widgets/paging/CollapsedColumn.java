package edu.rpi.metpetdb.client.ui.widgets.paging;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;
import edu.rpi.metpetdb.client.ui.widgets.MCollapsedText;

public class CollapsedColumn<RowType extends MObject> extends MultipleColumn<RowType, MCollapsedText>{

	public CollapsedColumn(String title, Property<RowType> property) {
		super(title, property);
	}

	@Override
	public MCollapsedText getCellValue(RowType rowValue) {
		return new MCollapsedText(getCellContents(rowValue));
	}

}

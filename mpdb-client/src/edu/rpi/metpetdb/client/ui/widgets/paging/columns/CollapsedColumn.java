package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;
import edu.rpi.metpetdb.client.ui.widgets.MCollapsedText;

public class CollapsedColumn<RowType extends MObject> extends MultipleColumn<RowType, MCollapsedText>{
	
	public enum TruncateMethod {
		CHAR_COUNT,
		ITEM_COUNT
	};
	
	private int truncLength = 20;
	private TruncateMethod method = TruncateMethod.CHAR_COUNT;
	
	public CollapsedColumn(String title, Property<RowType> property) {
		super(title, property);
	}

	@Override
	public MCollapsedText getCellValue(RowType rowValue) {
		if (method == TruncateMethod.CHAR_COUNT)
			return new MCollapsedText(getCellContents(rowValue), truncLength);
		else
			return new MCollapsedText(getCellContents(rowValue), getItemsLength(rowValue,truncLength));
	}
	
	public void setTruncateOptions(TruncateMethod m, int len) {
		method = m;
		truncLength = len;
	}

}

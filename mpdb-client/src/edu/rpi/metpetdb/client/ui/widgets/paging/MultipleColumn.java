package edu.rpi.metpetdb.client.ui.widgets.paging;

import java.util.Collection;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Columns that takes a set of items and puts them into one string
 * @author anthony
 *
 */
public abstract class MultipleColumn<RowType extends MObject, ColType> extends Column<RowType, ColType> {

	public MultipleColumn(String title, Property<RowType> property) {
		super(title, property);
	}
	
	protected String getCellContents(RowType rowValue) {
		final Object val = property.get(rowValue);
		if (val instanceof Collection) {
			final Collection<Object> vals = (Collection<Object>) val;
			String text = "";
			if (vals != null && vals.size() > 0) {
				for(Object o : vals) {
					text += o.toString() + ", ";
				}
				text = text.substring(0,text.length() - 2);
			} else {
				text = "-----";
			}
			return text;
		} else {
			return "-----";
		}
	}
}

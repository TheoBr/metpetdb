package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Columns that takes a set of items and puts them into one string
 * @author anthony
 *
 */
public abstract class MultipleColumn<RowType extends MObject, ColType> extends Column<RowType, ColType> {

	public MultipleColumn(String header, Property<RowType> property) {
		super(header, property);
	}
	
	protected String getCellContents(RowType rowValue) {
		final Object val = property.get(rowValue);
		if (val instanceof Collection) {
			final Collection<Object> vals = (Collection<Object>) val;
			String text = "";
			if (vals != null && vals.size() > 0) {
				//sort collection first
				Set sortedSet = new TreeSet(vals);
				
				for(Object o : sortedSet) {
					text += o.toString() + ", ";
				}
				text = text.substring(0,text.length() - 2);
			} else {
				text = "";
			}
			return text;
		} else {
			return "";
		}
	}
	
	protected int getItemsLength(RowType rowValue, int num) {
		int len = 0;
		final Object val = property.get(rowValue);
		if (val instanceof Collection) {
			final Collection<Object> vals = (Collection<Object>) val;
			if (vals != null && vals.size() > 0) {
				int i = 0;
				for(Object o : vals) {
					if (i++ < num) {
						len += o.toString().length() + 2;
					} else {
						break;
					}
				}
				len -= 2;
			}
		}
		return len;
	}
}

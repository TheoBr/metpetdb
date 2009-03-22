package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.commands.MCommand;

/**
 * Same as list box attribute, but it expects it's values to be sent after an
 * async call has come back
 * 
 * @author anthony
 * 
 * @param <ValueType>
 */
public class AsyncListboxAttribute<ValueType> extends ListboxAttribute {

	private Collection<ValueType> values;
	private final FlowPanel fp;

	public AsyncListboxAttribute(PropertyConstraint pc) {
		super(pc);
		fp = new FlowPanel();
	}
	
	public AsyncListboxAttribute(PropertyConstraint pc, MCommand<Object> o) {
		super(pc,o);
		fp = new FlowPanel();
	}

	public void setValues(Collection<ValueType> values) {
		this.values = values;
		fp.clear();
		fp.add(createListBox(values, "", ""));
	}
	
	public FlowPanel getContainer() {
		return fp;
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute attr) {
		if (values == null) {
			final Widget listBox;
			final ArrayList<String> temp = new ArrayList<String>();
			temp.add("...");
			listBox = createListBox(temp, id, "");
			fp.clear();
			fp.add(listBox);
		}

		return new Widget[] {
			fp
		};
	}
	
	protected Object get(final Widget editWidget) {
		if (editWidget instanceof ListBox) {
			return super.get(editWidget);
		} else {
			final ListBox widget = (ListBox) ((FlowPanel)editWidget).getWidget(0);
			final String v = widget.getValue(widget
					.getSelectedIndex());
			final Object o = objects.get(v);
			return o;
		}
	}
}

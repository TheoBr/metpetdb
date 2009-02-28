package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class ListboxAttribute extends GenericAttribute {

	private ServerOp notifier;

	private Map<String, Object> objects = new HashMap<String, Object>();

	public ListboxAttribute(final PropertyConstraint pc) {
		super(pc);
	}

	public ListboxAttribute(final PropertyConstraint pc, final ServerOp r) {
		super(pc);
		notifier = r;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new MText(get(obj))
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute attr) {
		if (attr.getConstraint() instanceof HasValues) {
			String selectedValue = get(obj);
			return new Widget[] {
				createListBox(((HasValues) attr.getConstraint()).getValues(),
						id, selectedValue)
			};
		} else {
			throw new RuntimeException("Wrong Attribute for a list box");
		}
	}
	private Widget createListBox(final Collection<?> items, final String id,
			final String selected) {
		final ListBox lb = new ListBox();
		DOM.setElementAttribute(lb.getElement(), "id", id);

		lb.setVisibleItemCount(1);

		final Iterator<?> itr = items.iterator();
		int index = 0;
		while (itr.hasNext()) {
			final Object o = itr.next();
			objects.put(o.toString(), o);
			lb.addItem(o.toString(), o.toString());
			if (o.toString().equals(selected)) {
				lb.setItemSelected(index, true);
			}
			++index;
		}

		if (lb.getItemCount() > 0)
			lb.setItemSelected(lb.getSelectedIndex(), true);

		lb.addChangeListener(new ChangeListener() {
			public void onChange(final Widget w) {
				if (notifier != null)
					notifier.onSuccess(get(lb));
			}
		});

		applyStyle(lb, true);
		return lb;
	}

	protected Object get(final Widget editWidget) {
		final String v = ((ListBox) editWidget).getValue(((ListBox) editWidget)
				.getSelectedIndex());
		final Object o = objects.get(v);
		return o;
	}

	protected String get(final MObject obj) {
		final Object v = mGet(obj);
		return v != null ? v.toString() : "";
	}

	protected void set(final MObject obj, final Object v) {
		mSet(obj, v);
	}
}

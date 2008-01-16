package edu.rpi.metpetdb.client.ui.input.attributes;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.InvalidPropertyException;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.IHasListItems;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public class ListboxAttribute extends GenericAttribute {
	
	private ServerOp notifier;

	public ListboxAttribute(final PropertyConstraint pc) {
		super(pc);
	}
	
	public ListboxAttribute(final PropertyConstraint pc, final ServerOp r) {
		super(pc);
		notifier = r;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[]{new MText(get(obj))};
	}

	public Widget[] createEditWidget(final MObject obj, final String id,
			final GenericAttribute attr) {
		if (attr instanceof ListboxAttribute) {
			String selectedValue = get(obj);			
			return new Widget[]{createListBox(((IHasListItems) attr
					.getConstraint()).getListItems(), id, selectedValue)};
		} else {
			throw new InvalidPropertyException(attr.getConstraint().propertyId);
		}
	}

	private Widget createListBox(final String[] items, final String id, final String selected) {
		final ListBox lb = new ListBox();
		DOM.setElementAttribute(lb.getElement(), "id", id);

		lb.setVisibleItemCount(1);

		for (int i = 0; i < items.length; i++)
		{
			lb.addItem(items[i], items[i]);
			if(items[i].equals(selected))
			{
				lb.setItemSelected(i, true);
			}
		}

		lb.setItemSelected(lb.getSelectedIndex(), true);
		
		lb.addChangeListener(new ChangeListener() {
			public void onChange(final Widget w) {
				if (notifier != null)
					notifier.onSuccess(lb.getItemText(lb.getSelectedIndex()));
			}
		});

		applyStyle(lb, true);
		return lb;
	}

	protected Object get(final Widget editWidget) {
		final String v = ((ListBox) editWidget).getValue(((ListBox) editWidget)
				.getSelectedIndex());
		return v != null && v.length() > 0 ? v : null;
	}

	protected String get(final MObject obj) {
		final String v = (String) mGet(obj);
		return v != null ? v : "";
	}

	protected void set(final MObject obj, final Object v) {
		mSet(obj, v != null && ((String) v).length() > 0 ? v : null);
	}
}
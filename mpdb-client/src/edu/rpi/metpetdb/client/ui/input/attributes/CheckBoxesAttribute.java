package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

/* TODO make generic, i.e. CheckBoxesAttribute<T>, where T is the type of object that the checkbox is representing */
public class CheckBoxesAttribute extends GenericAttribute {
	private MUnorderedList editList;
	/* To keep track of the physical object that is attached to the checkbox */
	private Map<CheckBox, Object> items;

	public CheckBoxesAttribute(final PropertyConstraint sc) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		final MUnorderedList list = new MUnorderedList();

		final Set<?> s = get(obj);
		if (s != null) {
			final Iterator<?> itr = s.iterator();
			while (itr.hasNext()) {
				final Label r = new Label(itr.next().toString());
				list.add(r);
			}
		}
		return new Widget[] {
			list
		};
	}

	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		editList = new MUnorderedList();

		items.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set<?> chosenItems = get(obj);
		final Collection<?> availableItems = ((HasValues) this.getConstraint())
				.getValues();
		if (availableItems != null) {
			final Iterator<?> iter = availableItems.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				// add all standard check boxes
				editList.add(this.createCheckBoxes(object.toString(),
						chosenItems.contains(object), object));
			}
		}

		return new Widget[] {
			editList
		};
	}

	public HorizontalPanel createCheckBoxes(final String s,
			final boolean chosen, final Object value) {
		final HorizontalPanel hp = new HorizontalPanel();
		final CheckBox rCheck = new CheckBox();
		if (s != null) {
			rCheck.setText(s);
		}

		rCheck.setChecked(chosen);

		items.put(rCheck, value);

		hp.add(rCheck);

		return hp;
	}

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}

	public Set<?> get(final MObjectDTO obj) {
		return (Set<?>) obj.mGet(this.getConstraint().property);
	}

	protected Object get(Widget editWidget) throws ValidationException {
		final Iterator<CheckBox> itr = items.keySet().iterator();
		final Set<Object> chosenItems = new HashSet<Object>();
		while (itr.hasNext()) {
			final CheckBox cb = itr.next();
			if (cb.isChecked())
				chosenItems.add(items.get(cb));
		}
		return chosenItems;
	}
}

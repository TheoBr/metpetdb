package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

/* TODO make generic, i.e. CheckBoxesAttribute<T>, where T is the type of object that the checkbox is representing */
public class CheckBoxesAttribute extends GenericAttribute implements
		ClickListener {
	private int cols;
	private FixedWidthFlexTable editList;
	/* To keep track of the physical object that is attached to the checkbox */
	private Map<CheckBox, Object> items;

	public CheckBoxesAttribute(final PropertyConstraint sc) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
		cols = 1;
	}

	public CheckBoxesAttribute(final PropertyConstraint sc, final int cols) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
		this.cols = cols;
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
		editList = new FixedWidthFlexTable();
		// editList.setWidth("100%");

		items.clear();
		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set<?> chosenItems = get(obj);
		final Collection<?> availableItems = ((HasValues) this.getConstraint())
				.getValues();
		if (availableItems != null) {
			final Iterator<?> iter = availableItems.iterator();
			final int numRows = (availableItems.size() / cols
					+ availableItems.size() % cols) - 1;
			int row = 0;
			int column = 0;
			while (iter.hasNext()) {
				if (row >= numRows) {
					row = 0;
					column++;
				}
				final Object object = iter.next();
				// add all standard check boxes
				editList.setWidget(row, column, this.createCheckBoxes(object
						.toString(), chosenItems.contains(object), object));
				row++;
			}
		}

		return new Widget[] {
			editList
		};
	}

	public CheckBox createCheckBoxes(final String s, final boolean chosen,
			final Object value) {
		final HorizontalPanel hp = new HorizontalPanel();
		final CheckBox rCheck = new CheckBox();
		rCheck.addClickListener(this);
		if (s != null) {
			rCheck.setText(s);
		}

		rCheck.setChecked(chosen);

		items.put(rCheck, value);

		hp.add(rCheck);

		return rCheck;
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

package edu.rpi.metpetdb.client.ui.input.attributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleCommentDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public class CheckBoxesAttribute extends GenericAttribute {
	private MUnorderedList editList;
	protected final ArrayList<Widget> realEditWidgets;

	public CheckBoxesAttribute(final ObjectConstraint sc) {
		super(sc);
		realEditWidgets = new ArrayList<Widget>();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		final MUnorderedList list = new MUnorderedList();

		final Set<CheckBox> s = get(obj);
		if (s != null) {
			final Iterator<CheckBox> itr = s.iterator();
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

		realEditWidgets.clear();

		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set<CheckBox> s = get(obj);
		if (s != null) {
			final Iterator<CheckBox> iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				// add all standard check boxes
				editList.add(this.createCheckBoxes(object.toString()));
			}
		}

		// change to add all standard check boxes
		editList.add(createCheckBoxes(null));

		return new Widget[] {
			editList
		};
	}

	public HorizontalPanel createCheckBoxes(String s) {
		final HorizontalPanel hp = new HorizontalPanel();
		final CheckBox rCheck = new CheckBox();
		if (s != null) {
			rCheck.setText(s);
		}

		realEditWidgets.add(rCheck);

		hp.add(rCheck);

		return hp;
	}

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}

	public Set<CheckBox> get(MObjectDTO obj) {
		return (Set<CheckBox>) obj.mGet(this.getConstraint().property);
	}

	protected Object get(Widget editWidget) throws ValidationException {
		final HashSet items = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final SampleCommentDTO m = new SampleCommentDTO();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				m.setText(name);
				items.add(m);
			}
		}
		return items;
	}
}

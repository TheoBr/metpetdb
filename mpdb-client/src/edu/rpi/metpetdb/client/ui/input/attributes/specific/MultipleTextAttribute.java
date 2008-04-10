package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MUnorderedList;

public abstract class MultipleTextAttribute<T> extends GenericAttribute
		implements ClickListener {

	private MUnorderedList editList;
	protected final ArrayList<Widget> realEditWidgets;

	public MultipleTextAttribute(final StringConstraint sc) {
		super(sc);
		realEditWidgets = new ArrayList<Widget>();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		final MUnorderedList list = new MUnorderedList();

		final Set<T> s = get(obj);
		if (s != null) {
			final Iterator<T> itr = s.iterator();
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

		final Set<T> s = get(obj);
		if (s != null) {
			final Iterator<T> iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				editList.add(MultipleTextAttribute.this
						.createOptionalTextBox(object.toString()));
			}
		}

		editList.add(createOptionalTextBox(null));

		return new Widget[] {
			editList
		};
	}

	public HorizontalPanel createOptionalTextBox(String s) {
		final HorizontalPanel hp = new HorizontalPanel();
		final TextBox rText = new TextBox();
		if (s != null)
			rText.setText(s);
		final Button addButton = new Button("Add", new ClickListener() {
			public void onClick(final Widget sender) {
				editList.add(MultipleTextAttribute.this
						.createOptionalTextBox(null));

			}
		});
		final Button removeButton = new Button("Remove", new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...
				if (realEditWidgets.size() > 1) {
					// remove one
					editList.remove(hp);
					realEditWidgets.remove(rText);
				}
			}
		});

		realEditWidgets.add(rText);

		hp.add(rText);
		hp.add(addButton);
		hp.add(removeButton);

		return hp;
	}

	public abstract Set<T> get(final MObjectDTO obj);

	protected abstract Object get(final Widget editWidget)
			throws ValidationException;

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}
}

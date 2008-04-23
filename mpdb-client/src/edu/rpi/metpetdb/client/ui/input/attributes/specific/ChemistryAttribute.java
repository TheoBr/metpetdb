package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class ChemistryAttribute extends GenericAttribute implements
		ClickListener {
	private final ArrayList<Widget> realEditWidgets;

	public ChemistryAttribute(final ObjectConstraint elements,
			final ObjectConstraint oxides) {
		super(new PropertyConstraint[] {
				elements, oxides
		});
		realEditWidgets = new ArrayList<Widget>();
	}

	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[] {
			new Label("...")
		};
	}
	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		final ListBox species = new ListBox();
		species.addItem("Species...", "Species...");
		species.addItem("Element", "Element");
		species.addItem("Oxide", "Oxide");
		species.setVisibleItemCount(1);

		final ListBox type = new ListBox();
		type.addItem("Type...", "Type...");
		type.addItem("Silicate", "Silicate");
		type.addItem("Oxide", "Oxide");
		type.addItem("Carbonate", "Carbonate");
		type.addItem("Phosphate", "Phosphate");
		type.addItem("Other", "Other");
		type.setVisibleItemCount(1);

		return new Widget[] {
			new Label("...")
		};
	}

	protected Object get(final Widget editWidget) throws ValidationException {
		final HashSet references = new HashSet();
		final Iterator itr = realEditWidgets.iterator();
		while (itr.hasNext()) {
			final Object obj = itr.next();
			final ReferenceDTO r = new ReferenceDTO();
			String name = ((HasText) obj).getText();
			if (!name.equals("")) {
				r.setName(name);
				references.add(r);
			}
		}
		return references;
	}

	public Set get(final MObjectDTO obj) {
		return (Set) mGet(obj);
	}

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {

	}
}

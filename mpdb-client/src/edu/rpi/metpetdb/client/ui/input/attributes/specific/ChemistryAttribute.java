package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralTypeDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class ChemistryAttribute extends GenericAttribute implements
		ClickListener, ChangeListener {
	private final ArrayList<Widget> realEditWidgets;
	private Button add;
	private ListBox species;
	private ListBox type;
	private ListBox choice;

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
		species = new ListBox();
		species.addItem("Species...");
		species.addItem("Element");
		species.addItem("Oxide");
		species.setVisibleItemCount(1);

		type = new ListBox();
		type.addItem("Type...");
		type.addItem("Silicates");
		type.addItem("Oxides");
		type.addItem("Carbonates");
		type.addItem("Phosphates");
		type.addItem("Other");
		type.setVisibleItemCount(1);

		choice = new ListBox();

		species.addChangeListener(this);
		type.addChangeListener(this);

		choice.addChangeListener(this);

		final HorizontalPanel hp = new HorizontalPanel();

		add = new Button("Add", this);

		hp.add(species);
		hp.add(type);
		hp.add(choice);
		hp.add(add);

		return new Widget[] {
			hp
		};
	}
	private Collection<OxideDTO> getOxidesOfMineralType(final String mineralType) {
		final Collection<OxideDTO> oxidesOfMineralType = new ArrayList<OxideDTO>();
		final ObjectConstraint oxideConstraint = (ObjectConstraint) this.constraints[1];
		final Collection<?> oxides = oxideConstraint
				.getValueInCollectionConstraint().getValues();
		final Iterator<?> itr = oxides.iterator();
		while (itr.hasNext()) {
			final OxideDTO oxide = (OxideDTO) itr.next();
			if (contains(oxide.getMineralTypes(), mineralType))
				oxidesOfMineralType.add(oxide);
		}
		return oxidesOfMineralType;
	}

	private Collection<ElementDTO> getElementsOfMineralType(
			final String mineralType) {
		final Collection<ElementDTO> elementsOfMineralType = new ArrayList<ElementDTO>();
		final ObjectConstraint elementConstraint = (ObjectConstraint) this.constraints[0];
		final Collection<?> elements = elementConstraint
				.getValueInCollectionConstraint().getValues();
		final Iterator<?> itr = elements.iterator();
		while (itr.hasNext()) {
			final ElementDTO oxide = (ElementDTO) itr.next();
			if (contains(oxide.getMineralTypes(), mineralType))
				elementsOfMineralType.add(oxide);
		}
		return elementsOfMineralType;
	}

	private boolean contains(final Set<MineralTypeDTO> mineralTypes,
			final String mineralType) {
		final Iterator<MineralTypeDTO> itr = mineralTypes.iterator();
		while (itr.hasNext()) {
			final MineralTypeDTO mt = itr.next();
			if (mt.getName().equals(mineralType))
				return true;
		}
		return false;
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
		if (sender == add) {
			Window.alert("You want to add "
					+ species.getItemText(species.getSelectedIndex())
					+ " of type " + type.getItemText(type.getSelectedIndex())
					+ " for choice "
					+ choice.getItemText(choice.getSelectedIndex()));
		}
	}

	public void onChange(final Widget sender) {
		if (sender == type || sender == species) {
			choice.clear();
			final String selectedMineralType = type.getItemText(type
					.getSelectedIndex());
			final String selectedSpecies = species.getItemText(species
					.getSelectedIndex());
			final Collection<?> itemsToAdd;
			if ("Element".equals(selectedSpecies)) {
				itemsToAdd = getElementsOfMineralType(selectedMineralType);
			} else if ("Oxide".equals(selectedSpecies)) {
				itemsToAdd = getOxidesOfMineralType(selectedMineralType);
			} else {
				itemsToAdd = new ArrayList<Object>();
			}
			final Iterator<?> itr = itemsToAdd.iterator();
			while (itr.hasNext()) {
				choice.addItem(itr.next().toString());
			}
			if (!itemsToAdd.isEmpty())
				choice.addItem("Custom...");
		} else if (sender == choice) {
			final String selectedChoice = choice.getItemText(choice
					.getSelectedIndex());
			if ("Custom...".equals(selectedChoice)) {
				final String selectedSpecies = species.getItemText(species
						.getSelectedIndex());
				final PopupPanel p = new PopupPanel(true);
				final HorizontalPanel hp = new HorizontalPanel();
				final ListBox customChoice = new ListBox();
				hp.add(new Label("Please choose a custom choice"));
				hp.add(customChoice);
				p.setWidget(hp);
				final Collection<?> itemsToAdd;
				if ("Element".equals(selectedSpecies)) {
					itemsToAdd = ((ObjectConstraint) ChemistryAttribute.this.constraints[0])
							.getValueInCollectionConstraint().getValues();
				} else if ("Oxide".equals(selectedSpecies)) {
					itemsToAdd = ((ObjectConstraint) ChemistryAttribute.this.constraints[1])
							.getValueInCollectionConstraint().getValues();

				} else {
					itemsToAdd = new ArrayList<Object>();
				}
				final Iterator<?> itr = itemsToAdd.iterator();
				while (itr.hasNext()) {
					customChoice.addItem(itr.next().toString());
				}

				customChoice.addChangeListener(new ChangeListener() {

					public void onChange(Widget sender) {
						p.hide();
						choice.addItem(customChoice.getItemText(customChoice
								.getSelectedIndex()));
						choice.setSelectedIndex(choice.getItemCount() - 1);
					}

				});

				DOM.setStyleAttribute(p.getElement(), "backgroundColor",
						"orange");

				DOM.setStyleAttribute(p.getElement(), "border",
						"solid 2px black");

				p.show();
				p.setPopupPositionAndShow(new PositionCallback() {

					public void setPosition(int offsetWidth, int offsetHeight) {
						p.setPopupPosition(sender.getAbsoluteLeft(), sender
								.getAbsoluteTop());

					}

				});
			}

		}

	}
}

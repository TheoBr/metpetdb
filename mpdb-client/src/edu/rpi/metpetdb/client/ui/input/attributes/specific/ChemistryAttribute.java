package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.MineralType;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.TextAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

public class ChemistryAttribute extends GenericAttribute implements
		ClickListener, ChangeListener {
	private static float defaultErr = .02F;
	private final ArrayList<Widget> realEditWidgets;

	private Button add;
	private ListBox species;
	private ListBox type;
	private ListBox choice;
	private FlexTable ft;
	private int rows;
	private ObjectConstraint oxideConstraint;
	private ObjectConstraint elementConstraint;

	// keeps track of what species type we have in each row
	private ArrayList<String> species_type;
	// keeps track of whether the species selected is an element or oxide
	private ArrayList<String> element_or_oxide_id;

	private Label no_chemistry_label;

	public ChemistryAttribute(final ObjectConstraint elements,
			final ObjectConstraint oxides) {
		super(new PropertyConstraint[] {
				elements, oxides
		});
		realEditWidgets = new ArrayList<Widget>();
		oxideConstraint = oxides;
		elementConstraint = elements;
	}

	public Widget[] createDisplayWidget(final MObject obj) {

		Collection<ChemicalAnalysisElement> elements = (Collection<ChemicalAnalysisElement>) obj
				.mGet(elementConstraint.property);
		Collection<ChemicalAnalysisOxide> oxides = (Collection<ChemicalAnalysisOxide>) obj
				.mGet(oxideConstraint.property);

		String elementDisplay = "";
		if (elements != null) {
			String elementWT = "";
			String elementPPM = "";
			elementDisplay += "<table>";
			final Iterator<ChemicalAnalysisElement> iter = elements.iterator();
			while (iter.hasNext()) {
				final ChemicalAnalysisElement element = iter.next();
				if (element.getMeasurementUnit().equalsIgnoreCase("ppm")) {
					if (elementPPM.length() == 0)
						//add in header
						elementPPM = "<tr class=\\\"\" + CSS.TYPE_SMALL_CAPS\n" + 
								"					+ \"\\\"><th>ppm</th><th>Element</th></tr>";
					elementPPM += "<tr><td class=\"ppm\">" + element.getAmount()
					+ "</td><td>" + element.getDisplayName()
					+ "</td></tr>";
				} else {
					if (elementWT.length() == 0)
						//add in header
						elementWT = "<tr class=\\\"\" + CSS.TYPE_SMALL_CAPS\n" + 
								"					+ \"\\\"><th>wt%</th><th>Element</th></tr>";
					elementWT += "<tr><td class=\"wt\">" + element.getAmount()
					+ "%</td><td>" + element.getDisplayName()
					+ "</td></tr>";
				}
				if (elementWT.length() > 0)
					elementDisplay += elementWT;
				if (elementPPM.length() > 0)
					elementDisplay += elementPPM;
			}
			elementDisplay += "</table>";
		}

		String oxideDisplay = "";
		if (oxides != null) {
			String oxidePPM = "";
			String oxideWT = "";
			oxideDisplay += "<table>";
			final Iterator<ChemicalAnalysisOxide> iter = oxides.iterator();
			while (iter.hasNext()) {
				final ChemicalAnalysisOxide oxide = iter.next();
				if (oxide.getMeasurementUnit().equalsIgnoreCase("ppm")) {
					if (oxidePPM.length() == 0)
						// add in the header row
						oxidePPM = "<tr class=\\\"\"+ CSS.TYPE_SMALL_CAPS +\"\\\"><th>ppm</th><th>Oxide</th></tr>";
					oxidePPM += "<tr><td class=\"ppm\">" + oxide.getAmount()
							+ "</td><td>" + oxide.getDisplayName()
							+ "</td></tr>";
				} else {
					if (oxideWT.length() == 0)
						// add in the header row
						oxideWT = "<tr class=\\\"\"+ CSS.TYPE_SMALL_CAPS +\"\\\"><th>wt%</th><th>Oxide</th></tr>";
					oxideWT += "<tr><td class=\"wt\">" + oxide.getAmount()
							+ "%</td><td>" + oxide.getDisplayName()
							+ "</td></tr>";
				}
			}
			if (oxideWT.length() > 0)
				oxideDisplay += oxideWT;
			if (oxidePPM.length() > 0)
				oxideDisplay += oxidePPM;
			oxideDisplay += "</table>";
		}

		MTwoColPanel panel = new MTwoColPanel();
		panel.addStyleName("elements-oxides");

		if (elementDisplay.length() == 0) {
			panel.getLeftCol().add(new HTML(oxideDisplay));
			panel.getRightCol().add(new HTML(elementDisplay));
		} else {
			panel.getLeftCol().add(new HTML(elementDisplay));
			panel.getRightCol().add(new HTML(oxideDisplay));
		}

		return new Widget[] {
			panel
		};
	}

	public Widget[] createEditWidget(final MObject obj, final String id) {
		ChemicalAnalysis ca = (ChemicalAnalysis) obj;

		species_type = new ArrayList<String>();
		element_or_oxide_id = new ArrayList<String>();
		no_chemistry_label = new Label("No chemical species specified yet.");
		rows = 2;

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

		ft = new FlexTable();
		add = new Button("Add", this);
		add.setSize("40px", "25px");

		final Label label_add = new Label("Add:");
		final Label label_amount = new Label("Amount");
		final Label label_precision = new Label("Precision");

		label_add.addStyleName("bold");
		label_add.addStyleName("gray");
		label_amount.addStyleName("bold");
		label_amount.addStyleName("brown");
		label_precision.addStyleName("bold");
		label_precision.addStyleName("brown");

		ft.setWidget(0, 0, label_add);
		ft.setWidget(0, 1, species);
		ft.setWidget(0, 3, type);
		ft.setWidget(0, 5, choice);
		ft.setWidget(0, 7, add);
		ft.setWidget(1, 0, label_amount);
		ft.setWidget(1, 1, label_precision);
		format_top_two_rows();
		no_chemistry_row();

		final Iterator<ChemicalAnalysisElement> itr = ca.getElements()
				.iterator();
		ArrayList<String> units = new ArrayList<String>(ChemicalAnalysis.getMeasurementUnits());
		while (itr.hasNext()) {
			final ChemicalAnalysisElement element = (ChemicalAnalysisElement) itr
					.next();
			add_row("Element", String.valueOf(element.getElement().getId()));
			((TextBox) ft.getWidget(rows - 1, 1)).setText(String
					.valueOf(element.getAmount()));
			((HTML) ft.getWidget(rows - 1, 3)).setText(element.getElement()
					.getName());
			((TextBox) ft.getWidget(rows - 1, 4)).setText(String
					.valueOf(element.getPrecision()));
			if (element.getPrecisionUnit().equalsIgnoreCase("ABS"))
				((ListBox) ft.getWidget(rows - 1, 5)).setSelectedIndex(0);
			else
				((ListBox) ft.getWidget(rows - 1, 5)).setSelectedIndex(1);
			((ListBox) ft.getWidget(rows - 1, 2)).setSelectedIndex(units
					.indexOf(element.getMeasurementUnit().toLowerCase()));
		}

		final Iterator<ChemicalAnalysisOxide> itr2 = ca.getOxides().iterator();
		while (itr2.hasNext()) {
			final ChemicalAnalysisOxide oxide = (ChemicalAnalysisOxide) itr2
					.next();
			add_row("Oxide", String.valueOf(oxide.getOxide().getOxideId()));
			((TextBox) ft.getWidget(rows - 1, 1)).setText(String.valueOf(oxide
					.getAmount()));
			((HTML) ft.getWidget(rows - 1, 3)).setText(oxide.getOxide()
					.getSpecies());
			((TextBox) ft.getWidget(rows - 1, 4)).setText(String.valueOf(oxide
					.getPrecision()));
			if (oxide.getPrecisionUnit() == null
					|| oxide.getPrecisionUnit().equalsIgnoreCase("ABS"))
				((ListBox) ft.getWidget(rows - 1, 5)).setSelectedIndex(0);
			else
				((ListBox) ft.getWidget(rows - 1, 5)).setSelectedIndex(1);
			if (oxide.getMeasurementUnit() != null)
				((ListBox) ft.getWidget(rows - 1, 2)).setSelectedIndex(units
						.indexOf(oxide.getMeasurementUnit().toLowerCase()));
		}

		return new Widget[] {
				ft, ft
		};
	}

	/**
	 * If there are no elements/oxides added to the widget, display this label
	 */
	private void no_chemistry_row() {
		if (rows == 2) {
			ft.insertRow(2);
			ft.setWidget(2, 0, no_chemistry_label);
			format_no_chemical_analysis_row();
			rows++;
		} else if (rows > 2 && ft.getWidget(2, 0) == no_chemistry_label) {
			ft.removeRow(2);
			rows--;
		}
	}

	private Collection<Oxide> getOxidesOfMineralType(final String mineralType) {
		final Collection<Oxide> oxidesOfMineralType = new ArrayList<Oxide>();
		final ObjectConstraint oxideConstraint = (ObjectConstraint) this.constraints[1];
		final Collection<?> oxides = oxideConstraint
				.getValueInCollectionConstraint().getValues();
		final Iterator<?> itr = oxides.iterator();
		while (itr.hasNext()) {
			final Oxide oxide = (Oxide) itr.next();
			if (contains(oxide.getMineralTypes(), mineralType))
				oxidesOfMineralType.add(oxide);
		}
		return oxidesOfMineralType;
	}

	private Collection<Element> getElementsOfMineralType(
			final String mineralType) {
		final Collection<Element> elementsOfMineralType = new ArrayList<Element>();
		final ObjectConstraint elementConstraint = (ObjectConstraint) this.constraints[0];
		final Collection<?> elements = elementConstraint
				.getValueInCollectionConstraint().getValues();
		final Iterator<?> itr = elements.iterator();
		while (itr.hasNext()) {
			final Element element = (Element) itr.next();
			if (contains(element.getMineralTypes(), mineralType))
				elementsOfMineralType.add(element);
		}
		return elementsOfMineralType;
	}

	/**
	 * Given an Element's ID, get the corresponding Oxide
	 * 
	 * @param id
	 * @return
	 */
	private Element getElementwithID(final String id) {
		Element elementwithID = new Element();
		final ObjectConstraint elementConstraint = (ObjectConstraint) this.constraints[0];
		Collection<?> elements = elementConstraint
				.getValueInCollectionConstraint().getValues();
		final Iterator<?> itr = elements.iterator();
		while (itr.hasNext()) {
			final Element element = (Element) itr.next();
			if (String.valueOf(element.getId()).equals(id))
				elementwithID = element;
		}
		return elementwithID;
	}

	/**
	 * Given an Oxide's ID, get the corresponding Oxide
	 * 
	 * @param id
	 * @return
	 */
	private Oxide getOxidewithID(final String id) {
		Oxide oxidewithID = new Oxide();
		final ObjectConstraint oxideConstraint = (ObjectConstraint) this.constraints[1];
		Collection<?> oxides = oxideConstraint.getValueInCollectionConstraint()
				.getValues();
		final Iterator<?> itr = oxides.iterator();
		while (itr.hasNext()) {
			final Oxide oxide = (Oxide) itr.next();
			if (String.valueOf(oxide.getOxideId()).equals(id))
				oxidewithID = oxide;
		}
		return oxidewithID;
	}

	private boolean contains(final Set<MineralType> mineralTypes,
			final String mineralType) {
		final Iterator<MineralType> itr = mineralTypes.iterator();
		while (itr.hasNext()) {
			final MineralType mt = itr.next();
			if (mt.getName().equals(mineralType))
				return true;
		}
		return false;
	}

	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) throws ValidationException {
		if (constraint == this.constraints[1]) {
			final HashSet<ChemicalAnalysisOxide> Oxides = new HashSet();
			// Add only Oxides
			for (int i = 2; i < element_or_oxide_id.size() + 2; i++) {
				if (species_type.get(i - 2).equals("Oxide")) {
					final ChemicalAnalysisOxide r = new ChemicalAnalysisOxide();
					r.setValues(getOxidewithID(element_or_oxide_id.get(i - 2)),
							Double.valueOf(
									((TextBox) ft.getWidget(i, 1)).getText())
									.doubleValue(), Double.valueOf(
									((TextBox) ft.getWidget(i, 4)).getText())
									.doubleValue(), ((ListBox) ft.getWidget(i,
									2)).getItemText(
									((ListBox) ft.getWidget(i, 2))
											.getSelectedIndex()).toUpperCase(),
							((ListBox) ft.getWidget(i, 5)).getItemText(
									((ListBox) ft.getWidget(i, 5))
											.getSelectedIndex()).toUpperCase());
					r.setMinMax();
					Oxides.add(r);
				}
			}
			return Oxides;
		} else {
			final HashSet<ChemicalAnalysisElement> Elements = new HashSet();
			// Add only Elements
			for (int i = 2; i < element_or_oxide_id.size() + 2; i++) {
				if (species_type.get(i - 2).equals("Element")) {
					final ChemicalAnalysisElement r = new ChemicalAnalysisElement();
					r.setValues(
							getElementwithID(element_or_oxide_id.get(i - 2)),
							Double.valueOf(
									((TextBox) ft.getWidget(i, 1)).getText())
									.doubleValue(), Double.valueOf(
									((TextBox) ft.getWidget(i, 4)).getText())
									.doubleValue(), ((ListBox) ft.getWidget(i,
									2)).getItemText(
									((ListBox) ft.getWidget(i, 2))
											.getSelectedIndex()).toUpperCase(),
							((ListBox) ft.getWidget(i, 5)).getItemText(
									((ListBox) ft.getWidget(i, 5))
											.getSelectedIndex()).toUpperCase());
					r.setMinMax();
					Elements.add(r);
				}
			}
			return Elements;
		}
	}

	public Set get(final MObject obj) {
		return (Set) mGet(obj);
	}

	protected void set(final MObject obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}

	protected void set(final MObject obj, final Object v) {

	}

	/**
	 * Don't allow users to add chemical analysis for the same element/oxide
	 * 
	 * @return
	 */
	private boolean checkDuplicateAdd() {
		int i = 2;
		if (ft.getWidget(2, 0) == no_chemistry_label)
			i = 3;
		for (; i < rows; i++) {
			//Stripping html tags to get the symbol
			HTML tempMText = new HTML();
			tempMText = (HTML) ft.getWidget(i, 3);
			if (tempMText.getText().equals(
					choice.getItemText(choice.getSelectedIndex())))
				return true;
		}
		return false;
	}

	/**
	 * create new row for user to input amount and precision for selected
	 * element/oxide
	 */
	private void add_row(String element_or_oxide, String id) {
		no_chemistry_row();
		ft.insertRow(rows);
		Button remove = new Button();

		ListBox listbox_precison = new ListBox();
		listbox_precison.addItem("Abs");
		listbox_precison.addItem("Rel");

		ListBox listbox_measurement = new ListBox();
		final Set<String> measurementUnits = ChemicalAnalysis
				.getMeasurementUnits();
		for (String s : measurementUnits)
			listbox_measurement.addItem(s);

		ChemicalAnalysisElement tryme = new ChemicalAnalysisElement();

		TextAttribute amount_input_text = new TextAttribute(
				MpDb.doc.ChemicalAnalysisElement_ChemicalAnalysis_elements_amount);
		amount_input_text.setVisibleLength(5);

		TextAttribute precision_input_text = new TextAttribute(
				MpDb.doc.ChemicalAnalysisElement_ChemicalAnalysis_elements_precision);
		precision_input_text.setVisibleLength(5);

		TextAttribute choice_label = new TextAttribute(
				MpDb.doc.ChemicalAnalysisElement_ChemicalAnalysis_elements_element);
		choice_label.setVisibleLength(5);

		remove.setStyleName("remove");
		remove.setPixelSize(14, 15);
		remove.addClickListener(new ClickListener() {
			public void onClick(final Widget sender1) {
				delete_row(sender1);
			}
		});

		ft.setWidget(rows, 1,
				amount_input_text.createEditWidget(tryme, "test")[0]);
		ft.setWidget(rows, 2, listbox_measurement);
		final HTML temp = (HTML) choice_label.createDisplayWidget(tryme)[0];
		ft.setWidget(rows, 3, temp);
		if (choice.getSelectedIndex() != -1)
			temp.setText(choice.getItemText(choice.getSelectedIndex()));
		ft.setWidget(rows, 4, precision_input_text.createEditWidget(tryme,
				"test")[0]);
		ft.setWidget(rows, 5, listbox_precison);
		ft.setWidget(rows, 6, remove);
		format_analysis_rows();

		species_type.add(element_or_oxide);
		element_or_oxide_id.add(id);

		rows++;
	}

	/**
	 * remove selected row from our widget and our 2 arrays
	 * 
	 * @param sender
	 */
	private void delete_row(final Widget sender) {
		for (int i = 2; i < rows; i++) {
			if (sender == ft.getWidget(i, 6)) {
				ft.removeRow(i);
				species_type.remove(i - 2);
				element_or_oxide_id.remove(i - 2);
				rows--;
				no_chemistry_row();
			}
		}
	}

	public void onClick(final Widget sender) {
		if (sender == add) {
			if (choice.getSelectedIndex() != -1 && choice.isVisible() == true) {
				if (!checkDuplicateAdd()) {
					add_row(species.getValue(species.getSelectedIndex()),
							choice.getValue(choice.getSelectedIndex()));
				}
			}
		}
	}

	public void onChange(final Widget sender) {
		if (sender == type || sender == species) {
			if (type.getSelectedIndex() != 0 && species.getSelectedIndex() != 0) {
				choice.setVisible(true);
			} else
				choice.setVisible(false);
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
				if ("Element".equals(selectedSpecies)) {
					Element temp = (Element) itr.next();
					choice.addItem(temp.getSymbol(), String.valueOf(temp
							.getId()));
				} else if ("Oxide".equals(selectedSpecies)) {
					Oxide temp = (Oxide) itr.next();
					choice.addItem(temp.getSpecies(), String.valueOf(temp
							.getOxideId()));
				}
			}
			if (!itemsToAdd.isEmpty()) {
				choice.addItem("Custom...");
			}
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

	private void format_top_two_rows() {
		ft.getFlexCellFormatter().setColSpan(1, 0, 4);
		ft.getFlexCellFormatter().setColSpan(1, 1, 4);
		ft.getCellFormatter().setWidth(0, 0, "60px");
		ft.getCellFormatter().setWidth(0, 2, "60px");
		ft.getCellFormatter().setWidth(0, 4, "60px");
		ft.getCellFormatter().setWidth(0, 6, "60px");

		for (int i = 0; i < 8; i++)
			ft.getFlexCellFormatter().setAlignment(0, 0,
					HasHorizontalAlignment.ALIGN_LEFT,
					HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getFlexCellFormatter().setAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getWidget(0, 5).setVisible(false);
		ft.getRowFormatter().setStyleName(0, "mpdb-dataTableBlue");
		ft.getRowFormatter().setStyleName(1, "mpdb-dataTablePink");
		ft.getFlexCellFormatter().setHeight(0, 0, "29px");
		ft.getFlexCellFormatter().setHeight(1, 0, "29px");
	}

	private void format_analysis_rows() {
		ft.getCellFormatter().setWidth(rows, 0, "80px");
		ft.getFlexCellFormatter().setAlignment(rows, 1,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_BOTTOM);
		ft.getFlexCellFormatter().setAlignment(rows, 4,
				HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_BOTTOM);
		ft.getFlexCellFormatter().setAlignment(rows, 6,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.getRowFormatter().setStyleName(rows, "mpdb-dataTable");
	}

	private void format_no_chemical_analysis_row() {
		ft.getFlexCellFormatter().setColSpan(2, 0, 8);
		ft.getFlexCellFormatter().setAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}
}

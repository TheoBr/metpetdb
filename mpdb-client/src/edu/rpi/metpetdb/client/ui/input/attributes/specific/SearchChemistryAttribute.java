package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public class SearchChemistryAttribute extends GenericAttribute {
	private FlexTable ft;

	public SearchChemistryAttribute(final ObjectConstraint elements,
			final ObjectConstraint oxides) {
		super(new PropertyConstraint[] {
				elements, oxides
		});
	}
	public Widget[] createDisplayWidget(final MObjectDTO obj) {
		return new Widget[] {
			new Widget()
		};
	}
	public Widget[] createEditWidget(final MObjectDTO obj, final String id) {
		ft = new FlexTable();
		ft.setStyleName("mpdb-dataTable");
		// create header
		createTableHeader();
		// get elements
		final ObjectConstraint elementConstraint = (ObjectConstraint) this.constraints[0];
		final Collection<?> elements = elementConstraint
				.getValueInCollectionConstraint().getValues();
		createRowsElement(elements);
		// get oxides
		final ObjectConstraint oxideConstraint = (ObjectConstraint) this.constraints[1];
		final Collection<?> oxides = oxideConstraint
				.getValueInCollectionConstraint().getValues();
		createRowsOxide(oxides);

		return new Widget[] {
				ft, ft
		};
	}

	private void createTableHeader() {
		ft.getRowFormatter().addStyleName(0, "mpdb-dataTablePink");
		ft.setWidget(0, 0, new Label("Element Range"));
		ft.getFlexCellFormatter().setColSpan(0, 0, 5);
		ft.getFlexCellFormatter().setAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setWidget(0, 1, new Label("Units"));
		ft.getFlexCellFormatter().setColSpan(0, 1, 2);
		ft.setWidget(0, 2, new Label("Oxide Range"));
		ft.getFlexCellFormatter().setColSpan(0, 2, 5);
		ft.getFlexCellFormatter().setAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ft.setWidget(0, 3, new Label("Units"));
		ft.getFlexCellFormatter().setColSpan(0, 3, 2);
	}

	private void createRowsElement(final Collection<?> elements) {
		final Iterator<?> itr = elements.iterator();
		int row = 0;
		while (itr.hasNext()) {
			++row;
			final ElementDTO element = (ElementDTO) itr.next();
			final TextBox lessThan = new TextBox();
			lessThan.setWidth("30px");
			final TextBox greaterThan = new TextBox();
			greaterThan.setWidth("30px");
			final ListBox unit = new ListBox();
			unit.addItem("% wt");
			unit.addItem("ppm");
			final Button set = new Button("Set");
			set.setStyleName("smallBtn");
			ft.setWidget(row, 0, greaterThan);
			ft.setWidget(row, 1, new Label("<"));
			ft.setWidget(row, 2, new Label(element.getSymbol()));
			ft.setWidget(row, 3, new Label("<"));
			ft.setWidget(row, 4, lessThan);
			ft.setWidget(row, 5, unit);
			ft.setWidget(row, 6, set);
			for (int i = 0; i < 7; i++) {
				ft.getFlexCellFormatter().setAlignment(row, i,
						HasHorizontalAlignment.ALIGN_CENTER,
						HasVerticalAlignment.ALIGN_MIDDLE);
			}
		}
	}

	private void createRowsOxide(final Collection<?> oxides) {
		final Iterator<?> itr = oxides.iterator();
		int row = 0;
		while (itr.hasNext()) {
			++row;
			final OxideDTO oxide = (OxideDTO) itr.next();
			final TextBox lessThan = new TextBox();
			lessThan.setWidth("30px");
			final TextBox greaterThan = new TextBox();
			greaterThan.setWidth("30px");
			final ListBox unit = new ListBox();
			unit.addItem("% wt");
			unit.addItem("ppm");
			final Button set = new Button("Set");
			set.setStyleName("smallBtn");
			ft.setWidget(row, 7, greaterThan);
			ft.setWidget(row, 8, new Label("<"));
			ft.setWidget(row, 9, new Label(oxide.getSpecies()));
			ft.setWidget(row, 10, new Label("<"));
			ft.setWidget(row, 11, lessThan);
			ft.setWidget(row, 12, unit);
			ft.setWidget(row, 13, set);
			for (int i = 7; i < 12; i++) {
				ft.getFlexCellFormatter().setAlignment(row, i,
						HasHorizontalAlignment.ALIGN_CENTER,
						HasVerticalAlignment.ALIGN_MIDDLE);
			}
		}
	}

	protected void set(final MObjectDTO obj, final Object o) {
		mSet(obj, o);
	}

	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) throws ValidationException {
		if (constraint == this.constraints[1]) {
			final HashSet<ChemicalAnalysisOxideDTO> Oxides = new HashSet();
			// Add only Oxides

			return Oxides;
		} else {
			final HashSet<ChemicalAnalysisElementDTO> Elements = new HashSet();
			// Add only Elements
			return Elements;
		}
	}
}

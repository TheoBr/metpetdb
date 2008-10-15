package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
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
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;

public class SearchChemistryAttribute extends SearchGenericAttribute {
	private FlexTable ft;

	public SearchChemistryAttribute(final ObjectConstraint elements,
			final ObjectConstraint oxides) {
		super(new PropertyConstraint[] {
				elements, oxides
		});
	}
	public Widget[] createDisplayWidget(final MObject obj) {
		return new Widget[] {
			new Widget()
		};
	}
	public Widget[] createEditWidget(final MObject obj, final String id) {
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
			final Element element = (Element) itr.next();
			final TextBox lessThan = new TextBox();
			lessThan.setWidth("30px");
			final TextBox greaterThan = new TextBox();
			greaterThan.setWidth("30px");
			final ListBox unit = new ListBox();
			unit.addItem("% wt");
			unit.addItem("ppm");
			ft.setWidget(row, 0, greaterThan);
			ft.setWidget(row, 1, new Label("<"));
			ft.setWidget(row, 2, new Label(element.getSymbol()));
			ft.setWidget(row, 3, new Label("<"));
			ft.setWidget(row, 4, lessThan);
			ft.setWidget(row, 5, unit);
			for (int i = 0; i < 6; i++) {
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
			final Oxide oxide = (Oxide) itr.next();
			final TextBox lessThan = new TextBox();
			lessThan.setWidth("30px");
			final TextBox greaterThan = new TextBox();
			greaterThan.setWidth("30px");
			final ListBox unit = new ListBox();
			unit.addItem("% wt");
			unit.addItem("ppm");
			final Button set = new Button("Set");
			set.setStyleName("smallBtn");
			ft.setWidget(row, 6, greaterThan);
			ft.setWidget(row, 7, new Label("<"));
			ft.setWidget(row, 8, new Label(oxide.getSpecies()));
			ft.setWidget(row, 9, new Label("<"));
			ft.setWidget(row, 10, lessThan);
			ft.setWidget(row, 11, unit);
			for (int i = 6; i < 12; i++) {
				ft.getFlexCellFormatter().setAlignment(row, i,
						HasHorizontalAlignment.ALIGN_CENTER,
						HasVerticalAlignment.ALIGN_MIDDLE);
			}
		}
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}
	
	protected void set(final MObject obj, final Object v,
			final PropertyConstraint pc) {
		mSet(obj, v, pc);
	}

	protected Object get(final Widget editWidget,
			final PropertyConstraint constraint) throws ValidationException {
		if (constraint == this.constraints[1]) {
			final HashSet<SearchOxide> Oxides = new HashSet();
			for (int i = 0; i < ft.getRowCount(); i++){
				try {
					String lowerBound = ((TextBox) ft.getWidget(i, 6)).getText();
					String upperBound = ((TextBox) ft.getWidget(i, 10)).getText();
					String oxide = ((Label) ft.getWidget(i, 8)).getText();
					SearchOxide o = new SearchOxide();
					if (!lowerBound.equals(""))
						o.setLowerBound(Float.valueOf(lowerBound));
					if (!upperBound.equals(""))
						o.setUpperBound(Float.valueOf(upperBound));
					if (!lowerBound.equals("") || !upperBound.equals("")){
						o.setSpecies(oxide);	
						Oxides.add(o);
					}
				} catch (Exception e){
					
				}
			}
			return Oxides;
		} else {
			final HashSet<SearchElement> Elements = new HashSet();
			for (int i = 0; i < ft.getRowCount(); i++){
				try{
					String lowerBound = ((TextBox) ft.getWidget(i, 0)).getText();
					String upperBound = ((TextBox) ft.getWidget(i, 4)).getText();
					String element = ((Label) ft.getWidget(i, 2)).getText();
					SearchElement e = new SearchElement();
					if (!lowerBound.equals(""))
						e.setLowerBound(Float.valueOf(lowerBound));
					if (!upperBound.equals(""))
						e.setUpperBound(Float.valueOf(upperBound));
					if (!lowerBound.equals("") || !upperBound.equals("")){
						e.setElementSymbol(element);	
						Elements.add(e);
					}
				} catch (Exception e){
					
				}
			}
			return Elements;
		}
	}

	public void onRemoveCriteria(final Object obj){
//		if (items.get(obj) != null)
//			((MCheckBox) obj).setChecked(false);
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
//		final Iterator<CheckBox> itr = items.keySet().iterator();
//		while (itr.hasNext()) {
//			final CheckBox cb = itr.next();
//			if (cb.isChecked())
//				criteria.add(new Pair(createCritRow("Rock Type:", items.get(cb).toString()), cb));
//		}
		return criteria;
	}
	
}

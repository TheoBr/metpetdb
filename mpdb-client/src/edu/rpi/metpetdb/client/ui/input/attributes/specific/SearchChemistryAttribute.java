package edu.rpi.metpetdb.client.ui.input.attributes.specific;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
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
	private Map<RowContainer,String> currentCriteria = new HashMap<RowContainer,String>();
	
	private class RowContainer extends FlowPanel implements ChangeListener{
		private TextBox lessThan = new TextBox();
		private TextBox greaterThan = new TextBox();
		private ListBox unit = new ListBox();
		final Label less = new Label("<");
		final Label greater = new Label("<");
		final Object elementOrOxide;
		final HTML symbol;
		
		public RowContainer(final Object elementOrOxide){
			this.elementOrOxide = elementOrOxide;
			final Set<String> measurementUnits = ChemicalAnalysis.getMeasurementUnits();
			for (String s : measurementUnits)
				unit.addItem(s);
			
			if (elementOrOxide instanceof Element){
				symbol = new HTML(((Element)elementOrOxide).getSymbol());
			} else {
				symbol = new HTML(((Oxide)elementOrOxide).getDisplayName());
			}
			
			lessThan.setWidth("30px");
			greaterThan.setWidth("30px");
			
			lessThan.addChangeListener(this);
			greaterThan.addChangeListener(this);
			unit.addChangeListener(this);
			
			lessThan.addStyleName("inline");
			greaterThan.addStyleName("inline");
			unit.addStyleName("inline");
			less.addStyleName("inline");
			greater.addStyleName("inline");
			symbol.addStyleName("inline");
			
			add(lessThan);
			add(less);
				
			add(symbol);
			add(greater);
			add(greaterThan);
			add(unit);
		}
		
		public void onChange(final Widget sender){
			String symbol;
			if (elementOrOxide instanceof Element){
				symbol =((Element)elementOrOxide).getSymbol();
			} else {
				symbol = ((Oxide)elementOrOxide).getDisplayName();
			}
			if (currentCriteria.containsKey(this)){
				currentCriteria.remove(this);
				SearchChemistryAttribute.this.getSearchInterface().createCritera();
			}
			if (!lessThan.getText().equals("") && !greaterThan.getText().equals("")){
				currentCriteria.put(this, lessThan.getText() + " < " + symbol + 
						" < " + greaterThan.getText() + " " + unit.getValue(unit.getSelectedIndex()));
				SearchChemistryAttribute.this.getSearchInterface().createCritera();
			}
			
		}
		
	}

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
		final HorizontalPanel header = new HorizontalPanel();
		header.add( new Label("Element Range"));
		header.add( new Label("Units"));
		header.add( new Label("Oxide Range"));
		header.add( new Label("Units"));
		ft.setWidget(0, 0, header);;
		ft.getFlexCellFormatter().setColSpan(0, 0, 2);
	}

	private void createRowsElement(final Collection<?> elements) {
		final Iterator<?> itr = elements.iterator();
		int row = 0;
		while (itr.hasNext()) {
			++row;
			ft.setWidget(row,0,new RowContainer((Element)itr.next()));
		}
	}

	private void createRowsOxide(final Collection<?> oxides) {
		final Iterator<?> itr = oxides.iterator();
		int row = 0;
		while (itr.hasNext()) {
			++row;
			ft.setWidget(row,1,new RowContainer((Oxide)itr.next()));
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
			Iterator<RowContainer> itr = currentCriteria.keySet().iterator();
			while (itr.hasNext()){
				final RowContainer temp = itr.next();
				if (temp.elementOrOxide instanceof Oxide){
					try{
						SearchOxide o = new SearchOxide();
						o.setValues(((Oxide)temp.elementOrOxide).getSpecies(),
								Float.valueOf(temp.lessThan.getText()), 
								Float.valueOf(temp.greaterThan.getText()),
								temp.unit.getValue(temp.unit.getSelectedIndex()));	
						Oxides.add(o);
					}
					catch (Exception e){
						
					}
				}
			}
			return Oxides;
		} else {
			final HashSet<SearchElement> Elements = new HashSet();
			Iterator<RowContainer> itr = currentCriteria.keySet().iterator();
			while (itr.hasNext()){
				final RowContainer temp = itr.next();
				if (temp.elementOrOxide instanceof Oxide){
					try{
						SearchElement e = new SearchElement();
						e.setValues(((Element)temp.elementOrOxide).getSymbol(),
								Float.valueOf(temp.lessThan.getText()), 
								Float.valueOf(temp.greaterThan.getText()),
								temp.unit.getValue(temp.unit.getSelectedIndex()));	
						Elements.add(e);
					}
					catch (Exception e){
						
					}
				}
			}
			return Elements;
		}
	}

	public void onClear(){
		for(RowContainer rc : currentCriteria.keySet()){
			rc.greaterThan.setText("");
			rc.lessThan.setText("");
			rc.unit.setSelectedIndex(0);
		}
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		Iterator<String> itr = currentCriteria.values().iterator();
		String crit = "";
		while (itr.hasNext()){
			crit += itr.next() + "<br>";
		}
		if (crit.length() > 0){
			crit = crit.substring(0, crit.length()-1);
			criteria.add(createCritRow(new HTML(crit)));
		}
		return criteria;
	}
	
}

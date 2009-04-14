package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
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
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MMultiWordSuggestOracle;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public class SearchChemistryAttribute extends SearchGenericAttribute {
	private Map<RowContainer,String> currentCriteria = new HashMap<RowContainer,String>();
	private MHtmlList editList;
	private final static String lessThanEqualToHTML = " &#8804 ";
	protected final ArrayList<Widget> realEditWidgets = new ArrayList<Widget>();
	private static final String STYLENAME = "chem-list";
	
	private static final String[] rangeChoices = {"is between", "is greater than", "is less than", "is equal to" };
	

	private class RowContainer extends FlowPanel implements ChangeListener{
		private ChemMSuggestText st;
		private ListBox range = new ListBox();
		private ListBox unit = new ListBox();
		private TextBox lessThan = new TextBox();
		private TextBox greaterThan = new TextBox();
		private TextBox precision = new TextBox();
		private final MText and = new MText("and","span");
		private final MText percent = new MText("%", "span");
		private final HTML plusminus = new HTML("&#177;");
		
		private class ChemMSuggestText extends MSuggestText{
			public ChemMSuggestText(){
				super(new HashSet<String>(), false, true);
				this.addAllSuggestPopupStyleName("all-suggest-popup-chem");
			}
			
			public ChemMSuggestText(final Set<String> suggestions, final boolean addShowAll){
				super(suggestions,addShowAll, true);
				this.addAllSuggestPopupStyleName("all-suggest-popup-chem");
			}
			protected String getValue(String input) {
				return removeHTML(input);
			}
		}
		
		public RowContainer(){
			final Set<String> measurementUnits = ChemicalAnalysis.getMeasurementUnits();
			for (String s : measurementUnits)
				unit.addItem(s);
			for (String s : rangeChoices)
				range.addItem(s);
			
			final ObjectConstraint elementConstraint = (ObjectConstraint) SearchChemistryAttribute.this.constraints[0];
			final Collection<Element> elements = elementConstraint
					.getValueInCollectionConstraint().getValues();
			
			final ObjectConstraint oxideConstraint = (ObjectConstraint) SearchChemistryAttribute.this.constraints[1];
			final Collection<Oxide> oxides = oxideConstraint
					.getValueInCollectionConstraint().getValues();
			
			final Set<String> options = new HashSet<String>();
			
			for (Element e : elements){
				options.add(e.getSymbol() + "&nbsp;<span>" + e.getName() +"<span>");
			}
			
			for (Oxide o : oxides){
				options.add(o.getDisplayName());
			}
			
			st = new ChemMSuggestText(options, true){
				
			};
			
			st.suggestBox.addEventHandler(new SuggestionHandler(){
				public void onSuggestionSelected(final SuggestionEvent e){
					st.setText(removeHTML(e.getSelectedSuggestion().getReplacementString()));
					onChange(RowContainer.this);
				}
			});
			st.suggestBox.addChangeListener(this);
			st.addPopupStyleName("chem-suggest-popup");
			lessThan.addChangeListener(this);
			greaterThan.addChangeListener(this);
			unit.addChangeListener(this);
			range.addChangeListener(this);
			
			lessThan.addStyleName("number-input");
			greaterThan.addStyleName("number-input");
			precision.addStyleName("number-input");
			
			plusminus.addStyleName("inline");
			setStyleName("chem-item");
			
			add(st);
			add(range);
			add(unit);
//			add(plusminus);
//			add(precision);
//			add(percent);
			
			
			reset(range.getItemText(range.getSelectedIndex()));
		}
		
		public void onChange(final Widget sender){
			reset(range.getItemText(range.getSelectedIndex()));
			String displayString = "";
			if (!st.getText().equals("")){
				Element e = findByElementSymbol(st.getText());
				Oxide o = findByOxideSymbol(st.getText());
				if (e != null){
					displayString = e.getSymbol();
				} else if (o != null){
					displayString = o.getDisplayName();
				}
			}
			if (currentCriteria.containsKey(this)){
				currentCriteria.remove(this);
			}
			if (!displayString.equals("")){
				// between
				if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[0]) && 
						!lessThan.getText().equals("") && !greaterThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is between " + greaterThan.getText() + " and " +  
							lessThan.getText() + " " + unit.getValue(unit.getSelectedIndex()));
				// greater than
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[1]) && 
						!greaterThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is greater than " + greaterThan.getText() + 
							" " + unit.getValue(unit.getSelectedIndex()));
				// less than
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[2]) && 
						!lessThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is less than " + lessThan.getText() + 
							" " + unit.getValue(unit.getSelectedIndex()));
				// equal to
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[3]) && 
						!lessThan.getText().equals("")){
					currentCriteria.put(this, displayString + 
							" is " + lessThan.getText() + " " + unit.getValue(unit.getSelectedIndex()));
				}
			}
			SearchChemistryAttribute.this.getSearchInterface().createCritera();
		}
		
		public void clear(){
			range.setSelectedIndex(0);
			unit.setSelectedIndex(0);
			st.setText("");
			lessThan.setText("");
			greaterThan.setText("");
			precision.setText("");
		}
		
		public void reset(final String rangeChoice){
			remove(greaterThan);
			remove(and);
			remove(lessThan);
			if (rangeChoice.equals(rangeChoices[0])){						
				insert(greaterThan,2);
				insert(and,3);
				insert(lessThan,4);
			} else if (rangeChoice.equals(rangeChoices[1])){
				insert(greaterThan,2);
			} else if (rangeChoice.equals(rangeChoices[2])){
				insert(lessThan,2);
			} else if (rangeChoice.equals(rangeChoices[3])){
				insert(lessThan,2);
			}
		}
		
		public String removeHTML(String input) {
			input = input.replaceAll("<.*?>", "");
			input = input.replaceAll("&nbsp.*", "");
			return input;
		}
		
		public Element findByElementSymbol(final String symbol){
			final ObjectConstraint elementConstraint = (ObjectConstraint) SearchChemistryAttribute.this.constraints[0];
			final Collection<Element> elements = elementConstraint
					.getValueInCollectionConstraint().getValues();
			for(Element e : elements){
				if (e.getSymbol().equalsIgnoreCase(symbol))
					return e;
			}
			return null;
		}
		
		public Oxide findByOxideSymbol(final String symbol){
			final ObjectConstraint oxideConstraint = (ObjectConstraint) SearchChemistryAttribute.this.constraints[1];
			final Collection<Oxide> oxides = oxideConstraint
					.getValueInCollectionConstraint().getValues();
			for(Oxide o : oxides){
				if (o.getSpecies().equalsIgnoreCase(symbol))
					return o;
			}
			return null;
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
		editList = new MHtmlList();
		editList.setStylePrimaryName(STYLENAME);
		DOM.setElementAttribute(editList.getElement(), "id", id);
		realEditWidgets.clear();
		currentCriteria.clear();
		
		final HashMap m = getAll(obj);
		if (m != null) {
			Set<SearchElement> s = (Set<SearchElement>) m.get(this.constraints[0]);
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(object);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				((RowContainer)t.getInputWidget()).onChange(t.getInputWidget());
			}
			Set<SearchOxide> ss = (Set<SearchOxide>) m.get(this.constraints[1]);
			final Iterator iter2 = ss.iterator();
			while (iter2.hasNext()) {
				final Object object = iter2.next();
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(object);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				((RowContainer)t.getInputWidget()).onChange(t.getInputWidget());
			}
		}

		MultipleInputPanel t = createOptionalSuggestBox(null);
		editList.add(t);
		realEditWidgets.add(t.getInputWidget());

		return new Widget[] {
				editList, editList
		};
	}
	
	public MultipleInputPanel createOptionalSuggestBox(final Object s) {
		final MultipleInputPanel panel = new MultipleInputPanel();
		final RowContainer rc = new RowContainer();
		if (s != null){
			if (s instanceof SearchElement){
				SearchElement e = (SearchElement) s;
				rc.st.setText(e.getElementSymbol());
				rc.lessThan.setText(String.valueOf(e.getUpperBound()));
				rc.greaterThan.setText(String.valueOf(e.getLowerBound()));

				if (e.getUpperBound() == Float.MAX_VALUE){
					rc.range.setSelectedIndex(1);
				}
				if (e.getLowerBound() == Float.MIN_VALUE){
					rc.range.setSelectedIndex(2);
				}
				if (e.getUpperBound() == e.getLowerBound()){
					rc.range.setSelectedIndex(3);
				}
				rc.reset(rc.range.getItemText(rc.range.getSelectedIndex()));
			}
			if (s instanceof SearchOxide){
				SearchOxide o = (SearchOxide) s;
				rc.st.setText(o.getSpecies());
				rc.lessThan.setText(String.valueOf(o.getUpperBound()));
				rc.greaterThan.setText(String.valueOf(o.getLowerBound()));

				if (o.getUpperBound() == Float.MAX_VALUE){
					rc.range.setSelectedIndex(1);
				}
				if (o.getLowerBound() == Float.MIN_VALUE){
					rc.range.setSelectedIndex(2);
				}
				if (o.getUpperBound() == o.getLowerBound()){
					rc.range.setSelectedIndex(3);
				}
				rc.reset(rc.range.getItemText(rc.range.getSelectedIndex()));
			}
		}
		
		panel.setInputWidget(rc);
		panel.addButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(null);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				setStyles();
			}
		});
		panel.removeButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				// If there are more than one entry spaces...
				if (realEditWidgets.size() > 1) {
					// remove one
					editList.remove(panel);
					realEditWidgets.remove(rc);
				}
				setStyles();
			}
		});
		return panel;
	}
	
	public HashMap getAll(final MObject obj) {
		return mGetAll(obj);
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
				final Oxide oo = temp.findByOxideSymbol(temp.st.getText());
				if (oo != null){
					try{
						SearchOxide o = new SearchOxide();
						Float lt = Float.MAX_VALUE;
						Float gt = Float.MIN_VALUE;
						if (!temp.lessThan.getText().equals("")){
							lt = Float.valueOf(temp.lessThan.getText());
						}
						if (!temp.greaterThan.getText().equals("")){
							gt = Float.valueOf(temp.greaterThan.getText());
						}
						if (temp.range.getItemText(temp.range.getSelectedIndex()).equals(rangeChoices[3])){
							gt = lt;
						}
						o.setValues(oo.getSpecies(),
								gt, 
								lt,
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
				final Element ee = temp.findByElementSymbol(temp.st.getText());
				if (ee != null){
					try{
						SearchElement e = new SearchElement();
						Float lt = Float.MAX_VALUE;
						Float gt = Float.MIN_VALUE;
						if (!temp.lessThan.getText().equals("")){
							lt = Float.valueOf(temp.lessThan.getText());
						}
						if (!temp.greaterThan.getText().equals("")){
							gt = Float.valueOf(temp.greaterThan.getText());
						}
						if (temp.range.getItemText(temp.range.getSelectedIndex()).equals(rangeChoices[3])){
							gt = lt;
						}
						e.setValues(ee.getSymbol(),
								gt, 
								lt,
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
		while (realEditWidgets.size()>1){
			realEditWidgets.remove(0);
			editList.remove(0);
		}
		((RowContainer) realEditWidgets.get(0)).clear();
		currentCriteria.clear();
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
	
	public void setStyles() {
		if (editList.getWidgetCount() == 1) {
			MultipleInputPanel p = (MultipleInputPanel) editList.getWidget(0);
			p.setAlone(true);
			CSS.show(p.addButton);
		} else {
			for (int i=0; i<editList.getWidgetCount(); i++) {
				MultipleInputPanel p = (MultipleInputPanel) editList.getWidget(i);
				p.setAlone(false);
				if (i < editList.getWidgetCount()-1) CSS.hide(p.addButton);
				else CSS.show(p.addButton);
			}
		}
	}
	
}
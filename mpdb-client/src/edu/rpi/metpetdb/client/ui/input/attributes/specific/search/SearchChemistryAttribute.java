package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.BooleanConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.input.attributes.FlyOutAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.FlyOutAttribute.FlyOutItem;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MPartialCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MSuggestText;
import edu.rpi.metpetdb.client.ui.widgets.MText;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;
import edu.rpi.metpetdb.client.ui.widgets.MPartialCheckBox.CheckedState;
import edu.rpi.metpetdb.client.ui.widgets.panels.MultipleInputPanel;

public class SearchChemistryAttribute extends SearchGenericAttribute {
	private Map<RowContainer,String> currentCriteria = new HashMap<RowContainer,String>();
	private MHtmlList editList;
	private final static String lessThanEqualToHTML = " &#8804 ";
	protected final ArrayList<Widget> realEditWidgets = new ArrayList<Widget>();
	private static final String STYLENAME = "chem-list";
	
	private static final String[] rangeChoices = {"is between", "is greater than", "is less than", "is equal to" };
	
	private ObjectConstraint oxideConstraint;
	private ObjectConstraint elementConstraint;
	private ValueInCollectionConstraint mineralConstraint;
	private BooleanConstraint wholeRockConstraint;

	private class RowContainer extends FlowPanel implements ChangeListener{
		private ChemMSuggestText st;
		private ListBox range = new ListBox();
		private ListBox unit = new ListBox();
		private TextBox lessThan = new TextBox();
		private TextBox greaterThan = new TextBox();
		private TextBox precision = new TextBox();
		{
			lessThan.addKeyboardListener(new NumericKeyboardListener());
			greaterThan.addKeyboardListener(new NumericKeyboardListener());
			precision.addKeyboardListener(new NumericKeyboardListener());
		};
		private final MText and = new MText("and","span");
		private final MText percent = new MText("%", "span");
		private final HTML plusminus = new HTML("&#177;");
		private InlineLabel mineral;
		private Button selectMineral;
		private FlyOutAttribute tree;
		private MPartialCheckBox cb = new MPartialCheckBox(LocaleHandler.lc_entity.ChemicalAnalysis_largeRock()); 
		private Boolean wholeRock = false;
		final PopupPanel pp = new PopupPanel(false){};
		final FlowPanel container = new FlowPanel();
		
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
			@Override
			public void notifyCompletion(){
				RowContainer.this.onChange(this);
			}
		}
		
		public RowContainer(final MObject obj, final String id){
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
			
			mineral = new InlineLabel("");
			tree = new FlyOutAttribute(mineralConstraint, 1);
				
			Widget [] ws = tree.createEditWidget(obj, id, mineralConstraint);
			
			cb.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					tree.uncheckRest(null);
					wholeRock = cb.getState().equals(CheckedState.CHECKED) ? true : false;
				}
			});
			tree.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					cb.setState(CheckedState.UNCHECKED);
					wholeRock = false;
				}
			});
			
			final Button finish = new Button("Finish");
			final Button cancel = new Button(LocaleHandler.lc_text.buttonCancel());
			
			finish.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					pp.hide();
					if (cb.getState() == CheckedState.CHECKED){
						mineral.setText(cb.getText());
						selectMineral.setText("Change");
					} else if (tree.getSelectedWidgets().size() > 0){
						FlyOutItem selectedMineral = (FlyOutItem) tree.getSelectedWidgets().get(0);
						while (selectedMineral.parent != null && tree.getSelectedWidgets().contains(selectedMineral.parent)){
							selectedMineral = selectedMineral.parent;
						}
						mineral.setText(selectedMineral.obj.toString());
						selectMineral.setText("Change");
					} else {
						mineral.setText("");
					}
					onChange(finish);
				}
			});
			
			cancel.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){
					pp.hide();
					clearTree(false);
					if (mineral.getText().equals(LocaleHandler.lc_entity.ChemicalAnalysis_largeRock())){
						cb.setState(CheckedState.CHECKED);
					} else
						tree.checkByString(mineral.getText());
				}
			});
			
			container.add(cb);
			for(Widget w : ws) 
				container.add(w);
			container.add(cancel);
			finish.addStyleName(CSS.PRIMARY_BUTTON);
			container.add(finish);
			
			pp.add(container);
			pp.setStyleName("mineral-chooser-popup");
			
			selectMineral = new Button("Select Mineral&#8230;");
			selectMineral.addClickListener(new ClickListener(){
				public void onClick(final Widget sender){	
					pp.setPopupPosition(selectMineral.getAbsoluteLeft(), selectMineral.getAbsoluteTop()+20);
					pp.show();
				}
			});
			
			final InlineLabel in = new InlineLabel("in");
			
			add(st);
			add(range);
			add(unit);
			add(in);
			add(mineral);
			add(selectMineral);
			
			reset(range.getItemText(range.getSelectedIndex()));
		}
		
		public void onChange(final Widget sender){
			reset(range.getItemText(range.getSelectedIndex()));
			String displayString = "";
			String mineralString = "";
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
			if (mineral.getText() != ""){
				mineralString = " in " + mineral.getText();
			}
			if (!displayString.equals("")){
				// between
				if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[0]) && 
						!lessThan.getText().equals("") && !greaterThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is between " + greaterThan.getText() + " and " +  
							lessThan.getText() + " " + unit.getValue(unit.getSelectedIndex()) + mineralString);
				// greater than
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[1]) && 
						!greaterThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is greater than " + greaterThan.getText() + 
							" " + unit.getValue(unit.getSelectedIndex())+ mineralString);
				// less than
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[2]) && 
						!lessThan.getText().equals("")){
					currentCriteria.put(this, displayString + " is less than " + lessThan.getText() + 
							" " + unit.getValue(unit.getSelectedIndex())+ mineralString);
				// equal to
				} else if (range.getItemText(range.getSelectedIndex()).equals(rangeChoices[3]) && 
						!lessThan.getText().equals("")){
					currentCriteria.put(this, displayString + 
							" is " + lessThan.getText() + " " + unit.getValue(unit.getSelectedIndex())+ mineralString);
				}
				
			} else if (mineral.getText() != ""){
				currentCriteria.put(this,"Mineral: " + mineral.getText());
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
			clearTree(true);
		}
		
		public void clearTree(final Boolean clearMineral){
			cb.setState(CheckedState.UNCHECKED);
			tree.uncheckRest(null);
			if (clearMineral){
				selectMineral.setHTML("Select Mineral&#8230;");
				mineral.setText("");
			}
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

	public SearchChemistryAttribute(final ObjectConstraint elements, final ObjectConstraint oxides, 
			final ValueInCollectionConstraint minerals, final BooleanConstraint wholeRock) {
		super(new PropertyConstraint[] {
				elements, oxides, minerals, wholeRock
		});
		elementConstraint = elements;
		oxideConstraint = oxides;
		mineralConstraint = minerals;
		wholeRockConstraint = wholeRock;
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
			Set<SearchElement> s = (Set<SearchElement>) m.get(elementConstraint);
			final Iterator iter = s.iterator();
			while (iter.hasNext()) {
				final Object object = iter.next();
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(obj, object, id);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				((RowContainer)t.getInputWidget()).onChange(t.getInputWidget());
			}
			Set<SearchOxide> ss = (Set<SearchOxide>) m.get(oxideConstraint);
			final Iterator iter2 = ss.iterator();
			while (iter2.hasNext()) {
				final Object object = iter2.next();
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(obj, object, id);
				editList.add(t);
				realEditWidgets.add(t.getInputWidget());
				((RowContainer)t.getInputWidget()).onChange(t.getInputWidget());
			}
		}

		MultipleInputPanel t = createOptionalSuggestBox(obj, null, id);
		editList.add(t);
		realEditWidgets.add(t.getInputWidget());

		return new Widget[] {
				editList, editList, editList, editList
		};
	}
	
	public MultipleInputPanel createOptionalSuggestBox(final MObject obj, final Object s, final String id) {
		final MultipleInputPanel panel = new MultipleInputPanel();
		final RowContainer rc = new RowContainer(obj,id);
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
				if (e.getWholeRock()){
					rc.cb.setState(CheckedState.CHECKED);
					rc.mineral.setText(LocaleHandler.lc_entity.ChemicalAnalysis_largeRock());
					rc.selectMineral.setText("Change");
				}
				if (e.getMinerals() != null){
					Mineral m = findParentMineral(e.getMinerals());
					if (m != null){
						rc.tree.checkByString(m.getName());
						rc.mineral.setText(m.getName());
						rc.selectMineral.setText("Change");
					}
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
				if (o.getWholeRock()){
					rc.cb.setState(CheckedState.CHECKED);
					rc.mineral.setText(LocaleHandler.lc_entity.ChemicalAnalysis_largeRock());
					rc.selectMineral.setText("Change");
				}
				if (o.getMinerals() != null){
					Mineral m = findParentMineral(o.getMinerals());
					if (m != null){
						rc.tree.checkByString(m.getName());
						rc.mineral.setText(m.getName());
						rc.selectMineral.setText("Change");
					}
				}
				rc.reset(rc.range.getItemText(rc.range.getSelectedIndex()));
			}
		}
		
		panel.setInputWidget(rc);
		panel.addButton.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				MultipleInputPanel t = SearchChemistryAttribute.this.createOptionalSuggestBox(obj, null, id);
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
					currentCriteria.remove(rc);
					SearchChemistryAttribute.this.getSearchInterface().createCritera();
				}
				setStyles();
			}
		});
		return panel;
	}
	
	private Mineral findParentMineral(final Set<Mineral> minerals){
		Iterator<Mineral> itr = minerals.iterator();
		while (itr.hasNext()){
			Mineral m = itr.next();
			if (m.getParentId() == null)
				return m;
			else if (minerals.containsAll(m.getChildren())){
				return m;
			}
		}
		return null;
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
		if (constraint == oxideConstraint) {
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
						o.setWholeRock(temp.wholeRock);
						o.setMinerals(new HashSet<Mineral>(temp.tree.getSelectedItems()));
						Oxides.add(o);
					}
					catch (Exception e){
						
					}
				}
			}
			return Oxides;
		} else if (constraint == elementConstraint){
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
						e.setWholeRock(temp.wholeRock);
						e.setMinerals(new HashSet<Mineral>(temp.tree.getSelectedItems()));
						Elements.add(e);
						
					}
					catch (Exception e){
						
					}
				}
			}
			return Elements;
		} else if (constraint == wholeRockConstraint){
			Iterator<RowContainer> itr = currentCriteria.keySet().iterator();
			
			while (itr.hasNext()){
				final RowContainer temp = itr.next();
				if (temp.st.getText().equals("") && temp.wholeRock)
					return new Boolean(true);
			}
			return new Boolean(false);
		} else if (constraint == mineralConstraint){
			// search for minerals with no oxide/element
			Iterator<RowContainer> itr = currentCriteria.keySet().iterator();
			final Set<Mineral> chemMinerals = new HashSet<Mineral>();
			
			while (itr.hasNext()){
				final RowContainer temp = itr.next();
				if (temp.st.getText().equals(""))
					chemMinerals.addAll(temp.tree.getSelectedItems());
			}
			return chemMinerals;
		} else {
			return null;
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

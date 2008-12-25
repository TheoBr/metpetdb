package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

public class SearchMetamorphicGradeAttribute extends SearchGenericAttribute implements ClickListener{
	private int cols;
	private FlexTable editList;
	private SimplePanel editListContainer = new SimplePanel();
	/* To keep track of the physical object that is attached to the checkbox */
	private Map<CheckBox, Object> items;
	private Set<String> suggestions;
	private Set<Object> selected;
	public SearchMetamorphicGradeAttribute(final ObjectConstraint sc) {
		this(sc,1);		
	}
	
	public SearchMetamorphicGradeAttribute(final ObjectConstraint sc, int cols) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
		this.cols = cols;
	}
	
	public SearchMetamorphicGradeAttribute(final StringConstraint sc) {
		super(sc);
	}
	
	public Widget[] createDisplayWidget(final MObject obj){
		final MHtmlList list = new MHtmlList();

		final Set<?> s = get(obj);
		if (s != null) {
			final Iterator<?> itr = s.iterator();
			while (itr.hasNext()) {
				final Label r = new Label(itr.next().toString());
				list.add(r);
			}
		}
		return new Widget[] {
			list
		};
	}
	
	public Widget[] createEditWidget(final MObject obj, final String id){
		editList = new FlexTable();
		editList.setStyleName(CSS.SEARCH_ROCKTYPES_TABLE);
		selected = (Set<Object>) get(obj);

		items.clear();
		DOM.setElementAttribute(editList.getElement(), "id", id);
		
		editListContainer.setWidget(editList);
		editListContainer.setStyleName(CSS.SEARCH_ROCKTYPES);
		new ServerOp() {
			@Override
			public void begin() {
				MpDb.metamorphicGrade_svc.allMetamorphicGrades(this);
			}
			public void onSuccess(final Object result) {
				suggestions = ((Set<String>) result);
				createCheckBoxes();
			}
		}.begin();
		
		return new Widget[] {
			editListContainer
		};
	}
	
	public void createCheckBoxes(){
		if (suggestions != null) {
			final Iterator<?> iter = suggestions.iterator();
			final int numRows = (suggestions.size() / cols + suggestions
					.size()
					% cols) - 1;
			int row = 0;
			int column = 0;
			while (iter.hasNext()) {
				if (row >= numRows) {
					row = 0;
					column++;
				}
				final Object object = iter.next();
				// add all standard check boxes
				editList.setWidget(row, column, this.createCheckBox(object
						.toString(), selected.contains(object), object));
				row++;
			}
		}
	}
	public CheckBox createCheckBox(final String s, final boolean chosen,
			final Object value) {
		final MCheckBox rCheck = new MCheckBox(s == null ? "" : s, true);
		rCheck.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rCheck.applyCheckedStyle(rCheck.isChecked());
				if (rCheck.isChecked())
					selected.add(items.get(rCheck));
				else
					selected.remove(items.get(rCheck));
				SearchMetamorphicGradeAttribute.this.getSearchInterface().createCritera();
			}
		});
		rCheck.setChecked(chosen);
		items.put(rCheck, value);
		return rCheck;
	}

	protected void set(final MObject obj, final Object o) {
		mSet(obj, o);
	}

	public void onClick(final Widget sender) {
		setObjects(new ArrayList<Object>(Arrays.asList((items.keySet().toArray()))));
	}

	public Set<?> get(final MObject obj) {
		return (Set<?>) obj.mGet(this.getConstraint().property);
	}
	
	protected Object get(Widget editWidget) throws ValidationException {
		return selected;
	}
	
	public void onClear(){
		for(CheckBox cb : items.keySet())
			cb.setChecked(false);
	}
	
	public ArrayList<Widget> getCriteria(){
		final ArrayList<Widget> criteria = new ArrayList<Widget>();
		String crit = "";
		for (Object o : selected){
			crit += o.toString() + ", ";
		}
		if (!crit.equals("")){
			crit = crit.substring(0,crit.length()-2);
			criteria.add(createCritRow(crit));
		}	
		return criteria;
	}
}

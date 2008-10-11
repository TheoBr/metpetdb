package edu.rpi.metpetdb.client.ui.input.attributes.specific.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.HasValues;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;

public class SearchRockTypesAttribute extends SearchGenericAttribute implements ClickListener {
	private int cols;
	private FlexTable editList;
	private SimplePanel editListContainer = new SimplePanel();
	/* To keep track of the physical object that is attached to the checkbox */
	private Map<CheckBox, Object> items;

	public SearchRockTypesAttribute(final PropertyConstraint sc) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
		cols = 1;
	}

	public SearchRockTypesAttribute(final PropertyConstraint sc, final int cols) {
		super(sc);
		items = new HashMap<CheckBox, Object>();
		this.cols = cols;
	}

	public Widget[] createDisplayWidget(final MObject obj) {
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

	public Widget[] createEditWidget(final MObject obj, final String id) {
		editList = new FlexTable();
		editList.setStyleName(CSS.SEARCH_ROCKTYPES_TABLE);
		

		items.clear();
		DOM.setElementAttribute(editList.getElement(), "id", id);

		final Set<?> chosenItems = get(obj);
		final Collection<?> availableItems = ((HasValues) this.getConstraint())
				.getValues();
		if (availableItems != null) {
			final Iterator<?> iter = availableItems.iterator();
			final int numRows = (availableItems.size() / cols + availableItems
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
				editList.setWidget(row, column, this.createCheckBoxes(object
						.toString(), chosenItems.contains(object), object));
				row++;
			}
		}
		
		editListContainer.setWidget(editList);
		editListContainer.setStyleName(CSS.SEARCH_ROCKTYPES);
		
		return new Widget[] {
			editListContainer
		};
	}

	public CheckBox createCheckBoxes(final String s, final boolean chosen,
			final Object value) {
		final MCheckBox rCheck = new MCheckBox(s == null ? "" : s, true);
		rCheck.setStylePrimaryName("rt-checkbox");
		rCheck.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				rCheck.applyCheckedStyle(rCheck.isChecked());
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
		final Iterator<CheckBox> itr = items.keySet().iterator();
		final Set<Object> chosenItems = new HashSet<Object>();
		while (itr.hasNext()) {
			final CheckBox cb = itr.next();
			if (cb.isChecked())
				chosenItems.add(items.get(cb));
		}
		return chosenItems;
	}
	
	public void onRemoveCriteria(final Object obj){
		if (items.get(obj) != null)
			((MCheckBox) obj).setChecked(false);
	}
	
	public ArrayList<Pair> getCriteria(){
		final ArrayList<Pair> criteria = new ArrayList<Pair>();
		final Iterator<CheckBox> itr = items.keySet().iterator();
		while (itr.hasNext()) {
			final CheckBox cb = itr.next();
			if (cb.isChecked())
				criteria.add(new Pair(createCritRow("Rock Type:", items.get(cb).toString()), cb));
		}
		return criteria;
	}
	

}

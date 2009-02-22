package edu.rpi.metpetdb.client.ui.input;

// probably should have result return list, but not sure how to do that yet 

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchInterface;
import edu.rpi.metpetdb.client.ui.widgets.MButton;

public abstract class ObjectSearchPanel<T extends MObject> extends FlowPanel {
	private T bean;
	protected ArrayList<SearchGenericAttribute> attributes;
	protected ArrayList<Widget[]> currentEditWidgets;
	private final MButton searchBtn = new MButton("Search");
	private final SearchInterface atts;
	
//	final Element resultsTd;

	protected ObjectSearchPanel(final SearchInterface atts) {
		this.setStyleName("criteria");
		
		bean = (T) new SearchSample();
		this.atts = atts;
		attributes = atts.getAttributes();
		
		searchBtn.setStyleName(CSS.SEARCH_BUTTON);
		searchBtn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				doSearch();
			}
		});
		atts.passActionWidget(searchBtn);
	}
	
	
	public T getBean(){
		return bean;
	}

	public void setBean(final T o) {
		bean = o;
	}
	
	public synchronized void edit(final SearchSample obj) {
		clear();
		Widget[] w = atts.createEditWidget(obj, "TEMP");
		currentEditWidgets = atts.getCurrentEditWidgets();
		for (int i = 0; i < w.length; i++){
			add(w[i]);
		}
	}

	public void doSearch() {
		new FormOp<T>(this) {
			protected void onSubmit() {
				startValidation(this);
			}
			public void onSuccess(final T result) {
				new FormOp<T>(ObjectSearchPanel.this) {
					protected void onSubmit() {
						searchBean(this);
						onSuccess(null);
					}
					public void onSuccess(final T result) {
						onSearchCompletion(this);
					}
				}.begin();
			}
		}.begin();
	}
	
	public void startValidation(final ServerOp<?> r) {
		new ServerOp<Object>() {
			int succeeded;

			public void begin() {
				validateEdit(this);
			}

			public void onSuccess(final Object result) {
				succeeded++;
				if (succeeded == getConstraintCount()) {
					r.onSuccess(null);
				}
			}

			public void onFailure(final Throwable e) {
				r.onFailure(e);
				// super.onFailure(e);
			}
		}.begin();
	}
	
	public int getConstraintCount(){
		int count = 0;
		for (SearchGenericAttribute s : attributes){
			count += s.getConstraints().length;
		}
		return count;
	}
	
	public boolean validateEdit() {
		return validateEdit(null);
	}

	public boolean validateEdit(final ServerOp<?> r) {
		int failed = 0;
		for (int row = 0; row < attributes.size(); row++) {
			final SearchGenericAttribute attr = (SearchGenericAttribute) attributes
					.get(row);
			final CurrentError err = new CurrentError();
			if (!attr.getReadOnly()) {
				if ((attr.getImmutable() && bean.mIsNew())
						|| !attr.getImmutable()) {
					attr.commitEdit(bean, getEditWidgets(attr), err, r);
				}
			} else if (r != null) {
				r.onSuccess(null);
			}
			if (attr.getReadOnly())
				err.setText("");
			if (err.isVisible())
				failed++;
		}
		return failed == 0;
	}
	
	private Widget[] getEditWidgets(final SearchGenericAttribute attr) {
		return currentEditWidgets.get(attributes.indexOf(attr));
	}
	
	protected void searchBean(final AsyncCallback<T> ac) {
		throw new UnsupportedOperationException();
	}
	protected void onSearchCompletion(final FormOp<T> ac) {
		
	}
}

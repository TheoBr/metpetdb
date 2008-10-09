package edu.rpi.metpetdb.client.ui.input;

// probably should have result return list, but not sure how to do that yet 

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchInterface;

public abstract class ObjectSearchPanel<T extends MObject> extends FlowPanel implements
		ClickListener {
	final Button search;
	private T bean;
	protected ArrayList<SearchGenericAttribute> attributes;
	protected ArrayList<Widget[]> currentEditWidgets;
//	final Element resultsTd;

	protected ObjectSearchPanel(final SearchInterface atts,
			final String header, final String description) {
		search = new Button(LocaleHandler.lc_text.search(), this);
		search.setStyleName(CSS.SEARCH_BUTTON);

		search.setVisible(true);
		bean = (T) new SearchSample();
		Widget[] w = atts.createEditWidget(new SearchSample(), "TEMP");
		attributes = atts.getAttributes();
		currentEditWidgets = atts.getCurrentEditWidgets();
		for (int i = 0; i < w.length; i++){
			add(w[i]);
		}
		add(search);
	}

	protected ObjectSearchPanel(final SearchInterface atts) {
		this(atts, "", "");
	}
	
	public T getBean(){
		return bean;
	}

	public void setBean(final T o) {
		bean = o;
	}
	
	public void edit(final SearchSample obj) {
//		super.edit(obj);
	}

	public void onClick(final Widget sender) {
		if (search == sender)
			doSearch();
	}

	private void removePrimaryStyle() {
		search.removeStyleName("btnPrimary");
	}

	private void addSecondaryStyle() {
		search.removeStyleName("btnSecondary");
	}

	private void setActiveButton(final Button b) {
		removePrimaryStyle();
		addSecondaryStyle();
		b.addStyleName("btnPrimary");
		b.removeStyleName("btnSecondary");
	}

	void doSearch() {
		new FormOp<List<Sample>>(this) {
			protected void onSubmit() {
				startValidation(this);
				searchBean(this);
			}
			public void onSuccess(final List<Sample> result) {
				enable(true);
				onSearchCompletion(result);
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
				if (succeeded == attributes.size()) {
					r.onSuccess(null);
				}
			}

			public void onFailure(final Throwable e) {
				r.onFailure(e);
				// super.onFailure(e);
			}
		}.begin();
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
	
	public void show(final List<Sample> obj) {

		search.setVisible(true);
		search.setEnabled(true);
		setActiveButton(search);
	}
	protected void searchBean(final AsyncCallback<List<Sample>> ac) {
		throw new UnsupportedOperationException();
	}
	protected void onSearchCompletion(final List<Sample> result) {
		ObjectSearchPanel.this.show(result);
	}
}

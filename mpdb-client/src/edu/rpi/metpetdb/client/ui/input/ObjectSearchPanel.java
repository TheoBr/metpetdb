package edu.rpi.metpetdb.client.ui.input;

// probably should have result return list, but not sure how to do that yet 

import java.util.ArrayList;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.ui.CSS;
import edu.rpi.metpetdb.client.ui.commands.FormOp;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.input.attributes.specific.search.SearchGenericAttribute;
import edu.rpi.metpetdb.client.ui.search.SearchInterface;
import edu.rpi.metpetdb.client.ui.widgets.MButton;

public abstract class ObjectSearchPanel extends FlowPanel {
	private SearchSample bean;
	protected ArrayList<SearchGenericAttribute> attributes;
	protected ArrayList<Widget[]> currentEditWidgets;
	private final MButton searchBtn = new MButton("Search");
	private final SearchInterface atts;

	// final Element resultsTd;

	protected ObjectSearchPanel(final SearchInterface atts) {
		this.setStyleName("criteria");

		bean = new SearchSample();
		this.atts = atts;
		attributes = atts.getAttributes();

		searchBtn.setStyleName(CSS.SEARCH_BUTTON);
		searchBtn.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				doSearch();
			}
		});
		atts.addActionWidget(searchBtn);
	}

	public SearchSample getBean() {
		return bean;
	}

	public void setBean(final SearchSample o) {
		bean = o;
	}

	public void edit(final SearchSample obj) {
		clear();
		Widget[] w = atts.createEditWidget(obj, "TEMP");
		currentEditWidgets = atts.getCurrentEditWidgets();
		for (int i = 0; i < w.length; i++) {
			add(w[i]);
		}
		bean = obj;
	}

	public void doSearch() {
		new FormOp<SearchSample>(this) {
			protected void onSubmit() {
				startValidation(this);
			}
			public void onSuccess(final SearchSample result) {
				new FormOp<SearchSample>(ObjectSearchPanel.this) {
					protected void onSubmit() {
						performSearch();
					}
					public void onSuccess(final SearchSample result) {
						onSearchCompletion(this);
					}
				}.begin();
			}
		}.begin();
	}

	public void startValidation(final ServerOp<?> r) {
		validateEdit(new Command() {
			int succeeded;

			public void execute() {
				succeeded++;
				if (succeeded == getConstraintCount()) {
					r.onSuccess(null);
				}
			}
		});
	}

	public int getConstraintCount() {
		int count = 0;
		for (SearchGenericAttribute s : attributes) {
			count += s.getConstraints().length;
		}
		return count;
	}

	public boolean validateEdit() {
		return validateEdit(null);
	}

	public boolean validateEdit(final Command r) {
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
				r.execute();
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

	protected void performSearch() {
		throw new UnsupportedOperationException();
	}
	protected void onSearchCompletion(final FormOp<SearchSample> ac) {

	}
}

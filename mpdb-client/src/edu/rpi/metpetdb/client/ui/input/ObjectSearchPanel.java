package edu.rpi.metpetdb.client.ui.input;

// probably should have result return list, but not sure how to do that yet 

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public abstract class ObjectSearchPanel<T> extends DetailsPanel implements
		ClickListener {
	final Button search;

	protected ObjectSearchPanel(final GenericAttribute[] atts,
			final String header, final String description) {
		search = new Button(LocaleHandler.lc_text.search(), this);

		search.setVisible(true);

		init(atts, new Button[] {
			search
		}, true, true);
	}

	protected ObjectSearchPanel(final GenericAttribute[] atts) {
		this(atts, "", "");
	}

	public void edit(final MObjectDTO obj) {
		super.edit(obj);
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
		new FormOp<T>(this) {
			protected void onSubmit() {
				searchBean(this);
			}
			public void onSuccess(final T result) {
				onSearchCompletion((MObjectDTO) result);
			}
		}.begin();
	}

	public void show(final MObjectDTO obj) {
		super.show(obj);
		search.setVisible(true);
		search.setEnabled(true);
		setActiveButton(search);
	}

	protected void searchBean(final AsyncCallback<T> ac) {
		throw new UnsupportedOperationException();
	}
	protected void onSearchCompletion(final MObjectDTO result) {
		ObjectSearchPanel.this.show(result);
	}
}

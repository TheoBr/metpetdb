package edu.rpi.metpetdb.client.ui.input;

// probably should have result return list, but not sure how to do that yet 

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.ui.FormOp;
import edu.rpi.metpetdb.client.ui.input.attributes.GenericAttribute;

public abstract class ObjectSearchPanel extends DetailsPanel implements
		ClickListener {
	final Button search;
	final Element resultsTd;

	protected ObjectSearchPanel(final GenericAttribute[] atts,
			final String header, final String description) {
		search = new Button(LocaleHandler.lc_text.search(), this);

		search.setVisible(true);

		init(atts, new Button[] {
			search
		}, true, true);

		resultsTd = DOM.createTD();
		DOM.appendChild(this.tbody, resultsTd);
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
		new FormOp<List<SampleDTO>>(this) {
			protected void onSubmit() {
				searchBean(this);
			}
			public void onSuccess(final List<SampleDTO> result) {
				onSearchCompletion(result);
			}
		}.begin();
	}
	public void show(final List<SampleDTO> obj) {

		search.setVisible(true);
		search.setEnabled(true);
		setActiveButton(search);
	}
	protected void searchBean(final AsyncCallback<List<SampleDTO>> ac) {
		throw new UnsupportedOperationException();
	}
	protected void onSearchCompletion(final List<SampleDTO> result) {
		ObjectSearchPanel.this.show(result);
	}
}

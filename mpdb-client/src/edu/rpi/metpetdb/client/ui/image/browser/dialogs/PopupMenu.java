package edu.rpi.metpetdb.client.ui.image.browser.dialogs;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.ui.widgets.MHtmlList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class PopupMenu extends DialogBox implements ClickListener {

	private final MHtmlList ul;
	private final MLink close;

	public PopupMenu() {
		super(true);
		ul = new MHtmlList();
		close = new MLink(LocaleHandler.lc_text.buttonClose(), this);
		ul.add(close);
		final FocusPanel fp = new FocusPanel();
		final PopupMenu me = this;
		fp.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				me.hide();
			}
		});
		fp.setWidget(ul);
		this.setText("Menu");
		this.setWidget(fp);
		this.setStyleName("mpdb-menuPopup");
		DOM.setStyleAttribute(this.getElement(), "zIndex", "1000");
	}

	public void addItem(final Widget w) {
		if (w != null)
			ul.add(w);
	}

	public void onClick(final Widget sender) {
		if (sender == close) {
			this.hide();
		}
	}
}

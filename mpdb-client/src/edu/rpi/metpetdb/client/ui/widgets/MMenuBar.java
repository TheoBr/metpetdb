package edu.rpi.metpetdb.client.ui.widgets;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MMenuBar extends MenuBar {

	public MMenuBar(final boolean b) {
		super(b);
		if (b) setStyleName("hdrnavMenu");
	}

	public MMenuBar() {
		super();
	}

	public MenuItem addItem(final String text, final MMenuBar menuBar) {
		String anchor = (menuBar.getItems() != null && !menuBar.getItems().isEmpty()) ?
			"<a class=\"hasMenu\">" : "<a>";
		anchor += text + "</a>";
		return super.addItem(anchor, true, menuBar);
	}

	public MenuItem addItem(final String text, final MenuItem menuItem) {
		final Element anchor = DOM.createAnchor();
		if (menuItem.getSubMenu() != null)
			DOM.setElementAttribute(anchor, "class", "hasMenu");
		DOM.setInnerText(anchor, text);
		DOM.setInnerText(menuItem.getElement(), "");
		DOM.appendChild(menuItem.getElement(), anchor);
		return menuItem;
	}
	
	public MenuItem addItem(final String text, final Command cmd) {
		return super.addItem("<a>" + text + "</a>", true, cmd);
	}

	public List getItems() {
		return super.getItems();
	}

}

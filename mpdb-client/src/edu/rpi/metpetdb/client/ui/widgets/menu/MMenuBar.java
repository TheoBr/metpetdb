package edu.rpi.metpetdb.client.ui.widgets.menu;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class MMenuBar extends MenuBar {

	public MMenuBar(final boolean b) {
		super(b);
		if (b)
			setStyleName("hdrnavMenu");
	}

	public MMenuBar() {
		super();
	}

	public MenuItem addItem(final String text, final MMenuBar menuBar) {
		String anchor = (menuBar.getItems() != null && !menuBar.getItems()
				.isEmpty()) ? "<a class=\"hasMenu\">" : "<a>";
		anchor += text + "</a>";
		return super.addItem(anchor, true, menuBar);
	}

	public MenuItem addItem(final String text, final Command cmd) {
		return super.addItem("<a>" + text + "</a>", true, cmd);
	}

	public List<MenuItem> getItems() {
		return super.getItems();
	}

}

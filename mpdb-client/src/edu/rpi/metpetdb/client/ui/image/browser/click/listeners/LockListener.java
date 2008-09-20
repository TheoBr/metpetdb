package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class LockListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private HashSet<MLink> notifiers = new HashSet<MLink>();

	public LockListener(final ImageOnGridContainer imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		String text;
		if (iog.isLocked()) {
			text = "Lock";
		} else {
			text = "Unlock";
		}
		iog.setLocked(!iog.isLocked());
		final Iterator<MLink> itr = notifiers.iterator();
		while (itr.hasNext()) {
			((MLink) itr.next()).setText(text);
		}
	}

	public LockListener addNotifier(final MLink link) {
		notifiers.add(link);
		return this;
	}

}

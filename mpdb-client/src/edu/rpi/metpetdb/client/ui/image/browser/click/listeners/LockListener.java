package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class LockListener implements ClickListener {

	private final ImageOnGrid iog;
	private HashSet notifiers = new HashSet();

	public LockListener(final ImageOnGrid imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		String text;
		if (iog.getIsLocked()) {
			text = "Lock";
		} else {
			text = "Unlock";
		}
		iog.setIsLocked(!iog.getIsLocked());
		final Iterator itr = notifiers.iterator();
		while (itr.hasNext()) {
			((MLink) itr.next()).setText(text);
		}
	}

	public LockListener addNotifier(final MLink link) {
		notifiers.add(link);
		return this;
	}

}

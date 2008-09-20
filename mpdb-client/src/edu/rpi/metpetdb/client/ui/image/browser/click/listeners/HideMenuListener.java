package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class HideMenuListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final HashSet<MLink> notifiers = new HashSet<MLink>();

	public HideMenuListener(final ImageOnGridContainer imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		String text;
		if (iog.getImageContainer().getStyleName().equals("imageContainer")) {
			text = "Show Menu";
			iog.getImageContainer().setStyleName("imageContainerNoMenu");
			iog.setMenuHidden(true);
		} else {
			text = "Hide Menu";
			iog.getImageContainer().setStyleName("imageContainer");
			iog.setMenuHidden(false);
		}
		final Iterator<MLink> itr = notifiers.iterator();
		while (itr.hasNext()) {
			((MLink) itr.next()).setText(text);
		}

	}

	public void addNotifier(final MLink link) {
		notifiers.add(link);
	}

}

package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.HashSet;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class HideMenuListener implements ClickListener {

	private final ImageOnGrid iog;
	private final HashSet notifiers = new HashSet();

	public HideMenuListener(final ImageOnGrid imageOnGrid) {
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		String text;
		if (iog.getImageContainer().getStyleName().equals("imageContainer")) {
			text = "Show Menu";
			iog.getImageContainer().setStyleName("imageContainerNoMenu");
			iog.setIsMenuHidden(true);
		} else {
			text = "Hide Menu";
			iog.getImageContainer().setStyleName("imageContainer");
			iog.setIsMenuHidden(false);
		}
		final Iterator itr = notifiers.iterator();
		while (itr.hasNext()) {
			((MLink) itr.next()).setText(text);
		}

	}

	public void addNotifier(final MLink link) {
		notifiers.add(link);
	}

}

package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserMouseListener;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;

public class AddPointListener implements ClickListener {

	private final ImageBrowserMouseListener mouseListener;
	private final ImageOnGridContainer iog;

	public AddPointListener(final ImageBrowserMouseListener ibml,
			final ImageOnGridContainer imageOnGrid) {
		mouseListener = ibml;
		iog = imageOnGrid;
	}

	public void onClick(final Widget sender) {
		mouseListener.setMode(3);
		mouseListener.setCurrentImage(iog);
		final Image image = new Image(GWT.getModuleBaseURL()
				+ "/images/point0.gif");
		mouseListener.setPoint(image);
		DOM.setStyleAttribute(image.getElement(), "zIndex", "2000");
		iog.getImagePanel().add(image);
	}

}

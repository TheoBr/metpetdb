package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.Collection;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ZoomOutListener extends ZoomHandler implements ClickListener {

	public ZoomOutListener(final Collection<ImageOnGrid> imagesOnGrid, final Element e,
			final ImageBrowserDetails ibm) {
		super(imagesOnGrid, e, ibm);
	}

	public void onClick(final Widget sender) {
		final int zoomLevel = getCurrentZoomLevel();
		if (zoomLevel >= MINZOOM)
			return;

		zoom(-1);
		updateSlider();
	}

	public void updateSlider() {
		DOM.setStyleAttribute(zSlide, "top", getCurrentZoomPixel() + 10 + "px");
	}

}

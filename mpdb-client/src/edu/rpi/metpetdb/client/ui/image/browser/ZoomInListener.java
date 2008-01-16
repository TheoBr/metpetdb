package edu.rpi.metpetdb.client.ui.image.browser;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Grid;

public class ZoomInListener extends ZoomHandler implements ClickListener {

	public ZoomInListener(final Grid g, final Element e,
			final ImageBrowserDetails ibm) {
		super(g, e, ibm);
	}

	public void onClick(final Widget sender) {
		final int zoomLevel = getCurrentZoomLevel();
		if (zoomLevel <= MAXZOOM)
			return;

		zoom(1);
		updateSlider();
	}

	public void updateSlider() {
		DOM.setStyleAttribute(zSlide, "top", getCurrentZoomPixel() - 10 + "px");
	}

}

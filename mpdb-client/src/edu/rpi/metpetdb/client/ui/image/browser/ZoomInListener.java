package edu.rpi.metpetdb.client.ui.image.browser;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ZoomInListener implements ClickListener {
	
	private final ZoomHandler zoomer;

	public ZoomInListener(final ZoomHandler zoomer) {
		this.zoomer = zoomer;
	}

	public void onClick(final Widget sender) {
		final int zoomLevel = zoomer.getCurrentZoomLevel();
		if (zoomLevel <= ZoomHandler.MAXZOOM)
			return;

		zoomer.zoom(1);
		zoomer.updateSlider(-10);
	}
}

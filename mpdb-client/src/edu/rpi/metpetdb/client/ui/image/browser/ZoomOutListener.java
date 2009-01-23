package edu.rpi.metpetdb.client.ui.image.browser;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ZoomOutListener implements ClickListener {
	
	private final ZoomHandler zoomer;

	public ZoomOutListener(final ZoomHandler zoomer) {
		this.zoomer = zoomer;
	}

	public void onClick(final Widget sender) {
		final int zoomLevel = zoomer.getCurrentZoomLevel();
		if (zoomLevel >= ZoomHandler.MINZOOM)
			return;

		zoomer.zoom(-1);
		zoomer.updateSlider(10);
	}
}

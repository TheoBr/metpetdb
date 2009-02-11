package edu.rpi.metpetdb.client.ui.sidebar;

import com.google.gwt.user.client.ui.FlowPanel;

import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.ui.image.browser.LayersSidebar;

public class Sidebar extends FlowPanel {


	private static LayersSidebar l;
	private static MySubsamples ms;
	private static Subsample s;

	public Sidebar() {
	}

	public static void insertLayersLeftSide(final LayersSidebar layer,
			final Subsample sub) {
		l = layer;
		s = sub;
		if (ms != null) {
			ms.insertLayers(l, s);
		}
	}

	public static void insertMySubsamplesLeftSide(final MySubsamples subsamples) {
		ms = subsamples;
		if (l != null) {
			ms.insertLayers(l, s);
		}
	}

	public static void clearLeft() {
		ms = null;
		l = null;
		s = null;
	}
}

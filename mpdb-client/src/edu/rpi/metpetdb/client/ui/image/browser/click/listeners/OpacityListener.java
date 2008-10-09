package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.OpacityPopup;
import edu.rpi.metpetdb.client.ui.widgets.MAbsolutePanel;

public class OpacityListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final MAbsolutePanel grid;

	public OpacityListener(final ImageOnGridContainer imageOnGrid,
			final MAbsolutePanel map) {
		iog = imageOnGrid;
		grid = map;
	}

	public void onClick(final Widget sender) {
		new ServerOp<String>() {
			public void begin() {
				new OpacityPopup(iog, this, iog.getTemporaryTopLeftX()
						+ grid.getAbsoluteLeft(), iog.getTemporaryTopLeftY()
						+ grid.getAbsoluteTop()).show();
			}
			public void onSuccess(final String result) {
				setOpacity(iog.getActualImage().getElement(), (int) Double
						.parseDouble((String) result));
				setOpacity(iog.getImageContainer().getElement(), (int) Double
						.parseDouble((String) result));
			}
		}.begin();
	}

	public void setOpacity(final Element element, final int amount) {
		DOM.setStyleAttribute(element, "opacity", String
				.valueOf(amount / 100.0));
		DOM.setStyleAttribute(element, "filter", "alpha(opacity=" + amount
				+ ")");
	}

}

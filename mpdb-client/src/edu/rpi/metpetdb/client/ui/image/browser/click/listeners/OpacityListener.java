package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserDetails;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.OpacityPopup;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.RotateDialog;
import edu.rpi.metpetdb.client.ui.widgets.panels.MAbsolutePanel;

public class OpacityListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final MAbsolutePanel grid;
	private final ImageBrowserDetails imageBrowser;

	public OpacityListener(final ImageOnGridContainer imageOnGrid,
			final MAbsolutePanel map, final ImageBrowserDetails imageBrowser) {
		iog = imageOnGrid;
		grid = map;
		this.imageBrowser = imageBrowser;
	}

	public void onClick(final Widget sender) {
		new ServerOp<String>() {
			public void begin() {
				new OpacityPopup(iog, this, (int)iog.getCurrentContainerPosition().x
						+ grid.getAbsoluteLeft(), (int)iog.getCurrentContainerPosition().y
						+ grid.getAbsoluteTop()).show();
			}
			public void onSuccess(final String result) {
				List<ImageOnGridContainer> imagesToChange = imageBrowser.getSelectedImages();
				if (!imageBrowser.getSelectedImages().contains(iog)){
					imageBrowser.selectionHandler.selectImage(iog);
					imagesToChange.addAll(imageBrowser.selectionHandler.getGroupByImage(iog));
				}
				for (ImageOnGridContainer iog : imagesToChange) {
					setOpacity(iog.getActualImage().getElement(), (int) Double
							.parseDouble((String) result));
					setOpacity(iog.getImageContainer().getElement(), (int) Double
							.parseDouble((String) result));
				}
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

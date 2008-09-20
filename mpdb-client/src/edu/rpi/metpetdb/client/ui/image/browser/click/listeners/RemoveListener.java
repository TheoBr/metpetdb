package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.Map;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;

public class RemoveListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final LeftSideLayer leftSideLayer;
	private final Map<Image, ImageOnGridContainer> imagesOnGrid;

	public RemoveListener(final ImageOnGridContainer imageOnGrid,
			final LeftSideLayer lsl,
			final Map<Image, ImageOnGridContainer> imagesOnGrid) {
		iog = imageOnGrid;
		leftSideLayer = lsl;
		this.imagesOnGrid = imagesOnGrid;
	}

	public void onClick(final Widget sender) {
		if (sender.getParent().getParent() instanceof FlexTable) {
			((DialogBox) sender.getParent().getParent().getParent()).hide();
		}
		leftSideLayer.removeImage(iog);
		imagesOnGrid.remove(iog.getIog().getImage());
		iog.getIog().delete();
		iog.getImageContainer().removeFromParent();

	}

}

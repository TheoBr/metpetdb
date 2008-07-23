package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.Map;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGrid;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;

public class RemoveListener implements ClickListener {

	private final ImageOnGrid iog;
	private final LeftSideLayer leftSideLayer;
	private final Map<ImageDTO, ImageOnGrid> imagesOnGrid;

	public RemoveListener(final ImageOnGrid imageOnGrid,
			final LeftSideLayer lsl,
			final Map<ImageDTO, ImageOnGrid> imagesOnGrid) {
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

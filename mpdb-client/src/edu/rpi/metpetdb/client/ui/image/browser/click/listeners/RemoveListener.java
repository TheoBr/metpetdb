package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.ui.image.browser.LeftSideLayer;

public class RemoveListener implements ClickListener {

	private final ImageOnGridDTO iog;
	private final LeftSideLayer leftSideLayer;

	public RemoveListener(final ImageOnGridDTO imageOnGrid, final LeftSideLayer lsl) {
		iog = imageOnGrid;
		leftSideLayer = lsl;
	}

	public void onClick(final Widget sender) {
		if (sender.getParent().getParent() instanceof FlexTable) {
			((DialogBox) sender.getParent().getParent().getParent()).hide();
		}
		leftSideLayer.removeImage(iog);
		iog.delete();
		iog.getImageContainer().removeFromParent();
	}

}

package edu.rpi.metpetdb.client.ui.image.browser.click.listeners;

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.ui.image.browser.ImageOnGridContainer;
import edu.rpi.metpetdb.client.ui.image.browser.LayersSidebar;

public class RemoveListener implements ClickListener {

	private final ImageOnGridContainer iog;
	private final LayersSidebar leftSideLayer;
	private final Map<Image, ImageOnGridContainer> imagesOnGrid;

	public RemoveListener(final ImageOnGridContainer imageOnGrid,
			final LayersSidebar lsl,
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
		final Iterator<ChemicalAnalysis> itr = iog.getChemicalAnalyses().iterator();
		while (itr.hasNext()) {
			final ChemicalAnalysis ma = itr.next();
			final com.google.gwt.user.client.ui.Image i = (com.google.gwt.user.client.ui.Image) ma.getActualImage();
			i.removeFromParent();
		}
		iog.getIog().delete();
		iog.getImageContainer().removeFromParent();

	}

}
